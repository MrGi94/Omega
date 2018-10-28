package AI;

import Controller.MapController;
import Model.*;

import java.util.*;
import java.util.concurrent.TimeoutException;

public class AIController {
    private ArrayList lastMoves;
    private short free_tiles_left;
    private long starting_time;
    private List<HistoryEntry> history;
    private int pruning;

    // divide total thinking time
    private long processing_time_per_turn;

    public AIController(short free_tiles_left) {
        this.free_tiles_left = free_tiles_left;
        this.processing_time_per_turn = GameData.PROCESSING_TIME / ((GameData.HEX_MAP.size()) / 4);
        this.starting_time = System.currentTimeMillis();
        this.lastMoves = new ArrayList();
        // to match the depth index
        this.lastMoves.add(0);
        this.pruning = 0;
        this.history = new ArrayList<>();
    }

    // random AI
    public byte playRandomMove() {
        Vector free_tiles = getFreeTiles(GameData.UNION_FIND_TILE_ARRAY, true);
        return (byte) free_tiles.get((int) (Math.random() * free_tiles.size()));
    }

    private boolean isTerminalNode() {
        return (GameData.HEX_MAP.size() - GameData.HEX_MAP.size() / 4 * 4) == free_tiles_left;
    }

    private UnionFindTile[] copyBoard(UnionFindTile[] old_board) {
        UnionFindTile[] p = new UnionFindTile[old_board.length];
        for (byte i = 0; i < p.length; i++) {
            p[i] = new UnionFindTile(old_board[i]);
        }
        return p;
    }

    private Vector<Byte> getFreeTiles(UnionFindTile[] position_array, boolean emptyTiles) {
        Vector tiles = new Vector();
        for (byte i = 0; i < position_array.length; i++) {
            if (emptyTiles) {
                if (position_array[i].getColor() == 0)
                    tiles.add(i);
            } else {
                if (position_array[i].getColor() != 0)
                    tiles.add(i);
            }

        }
        return tiles;
    }

    private long numbSuccessorsPrime() {
        return free_tiles_left;
    }

    private byte[] getOpenBookMove() {
        byte[] move_array = new byte[2];
        if (!GameData.OPEN_BOOK_CENTER.isEmpty() && (GameData.HEX_MAP.size() - GameData.FREE_TILES_LEFT) / 4 < 3) {
            for (Object center_pos : GameData.OPEN_BOOK_CENTER) {
                if (GameData.UNION_FIND_TILE_ARRAY[(byte) center_pos].getColor() == 0) {
                    move_array[0] = (byte) center_pos;
                    break;
                }
            }

            for (Object corner_pos : GameData.OPEN_BOOK_CORNERS) {
                if (GameData.UNION_FIND_TILE_ARRAY[(byte) corner_pos].getColor() == 0) {
                    move_array[1] = (byte) corner_pos;
                    break;
                }
            }
        }
        return move_array;
    }

    private LinkedHashSet<Byte> generateClusterParentID(UnionFindTile[] board) {
        LinkedHashSet cluster_parent_id = new LinkedHashSet<>();
        for (UnionFindTile aBoard : board) {
            if (aBoard.getPlacement_id() == aBoard.getParent()) {
                cluster_parent_id.add(aBoard.getParent());
            }
        }
        return cluster_parent_id;
    }

    private HistoryEntry getBestHistoryEntry() {
        if (!history.isEmpty()) {
            // sort move history, pick best score and continue from there
            history.sort(new HistoryEntrySort());
            return history.get(0);
        }
        ArrayList<Byte> list = new ArrayList<>();
        list.add((byte) 0);
        list.add(playRandomMove());
        list.add(playRandomMove());
        return new HistoryEntry(list);
    }

    private List<HistoryEntry> getHistoryEntriesByValue(HistoryEntry entry) {
        Iterator it = history.iterator();
        List<HistoryEntry> history_list = new ArrayList<>();
        while (it.hasNext()) {
            HistoryEntry he = (HistoryEntry) it.next();
            if (he.getValue() == entry.getValue() && he.getDepth() == entry.getDepth()) {
                history_list.add(he);
            }
        }
        return history_list;
    }

