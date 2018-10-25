package AI;

import Controller.MapController;
import Model.GameData;
import Model.Hexagon;
import Model.UnionFindTile;

import java.util.*;

public class AIController {

    private Deque lastMoves = new ArrayDeque();
    private short bestScore = Short.MIN_VALUE;
    private short lowestScore = Short.MAX_VALUE;
    public static byte bestMove;
    public static byte worstMove;
    private boolean firstPiece;
    private LinkedHashSet<Byte> cluster_parent_id;
    private byte free_tiles_left;
    private boolean isAIMaximizing;
    private int evaluations = 1;
    private int eval_childs = 1;


//    public static void playRandomMove() {
//        while (!GameData.HUMAN_PLAYER_TURN) {
//            Byte b = BoardController.determineNextMoveColor();
//            Hexagon h = new Hexagon();
//            for (Map.Entry<Hexagon, UnionFindTile> entry : MapController.getHexMapEntrySet()) {
//                if (entry.getValue().getColor() == 0) {
//                    h = entry.getKey();
//                    break;
//                }
//            }
//            BoardController.placeOnFreeTile(h, GameData.GAME_STATE, false);
//        }
//    }

    public AIController(LinkedHashSet<Byte> cluster_parent_id, byte free_tiles_left, boolean firstPiece, boolean isAIMaximizing) {
        this.cluster_parent_id = copyClusterParent(cluster_parent_id);
        this.free_tiles_left = free_tiles_left;
        this.isAIMaximizing = isAIMaximizing;
        if (firstPiece)
            this.firstPiece = true;
        else
            this.firstPiece = false;
    }

    private LinkedHashSet<Byte> copyClusterParent(LinkedHashSet<Byte> cluster_parent_id) {
        LinkedHashSet<Byte> set = new LinkedHashSet();
        Iterator it = cluster_parent_id.iterator();
        while (it.hasNext()) {
            set.add((byte) it.next());
        }
        return set;
    }

    private boolean isTerminalNode() {
        return free_tiles_left / 4 == 0;
    }

    private short numbSuccessors() {
        return free_tiles_left;
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
        Hexagon h = GameData.GAME_STATE.HEX_MAP_BY_ID.get(position_array[move].getTileId());
        ArrayList<Hexagon> valid_neighbors = MapController.getValidNeighbors(h);
        for (Hexagon entry : valid_neighbors) {
            byte tile_id = GameData.GAME_STATE.HEX_MAP.get(entry).getTileId();
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
    private short evaluate(UnionFindTile[] position_array, boolean isMaximizingPlayer) {
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
        if (isMaximizingPlayer) {
            return score[0]; //(short) (Math.log(score[0]) * 100);
        } else {
            return score[1];//(short) (Math.log(score[1]) * 100);
        }
    }

    public long AlphaBeta(UnionFindTile[] position_array, int depth, boolean isMaximizingPlayer, long alpha, long beta) {
        if (isTerminalNode()) {
            short s = evaluate(position_array, isMaximizingPlayer);
            if (isAIMaximizing) {
                if (s > bestScore) {
                    bestMove = (byte) lastMoves.peekFirst();
                    bestScore = s;
                }
            } else {
                if (s < lowestScore) {
                    worstMove = (byte) lastMoves.peekFirst();
                    lowestScore = s;
                }
            }
            firstPiece = !firstPiece;
            cluster_parent_id.remove(lastMoves.pollLast());
            free_tiles_left++;
            System.out.println(evaluations);
            evaluations++;
            return s;
        }

        if (isMaximizingPlayer) {
            long bestVal = Long.MIN_VALUE;
            for (short child = 0; child < numbSuccessors(); child++) {
                System.out.println(child);
                long value = AlphaBeta(successor(position_array, child), depth + 1, false, alpha, beta);
                bestVal = Math.max(bestVal, value);
                alpha = Math.max(alpha, bestVal);
                if (beta <= alpha)
                    break;
            }
            return bestVal;
        } else {
            long bestVal = Long.MAX_VALUE;
            for (short child = 0; child < numbSuccessors(); child++) {
                System.out.println(child);
                long value = AlphaBeta(successor(position_array, child), depth + 1, true, alpha, beta);
                bestVal = Math.min(bestVal, value);
                beta = Math.min(alpha, bestVal);
                if (beta <= alpha)
                    break;
            }
            return bestVal;
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
