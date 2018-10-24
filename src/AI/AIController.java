package AI;

import Controller.GUI.BoardController;
import Model.GameData;
import Controller.MapController;
import Model.Hexagon;
import Model.UnionFindTile;

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
