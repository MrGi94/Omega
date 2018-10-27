package AI;

import Controller.GUI.BoardController;
import Controller.MapController;
import Model.GameData;
import Model.Hexagon;
import Model.TranspositionTable.TTEntry;
import Model.UnionFindTile;

import java.util.*;

public class AIController {
    private ArrayList lastMoves;
    private boolean firstPiece;
    private boolean maximizeAI;
    private byte[] bestMoves;
    private LinkedHashSet<Byte> cluster_parent_id;
    private short free_tiles_left;
    private long starting_time;
    private List<TTEntry> history;
    private int counterBlack;
    private int counterWhite;
    private int counterBlackBad;
    private int counterWhiteBad;
    private int breakBlack;
    private int breakWhite;
    // divide total thinking time
    private long processing_time_per_turn;


    public static void playRandomMove() {
        while (GameData.HUMAN_PLAYER_TURN) {
            Hexagon h = new Hexagon(0, 0, 0);
            for (Map.Entry<Hexagon, UnionFindTile> entry : MapController.getHexMapEntrySet()) {
                if (entry.getValue().getColor() == 0) {
                    h = entry.getKey();
                    break;
                }
            }
            BoardController.placeOnFreeTile(h);
        }
    }

    public AIController(LinkedHashSet<Byte> cluster_parent_id, short free_tiles_left) {
        this.cluster_parent_id = copyClusterParent(cluster_parent_id);
        this.free_tiles_left = free_tiles_left;
        this.firstPiece = true;
        this.maximizeAI = true;
        this.bestMoves = new byte[2];
        this.processing_time_per_turn = GameData.PROCESSING_TIME / ((GameData.HEX_MAP.size()) / 4);
        this.starting_time = System.currentTimeMillis();
        this.lastMoves = new ArrayList();
        // to match the depth index
        this.lastMoves.add(0);
        this.history = new ArrayList<>();
    }

    private LinkedHashSet<Byte> copyClusterParent(LinkedHashSet<Byte> cluster_parent_id) {
        LinkedHashSet<Byte> set = new LinkedHashSet();
        for (Object aCluster_parent_id : cluster_parent_id) {
            set.add((byte) aCluster_parent_id);
        }
        return set;
    }

    private boolean isTerminalNode() {
        return (GameData.HEX_MAP.size() - GameData.HEX_MAP.size() / 4 * 4) == free_tiles_left;
    }

    private int numbSuccessors(UnionFindTile[] board) {
        return getFreeTiles(board).size();
    }

    private UnionFindTile[] successor(UnionFindTile[] position_array, short child) {
        UnionFindTile[] board = copyBoard(position_array);
        Vector moves_list = getFreeTiles(board);
        byte move = (byte) moves_list.get(child);
        if (!((GameData.HUMAN_PLAYER_FIRST && firstPiece) || !GameData.HUMAN_PLAYER_FIRST && !firstPiece))
            board[move].setColor((byte) 2);
        else
            board[move].setColor((byte) 1);
        lastMoves.add(move);
        cluster_parent_id.add(position_array[move].getTileId());
        free_tiles_left--;
        board = unionTile(board, move);
        firstPiece = !firstPiece;
        return board;
    }

    private UnionFindTile[] copyBoard(UnionFindTile[] old_board) {
        UnionFindTile[] p = new UnionFindTile[old_board.length];
        for (byte i = 0; i < p.length; i++) {
            p[i] = new UnionFindTile(old_board[i]);
        }
        return p;
    }

    private UnionFindTile[] unionTile(UnionFindTile[] position_array, byte move) {
        Hexagon h = GameData.HEX_MAP_BY_ID.get(position_array[move].getTileId());
        ArrayList<Hexagon> valid_neighbors = MapController.getValidNeighbors(h);
        for (Hexagon entry : valid_neighbors) {
            byte tile_id = GameData.HEX_MAP.get(entry).getTileId();
            if (position_array[tile_id].getColor() == position_array[move].getColor()) {
                position_array = union(move, tile_id, position_array);
            }
        }
        return position_array;
    }

    private UnionFindTile[] union(byte tile_id1, byte tile_id2, UnionFindTile[] position_array) {
        UnionFindTile root1 = find(tile_id1, position_array);
        UnionFindTile root2 = find(tile_id2, position_array);
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
        cluster_parent_id.remove(smallParent.getTileId());
        return position_array;
    }

