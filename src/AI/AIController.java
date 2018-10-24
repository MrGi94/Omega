package AI;

import Controller.GUI.BoardController;
import Controller.MapController;
import Model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class AIController {

    public static Hexagon lastMove;

    public static void playRandomMove() {
        while (!GameData.HUMAN_PLAYER_TURN) {
            Byte b = BoardController.determineNextMoveColor();
            Hexagon h = new Hexagon();
            for (Map.Entry<Hexagon, UnionFindTile> entry : MapController.getHexMapEntrySet()) {
                if (entry.getValue().getColor() == 0) {
                    h = entry.getKey();
                    break;
                }
            }
            BoardController.placeOnFreeTile(h, GameData.GAME_STATE, false);
        }
    }

    public static boolean isTerminalNode(GameState gs) {
        return gs.FREE_TILES_LEFT - 1 == 0;
    }

    public static short numbSuccessors(GameState gs) {
        return gs.FREE_TILES_LEFT;
    }

    public static GameState successor(GameState gs, short child) {
        ArrayList<Hexagon> moves_list = getFreeTiles(gs);
        lastMove = moves_list.get(child);
        BoardController.placeOnFreeTile(lastMove, gs, true);
        return gs;
    }

    private static ArrayList<Hexagon> getFreeTiles(GameState gs) {
        ArrayList<Hexagon> free_tiles = new ArrayList<>();
        for (Map.Entry<Hexagon, UnionFindTile> entry : gs.HEX_MAP.entrySet()) {
            if (entry.getValue().getColor() == 0) {
                free_tiles.add(entry.getKey());
            }
        }
        return free_tiles;
    }

    /* returns the score in respect to the AI's color */
    public static short evaluate(GameState gs) {
        Iterator it = gs.CLUSTER_PARENT_ID_LIST.iterator();
        short[] score = {1, 1};
        // score[0] white score | score[1] black score
        while (it.hasNext()) {
            UnionFindTile uft = gs.UNION_FIND_MAP.get((byte) it.next());
            if (uft.getTileId() % 2 == 0)
                score[0] = (short) (score[0] * uft.getSize());
            else
                score[1] = (short) (score[1] * uft.getSize());
        }
        if (GameData.HUMAN_PLAYER_FIRST) {
            return score[1];
        } else {
            return score[2];
        }
    }

    public static long AlphaBeta(GameState gs, int depth, long alpha, long beta) {
        if (isTerminalNode(gs) || depth == 0)
            return evaluate(gs);
        long score = Long.MIN_VALUE;
        for (short child = 1; child <= numbSuccessors(gs); child++) {
            long value = -AlphaBeta(successor(gs, child), depth - 1, -beta, -alpha);
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
