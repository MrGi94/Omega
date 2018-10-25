package AI;

import Controller.MapController;
import Model.GameData;
import Model.Hexagon;
import Model.UnionFindTile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class AIController {

    public static Vector lastMoves = new Vector<>();
    public static short bestScore = Short.MIN_VALUE;
    public static byte bestMove;
    private boolean firstPiece = true;

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

    private boolean isTerminalNode(byte[] position_array) {
        return getFreeTiles(position_array).size() -1 == 0;
    }

    private short numbSuccessors(byte[] position_array) {
        return (short) getFreeTiles(position_array).size();
    }

    private byte[] successor(byte[] position_array, short child) {
        byte[] p = copyArray(position_array);
        Vector moves_list = getFreeTiles(p);
        byte move = (byte) moves_list.get(child);
        if ((GameData.HUMAN_PLAYER_FIRST && firstPiece) || !GameData.HUMAN_PLAYER_FIRST && !firstPiece)
            p[move] = 2;
        else
            p[move] = 1;
//        gs.CLUSTER_PARENT_ID_LIST.add(move.getTileId());
//        unionTile();
        lastMoves.add(move);
        firstPiece = !firstPiece;
        return p;
    }

    private byte[] copyArray(byte[] old_board){
        byte[] p = new byte[old_board.length];
        for(byte i = 0; i < p.length; i++){
            p[i] = old_board[i];
        }
        return p;
    }

//    private void unionTile(byte[] position_array, UnionFindTile move) {
//        Hexagon h = gs.HEX_MAP_BY_ID.get(move.getTileId());
//        ArrayList<Hexagon> valid_neighbors = MapController.getValidNeighbors(h);
//        for (Hexagon entry : valid_neighbors) {
//            UnionFindTile uft = gs.HEX_MAP.get(entry);
//            uft = gs.UNION_FIND_MAP.get(uft.getTileId());
//            if (uft.getColor() == move.getColor()) {
//                MapController.union(move.getTileId(), uft.getTileId(), gs);
//            }
//        }
//        MapController.setUnionFindTile(move, gs);
//        return move;
//    }

    private Vector getFreeTiles(byte[] position_array) {
        Vector free_tiles = new Vector();
        for(byte i = 0; i < position_array.length; i++){
            if(position_array[i] == 0)
                free_tiles.add(i);
        }
        return free_tiles;
    }

    /* returns the score in respect to the AI's color */
    private short evaluate(byte[] position_array) {
//        Iterator it = gs.CLUSTER_PARENT_ID_LIST.iterator();
//        short[] score = {1, 1};
//        // score[0] white score | score[1] black score
//        while (it.hasNext()) {
//            UnionFindTile uft = gs.UNION_FIND_MAP.get(it.next());
//            if (uft.getTileId() % 2 == 0)
//                score[0] = (short) (score[0] * uft.getSize());
//            else
//                score[1] = (short) (score[1] * uft.getSize());
//        }
//        if (!GameData.HUMAN_PLAYER_FIRST && GameData.FIRST_PIECE) {
//            return score[0];
//        } else {
//            return score[1];
//        }
        return (short) Math.random();
    }

    public long AlphaBeta(byte[] position_array, int depth, long alpha, long beta) {
        if (isTerminalNode(position_array) || depth == 0) {
            short s = evaluate(position_array);
            if(s >= bestScore)
                bestMove = (byte) lastMoves.firstElement();
            lastMoves = new Vector<>();
            return s;
        }

        long score = Long.MIN_VALUE;
        for (short child = 0; child < numbSuccessors(position_array); child++) {
            long value = -AlphaBeta(successor(position_array, child), depth - 1, -beta, -alpha);
            if (value > score)
                score = value;
            if (value > alpha)
                alpha = score;
            if (score >= beta)
                break;
        }
        return score;
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
