package AI;

import Controller.GUI.BoardController;
import Controller.GameLogicController;
import Model.*;
import Controller.MapController;
import Model.TranspositionTable.TTEntry;

import java.util.HashMap;
import java.util.Map;

public class AIController {

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
            BoardController.placeOnFreeTile(h, b);
        }
    }

//    public int AlphaBeta(state s, int depth, int alpha, int beta){
//        if (terminal node|| depth == 0) return (Evaluate(s));
//        score = -inf;
//        for(child = 1; child <= numbSuccessors(s); child++){
//            value = -AlphaBeta(successor(s, child), depth-1, -beta, -alpha);
//            if(value > score) score = value;
//            if(score > alpha) alpha = score;
//            if(score >= beta) break;
//        }
//        return(score);
//    }

    /* do the TT lockup and return the respective entry */
    public TTEntry retrieve(HashMap<Hexagon, UnionFindTile> hex_map) {
        return new TTEntry();
    }

    private void store(HashMap<Hexagon, UnionFindTile> hex_map, short bestMove, short bestValue, Constants.FLAG flag, byte depth) {
        GameData.TRANSPOSITION_TABLE.put(zobristKey, new TTEntry(flag, depth, bestValue, bestMove));
    }

    private short evaluate(HashMap<Hexagon, UnionFindTile> hex_map) {
        short score = GameLogicController.getAIScore();
        score = (short) (Math.log(score) * 100);
        return score;
    }

    public short AlphaBetaWithTT(HashMap<Hexagon, UnionFindTile> hex_map, byte depth, short alpha, short beta) {
        int olda = alpha; /* save original alpha value */
        TTEntry n = retrieve(hex_map); /* Transposition-table lookup */
        if (n.getDepth() >= depth) {
            if (n.getFlag() == Constants.FLAG.EXACT) {
                return n.getValue();
            } else if (n.getFlag() == Constants.FLAG.LOWERBOUND) {
                alpha = (short) Math.max(alpha, n.getValue());
            } else if (n.getFlag() == Constants.FLAG.UPPERBOUND) {
                beta = (short) Math.min(beta, n.getValue());
            }
            if (alpha >= beta) {
                return n.getValue();
            }
        }
        /* if position is not found, depth will be -1 */
        if (depth == 0 || terminalnode){
            g = evaluate(hex_map); /* leaf node */
        }

        /* this part is just plain alpha-beta */
        short bestValue = Short.MIN_VALUE;
        short bestMove = 0;
        for (short child = 1; child <= NumSuccessors(hex_map); child++) {
            short result = -AlphaBetaWithTT(Succssor(child), -beta, -alpha, depth - 1);
            if (result > bestValue) {
                bestValue = result;
                bestMove = child;
                if (bestValue >= alpha)
                    alpha = bestValue;
                if (bestValue >= beta)
                    break;
                /* traditional transposition table storing of bounds */
            }
            Constants.FLAG flag = Constants.FLAG.EXACT;
            if (bestValue <= olda) {
                /* fail-low result implies an upper bound */
                flag = Constants.FLAG.UPPERBOUND;
            } else if (bestValue >= beta) {
                /* fail-high result implies an lower bound */
                flag = Constants.FLAG.LOWERBOUND;
            }
            /* this part stores information in the transposition table */
            store(hex_map, bestMove, bestValue, flag, depth);
            /* not sure about this placement here (could be outside for loop) */
            return bestValue;
        }
    }

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
