package Controller;

import AI.AIController;
import Controller.GUI.BoardController;
import Model.*;
import View.InfoBox;

import java.util.Iterator;

public class GameLogicController {

    private static boolean newRoundPossible() {
        return GameData.GAME_STATE.FREE_TILES_LEFT / 4 != 0;
    }

    public static void newTurn() {
        if (newRoundPossible()) {
            if (!GameData.HUMAN_PLAYER_TURN) {
                AIController ai = new AIController(GameData.GAME_STATE.CLUSTER_PARENT_ID_LIST, GameData.GAME_STATE.FREE_TILES_LEFT, GameData.FIRST_PIECE);
                ai.AlphaBeta(GameData.GAME_STATE.test_array, 15, Long.MIN_VALUE, Long.MAX_VALUE);
                Hexagon h = GameData.GAME_STATE.HEX_MAP_BY_ID.get(AIController.bestMove);
                BoardController.placeOnFreeTile(h);
                newTurn();
            }
        } else {
            InfoBox.infoBox(generateScoreMessage(), Constants.INFO_BOX_GAME_END_TITLE);
        }
    }

    public static String generateScoreMessage() {
        int[] score = getScore(GameData.GAME_STATE);
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

    public static int[] getScore(GameState gs) {
        Iterator it = gs.CLUSTER_PARENT_ID_LIST.iterator();
        int[] score = {1, 1};
        // score[0] white score | score[1] black score
        while (it.hasNext()) {
            UnionFindTile uft = gs.UNION_FIND_MAP.get(it.next());
            if (uft.getColor() == 1)
                score[0] = score[0] * uft.getSize();
            else
                score[1] = score[1] * uft.getSize();
        }
        return score;
    }
}