    public byte[] OmegaPrime(int depth_limit) {
        // performs two open book moves if the board size is bigger than 3
        if (GameData.OPEN_BOOK_CENTER != null && !GameData.OPEN_BOOK_CENTER.isEmpty() && (GameData.HEX_MAP.size() - GameData.FREE_TILES_LEFT) / 4 < 2) {
            return getOpenBookMove();
        }

        // performs iterative deepening and stops when time per turn runs out
        for (byte d = 2; d <= depth_limit * 2; d = (byte) (d + 2)) {
            try {
                if (!history.isEmpty()) {
                    // selects most promising entries with equal value and depth and explores them
                    List<HistoryEntry> entry_list = getHistoryEntriesByValue(getBestHistoryEntry());
                    for (HistoryEntry entry : entry_list) {
                        ArrayList moveHistory = entry.getMoveHistory();
                        boolean firstPiece = false;
                        free_tiles_left = (short) getFreeTiles(GameData.UNION_FIND_TILE_ARRAY, true).size();
                        UnionFindTile[] board = placeMove(GameData.UNION_FIND_TILE_ARRAY, (byte) moveHistory.get(1), true);
                        for (int idx = 2; idx < moveHistory.size(); idx++) {
                            board = placeMove(board, (byte) moveHistory.get(idx), firstPiece);
                            firstPiece = !firstPiece;
                        }
                        generateClusterParentID(board);
                        NegaMax(board, (byte) 2, Short.MIN_VALUE, Short.MAX_VALUE);
                    }
                    System.out.println("---------- depth " + d + " -------------");
                    System.out.println("History size is: " + history.size());
                    System.out.println("Pruning: " + pruning);
                } else {
                    // performs normal NegaMax with no history
                    NegaMax(GameData.UNION_FIND_TILE_ARRAY, d, Short.MIN_VALUE, Short.MAX_VALUE);
                    System.out.println("---------- depth " + d + " -------------");
                    System.out.println("History size is: " + history.size());
                    System.out.println("Pruning: " + pruning);
                }
            } catch (TimeoutException e) {
                System.out.println("TimeoutException detected");
                return getBestHistoryEntry().getBestMoves();
            }
        }
        return getBestHistoryEntry().getBestMoves();
    }

    // places the specific move on the board and updates all relevant information
    private UnionFindTile[] placeMove(UnionFindTile[] position_array, byte move, boolean firstPiece) {
        UnionFindTile[] board = copyBoard(position_array);
        if ((GameData.HUMAN_PLAYER_FIRST && firstPiece) || (!GameData.HUMAN_PLAYER_FIRST && !firstPiece))
            board[move].setColor((byte) 1);
        else
            board[move].setColor((byte) 2);
        byte placement_id = (byte) (GameData.HEX_MAP.size() - free_tiles_left + 1);
        board[move].setPlacement_id(placement_id);
        board[move].setParent(placement_id);
        free_tiles_left--;
        board = unionTile(board, move);
        return board;
    }

    // selects the free tile matching with the child value and then performs that free move
    private UnionFindTile[] applySuccessor(UnionFindTile[] position_array, byte child, boolean firstPiece) {
        Vector moves_list = getFreeTiles(position_array, true);
        byte move = (byte) moves_list.get(child);
        lastMoves.add(move);
        return placeMove(position_array, move, firstPiece);
    }

    private UnionFindTile[] unionTile(UnionFindTile[] position_array, byte move) {
        Hexagon h = GameData.HEX_MAP_BY_ID.get(position_array[move].getTileId());
        ArrayList<Hexagon> valid_neighbors = MapController.getValidNeighbors(h);
        for (Hexagon entry : valid_neighbors) {
            byte tile_id = GameData.HEX_MAP.get(entry).getTileId();
            if (position_array[tile_id].getColor() == position_array[move].getColor()) {
                position_array = union(position_array[move].getPlacement_id(), position_array[tile_id].getPlacement_id(), position_array);
            }
        }
        return position_array;
    }

    private UnionFindTile[] union(byte placement_id1, byte placement_id2, UnionFindTile[] position_array) {
        UnionFindTile root1 = find(placement_id1, position_array);
        UnionFindTile root2 = find(placement_id2, position_array);
        if (root1 == root2) return position_array;
        // make root of smaller parent point to root of larger parent
        if (root1.getParent() < root2.getParent())
            position_array = linkUnionFindTile(root2, root1, position_array);
        else
            position_array = linkUnionFindTile(root1, root2, position_array);
        return position_array;
    }

    private UnionFindTile[] linkUnionFindTile(UnionFindTile largeParent, UnionFindTile smallParent, UnionFindTile[] position_array) {
        smallParent.setParent(largeParent.getParent());
        largeParent.setSize((byte) (smallParent.getSize() + largeParent.getSize()));
        position_array[largeParent.getTileId()] = largeParent;
        position_array[smallParent.getTileId()] = smallParent;
        return position_array;
    }

    private UnionFindTile find(byte placement_id, UnionFindTile[] position_array) {
        UnionFindTile uft = getUnionFindTileByPlacement(placement_id, position_array);
        do {
            uft = getUnionFindTileByPlacement(uft.getParent(), position_array);
        } while (uft.getPlacement_id() != uft.getParent());
        return uft;
    }