    private UnionFindTile find(byte tile_id, UnionFindTile[] position_array) {
        UnionFindTile uft = position_array[tile_id];
        do {
            uft = position_array[uft.getParent()];
        } while (uft.getTileId() != uft.getParent());
        return uft;
    }

    private Vector getFreeTiles(UnionFindTile[] position_array) {
        Vector free_tiles = new Vector();
        for (byte i = 0; i < position_array.length; i++) {
            if (position_array[i].getColor() == 0)
                free_tiles.add(i);
        }
        return free_tiles;
    }

    /* returns the score in respect to the AI's color */
    private short evaluate(UnionFindTile[] position_array) {
        Iterator it = cluster_parent_id.iterator();
        short[] score = {1, 1};
        // score[0] white score | score[1] black score
        while (it.hasNext()) {
            byte tile_id = (byte) it.next();
            if (position_array[tile_id].getColor() == 1)
                score[0] = (short) (score[0] * position_array[tile_id].getSize());
            else
                score[1] = (short) (score[1] * position_array[tile_id].getSize());
        }
        // if human is first we play black and therefore want to keep positive difference
        if (GameData.HUMAN_PLAYER_FIRST)
            return (short) (score[1] - score[0]);
        else
            return (short) (score[0] - score[1]);
    }


    public long AlphaBeta(UnionFindTile[] position_array, int depth, byte maximizingPlayer, long alpha, long beta) {
        // https:github.com/avianey/minimax4j/blob/master/minimax4j/src/main/java/fr/avianey/minimax4j/impl/AlphaBeta.java
        if (isTerminalNode() || depth == 0) {
//            cluster_parent_id.remove(lastMoves.pollLast());
            return maximizingPlayer * evaluate(position_array);
//            if (isAIMaximizing) {
//                if (s > bestScore) {
//                    bestMove = (byte) lastMoves.peekFirst();
//                    bestScore = s;
//                }
//            } else {
//                if (s < lowestScore) {
//                    worstMove = (byte) lastMoves.peekFirst();
//                    lowestScore = s;
//                }
//            }
//            firstPiece = !firstPiece;
//            return s;
        }
        long score;
        if (firstPiece) {
            maximizingPlayer = (byte) -maximizingPlayer;
            firstPiece = !firstPiece;
        }
        if (maximizingPlayer > 0) {
            for (short child = 0; child < numbSuccessors(position_array); child++) {
                UnionFindTile[] board = successor(position_array, child);
                score = AlphaBeta(board, depth - 1, (byte) -maximizingPlayer, alpha, beta);
                if (score > alpha) {
                    alpha = score;
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            return alpha;
        } else {
            for (short child = 0; child < numbSuccessors(position_array); child++) {
                UnionFindTile[] board = successor(position_array, child);
                score = AlphaBeta(board, depth - 1, maximizingPlayer, alpha, beta);
                if (score < beta) {
                    beta = score;
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            return beta;
        }
    }


    private long numbSuccessorsPrime() {
        return free_tiles_left;
        //return free_tiles_left * (free_tiles_left - 1);
    }

    private short evaluatePrime(UnionFindTile[] position_array) {
        Iterator it = cluster_parent_id.iterator();
        short[] score = {1, 1};
        // score[0] white score | score[1] black score
        while (it.hasNext()) {
            byte tile_id = (byte) it.next();
            if (position_array[tile_id].getColor() == 1)
                score[0] = (short) (score[0] * position_array[tile_id].getSize());
            else
                score[1] = (short) (score[1] * position_array[tile_id].getSize());
        }
        if (GameData.HUMAN_PLAYER_FIRST)
            return (short) (score[1] - score[0]);
        else
            return (short) (score[0] - score[1]);
//        return (long) ((Math.log(score[0]) * 100) - (Math.log(score[1]) * 100));
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
            if (!GameData.HUMAN_PLAYER_FIRST) {
                byte tmp = move_array[0];
                move_array[0] = move_array[1];
                move_array[1] = tmp;
            }
        }
        return move_array;
    }

    private TTEntry sortHistoryReturnBestEntry() {
        if (!history.isEmpty()) {
            // sort move history, pick best score and continue from there
            Collections.sort(history);
            return history.get(0);
        }
        return new TTEntry(bestMoves);
    }

    public byte[] OmegaPrime(int depth_limit) {
        if (!GameData.OPEN_BOOK_CENTER.isEmpty() && (GameData.HEX_MAP.size() - GameData.FREE_TILES_LEFT) / 4 < 2) {
            return getOpenBookMove();
        }

        for (byte d = 2; d <= depth_limit * 2; d = (byte) (d + 2)) {
            try {
                if (!history.isEmpty()) {
                    TTEntry entry = sortHistoryReturnBestEntry();
                    free_tiles_left = (short) getFreeTiles(entry.getBoard()).size();
                    NegaMax(entry.getBoard(), entry.getDepth(), entry.getValue(), Long.MAX_VALUE);
                } else {
                    NegaMax(GameData.UNION_FIND_TILE_ARRAY, d, Long.MIN_VALUE, Long.MAX_VALUE);
                    System.out.println("History size is: " + history.size());
                    System.out.println("Black counter is: " + counterBlack);
                    System.out.println("White counter is: " + counterWhite);
                    System.out.println("Black bad is " + counterBlackBad + " White bad is " + counterWhiteBad);
                    System.out.println("Black breaking " + breakBlack + " White breaking " + breakWhite);
                }
            } catch (TimeUpException e) {
                return sortHistoryReturnBestEntry().getBestMove();
            }
        }
        return sortHistoryReturnBestEntry().getBestMove();
    }

    private UnionFindTile[] successorPrime(UnionFindTile[] position_array, short child) {
        UnionFindTile[] board = copyBoard(position_array);
        Vector moves_list = getFreeTiles(board);
        byte move = (byte) moves_list.get(child);
        if ((GameData.HUMAN_PLAYER_FIRST && firstPiece) || (!GameData.HUMAN_PLAYER_FIRST && !firstPiece))
            board[move].setColor((byte) 2);
        else
            board[move].setColor((byte) 1);
        lastMoves.add(move);
        cluster_parent_id.add(position_array[move].getTileId());
        free_tiles_left--;
        board = unionTile(board, move);
        firstPiece = !firstPiece;
        return board;
    }

    private void unmakeMove() {
        int size = lastMoves.size() - 1;
        byte tile_id = (byte) lastMoves.get(size);
        lastMoves.remove(size);
        cluster_parent_id.remove(tile_id);
        free_tiles_left++;
    }

    private void storeMove(UnionFindTile[] board, short bestValue) {
        byte index = (byte) (lastMoves.size() - 1);
        bestMoves[0] = (byte) lastMoves.get(--index);
        bestMoves[1] = (byte) lastMoves.get(index);
        history.add(new TTEntry(index, bestValue, bestMoves, board));
    }

    public short NegaMax(UnionFindTile[] position_array, byte depth, long alpha, long beta) throws TimeUpException {
        if (isTerminalNode() || depth == 0) {
            long i = System.currentTimeMillis() - starting_time;
//            if ((System.currentTimeMillis() - starting_time) > processing_time_per_turn)
//                throw new TimeUpException();
            short s = evaluatePrime(position_array);
            return s;
        }

        short score = Short.MIN_VALUE;
        byte d = --depth;
        UnionFindTile[] board;
        if (firstPiece && maximizeAI) {
            for (short child = 0; child < numbSuccessorsPrime(); child++) {
                if (counterBlack == 0)
                    System.out.println("First Piece Maximizing: numbSuccessors = " + numbSuccessorsPrime());
                counterBlack++;
                // place first stone and maximize second turn as well
                board = successorPrime(position_array, child);
                short value = NegaMax(board, d, alpha, beta);
                unmakeMove();
                if (value > score)
                    score = value;
                if (score > alpha)
                    alpha = score;
                if (score > beta) {
                    breakBlack++;
                    break;
                }
            }
            return score;
        } else if (!firstPiece && maximizeAI) {
            // place second stone and minimize first opponents turn
            maximizeAI = false;
            for (short child = 0; child < numbSuccessorsPrime(); child++) {
                if (counterWhite == 0)
                    System.out.println("Second Piece Maximizing: numbSuccessors = " + numbSuccessorsPrime());
                counterWhite++;
                board = successorPrime(position_array, child);
                short value = NegaMax(board, d, alpha, beta);
                storeMove(board, value);
                unmakeMove();
                if (value > score)
                    score = value;
                if (score > alpha)
                    alpha = score;
                if (score > beta) {
                    breakWhite++;
                    break;
                }
            }
            return score;
        } else if (firstPiece && !maximizeAI) {
            // place opponents first stone and minimize second turn as well
            for (short child = 0; child < numbSuccessorsPrime(); child++) {
                counterBlackBad++;
                board = successorPrime(position_array, child);
                short value = (short) -NegaMax(board, d, -beta, -alpha);
                unmakeMove();
                if (value > score)
                    score = value;
                if (score > alpha)
                    alpha = score;
                if (score > beta)
                    break;
            }
            return score;
        } else {
            // place opponents second stone and maximize first AI turn
            maximizeAI = true;
            for (short child = 0; child < numbSuccessorsPrime(); child++) {
                counterWhiteBad++;
                board = successorPrime(position_array, child);
                short value = (short) -NegaMax(board, d, -beta, -alpha);
                unmakeMove();
                if (value > score)
                    score = value;
                if (score > alpha)
                    alpha = score;
                if (score > beta)
                    break;
            }
            return score;
        }
    }


    /* do the TT lockup and return the respective entry */
//    public TTEntry retrieve() {
//        return GameData.TRANSPOSITION_TABLE.get(zobristKey);
//    }
//
//    private void store(short bestMove, short bestValue, Constants.FLAG flag, byte depth) {
//        GameData.TRANSPOSITION_TABLE.put(zobristKey, new TTEntry(flag, depth, bestValue, bestMove));
//    }
//
//    private short evaluate() {
//        short score = GameLogicController.getAIScore();
//        score = (short) (Math.log(score) * 100);
//        return score;
//    }

//    public short AlphaBetaWithTT(short alpha, short beta, byte depth) {
//        int olda = alpha; /* save original alpha value */
//        TTEntry n = retrieve(); /* Transposition-table lookup */
//        if (n.getDepth() >= depth) {
//            if (n.getFlag() == Constants.FLAG.EXACT) {
//                return n.getValue();
//            } else if (n.getFlag() == Constants.FLAG.LOWERBOUND) {
//                alpha = (short) Math.max(alpha, n.getValue());
//            } else if (n.getFlag() == Constants.FLAG.UPPERBOUND) {
//                beta = (short) Math.min(beta, n.getValue());
//            }
//            if (alpha >= beta) {
//                return n.getValue();
//            }
//        }
//        /* if position is not found, depth will be -1 */
//        if (depth == 0 || GameData.FREE_TILES_LEFT == 1) {
//            return (evaluate()); /* leaf node */
//        }
//
//        /* this part is just plain alpha-beta */
//        short bestValue = Short.MIN_VALUE;
//        short bestMove = 0;
//        ArrayList<Hexagon> free_tiles = MapController.getFreeTiles();
//        for (short child = 1; child <= GameData.FREE_TILES_LEFT - 1; child++) {
//            Hexagon h = free_tiles.get(child);
//            UnionFindTile utf = GameData.HEX_MAP.get(h);
//            utf.setColor(BoardController.determineNextMoveColor());
//            GameData.HEX_MAP.put(h, utf);
//            short result = (short) -AlphaBetaWithTT((short) -beta, (short) -alpha, (byte) (depth - 1));
//            if (result > bestValue) {
//                bestValue = result;
//                bestMove = child;
//                if (bestValue >= alpha)
//                    alpha = bestValue;
//                if (bestValue >= beta)
//                    break;
//                /* traditional transposition table storing of bounds */
//            }
//            Constants.FLAG flag = Constants.FLAG.EXACT;
//            if (bestValue <= olda) {
//                /* fail-low result implies an upper bound */
//                flag = Constants.FLAG.UPPERBOUND;
//            } else if (bestValue >= beta) {
//                /* fail-high result implies an lower bound */
//                flag = Constants.FLAG.LOWERBOUND;
//            }
//            /* this part stores information in the transposition table */
//            store(bestMove, bestValue, flag, depth);
//        }
//        /* not sure about this placement here (could be inside for loop) */
//        return bestValue;
//    }

//    I started with a version that gives each player a single placement of their color instead of the double placement move.
//    This version was quite interesting in that it would always attempt to form groups of 3 pieces on the board.
//    I didn't explicitly program that in, so in a sense, the program discovered that "3" is a discrete approximation to "e".
//    Very interesting. You might also find it interesting to know that for the evaluation,
//    I take the natural logarithm of the score (product of all groups) and scale that to a 3 digit integer after multiplying by 100.
//    That seemed to work pretty well. Also, my group update is incremental.
//    When a piece is placed, adjacent groups are detected, their size divided out from the total,
//    and the new larger group formed by adding the new piece is multiplied back into the score.
//    That way, a full board scan is not required.

}

class TimeUpException extends Exception {

    TimeUpException() {
    }
}
