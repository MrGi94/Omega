package Controller;

import AI.AIController;
import Model.Constants;
import Model.GameData;
import Model.UnionFindTile;
import View.InfoBox;

import java.util.Iterator;

import static java.lang.System.*;

public class GameLogicController {

    private static boolean newRoundPossible() {
        return GameData.FREE_TILES_LEFT / 4 != 0;
    }

    public static void newTurn() {
        if (newRoundPossible()) {
            if (!GameData.HUMAN_PLAYER_TURN) {
                AIController.playRandomMove();
                newTurn();
            }
        } else {
            InfoBox.infoBox(generateScoreMessage(), Constants.INFO_BOX_GAME_END_TITLE);
        }
    }

    public static String generateScoreMessage() {
        int[] score = getScore();
        String message = "";
        if (score[0] > score[1]) {
            message = "White wins with " + score[0] + " points.\nWhereas black has " + score[1] + " points.";
        } else if (score[0] < score[1]) {
            message = "Black wins with " + score[1] + " points.\nWhereas white has " + score[0] + " points.";
        } else {
            message = "The match ended in a draw with both players scoring " + score[0] + " points.";
        }
        return message;
    }

    public static int[] getScore() {
        Iterator it = GameData.CLUSTER_PARENT_ID_LIST.iterator();
        int[] score = {1, 1};
        // score[0] white score | score[1] black score
        while (it.hasNext()) {
            UnionFindTile uft = MapController.getUnionFindTile((byte) it.next());
            if (uft.getTileId() % 2 == 0)
                score[0] = score[0] * uft.getSize();
            else
                score[1] = score[1] * uft.getSize();
        }
        return score;
    }
}