    private UnionFindTile getUnionFindTileByPlacement(byte placement_id, UnionFindTile[] position_array) {
        for (UnionFindTile aPosition_array : position_array) {
            if (aPosition_array.getPlacement_id() == placement_id)
                return aPosition_array;
        }
        return new UnionFindTile(placement_id);
    }

    private UnionFindTile[] unmakeMove(UnionFindTile[] position_array) {
        int size = lastMoves.size() - 1;
        UnionFindTile uft = position_array[(byte) lastMoves.get(size)];
        lastMoves.remove(size);
        Vector<Byte> occupied_tiles = getFreeTiles(position_array, false);
        for (byte tile : occupied_tiles) {
            UnionFindTile tmp = position_array[tile];
            if (tmp.getParent() == uft.getPlacement_id()) {
                tmp.setParent(tmp.getPlacement_id());
                position_array[tmp.getTileId()] = tmp;
            }
        }
        free_tiles_left++;
        return position_array;
    }

    private void storeMoveHistory(short bestValue) {
        byte depth = (byte) (lastMoves.size() - 1);
        ArrayList moveHistory = new ArrayList(lastMoves);
        history.add(new HistoryEntry(depth, bestValue, moveHistory));
    }

    private short evaluate(UnionFindTile[] position_array) {
        LinkedHashSet<Byte> cluster_parent_id = generateClusterParentID(position_array);
        Iterator it = cluster_parent_id.iterator();
        short[] score = {1, 1};
        // score[0] white score | score[1] black score
        while (it.hasNext()) {
            byte tile_id = getUnionFindTileByPlacement((byte) it.next(), position_array).getTileId();
            if (position_array[tile_id].getColor() == 1)
                score[0] = (short) (score[0] * position_array[tile_id].getSize());
            else
                score[1] = (short) (score[1] * position_array[tile_id].getSize());
        }
        if (GameData.HUMAN_PLAYER_FIRST)
            return (short) (score[1] - score[0]);
        else
            return (short) (score[0] - score[1]);
    }

    private short NegaMax(UnionFindTile[] position_array, byte depth, short alpha, short beta) throws TimeoutException {
        // checks for time out 
        if ((System.currentTimeMillis() - starting_time) > processing_time_per_turn)
            throw new TimeoutException();

        // int olda = alpha; /* save original alpha value */
        // TTEntry n = retrieve(position_array); /* Transposition-table lookup */
        // if (n.getDepth() >= depth) {
        // if (n.getFlag() == Constants.FLAG.EXACT) {
        // return n.getValue();
        // } else if (n.getFlag() == Constants.FLAG.LOWERBOUND) {
        // alpha = (short) Math.max(alpha, n.getValue());
        // } else if (n.getFlag() == Constants.FLAG.UPPERBOUND) {
        // beta = (short) Math.min(beta, n.getValue());
        // }
        // if (alpha >= beta) {
        // return n.getValue();
        // }
        // }

        if (isTerminalNode() || depth == 0) {
            return evaluate(position_array);
        }

        // d1&d2 player maximizes, d3&d4 player minimizes, d5%d6 player maximizes, ...
        boolean maximizeAI = (depth % 4 == 1 || depth % 4 == 2);
        // d1 second turn, d2 first turn, d3 second turn, d4 first turn, ...
        boolean firstPiece = (depth % 4 == 0 || depth % 4 == 2);

        short score = Short.MIN_VALUE;
        // byte bestMove = (byte) lastMoves.get(lastMoves.size() - 1);
        byte d = --depth;
        for (byte child = 0; child < numbSuccessorsPrime(); child++) {
            // place first stone and maximize second turn as well
            UnionFindTile[] board = applySuccessor(position_array, child, firstPiece);
            short value;
            if (maximizeAI)
                value = NegaMax(board, d, alpha, beta);
            else
                value = (short) -NegaMax(board, d, (short) -beta, (short) -alpha);
            if (!firstPiece)
                storeMoveHistory(value);
            position_array = unmakeMove(position_array);
            if (value > score) {
                score = value;
                // bestMove = (byte) lastMoves.get(lastMoves.size() - 1);
            }
            if (score >= alpha)
                alpha = score;
            if (score >= beta) {
                pruning++;
                break;
            }
        }
        //        Enum flag = Constants.FLAG.EXACT;
        //        if (score <= olda)
        //            flag = Constants.FLAG.UPPERBOUND;
        //        else if (score >= beta)
        //            flag = Constants.FLAG.LOWERBOUND;
        //        saveTransposition(position_array, bestMove, score, flag, depth);

        return score;
    }
}
