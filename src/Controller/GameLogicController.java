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
                AIController ai = new AIController();
                GameState gs = new GameState(GameData.GAME_STATE);
                byte[] array = new byte[7];
                ai.AlphaBeta(array, GameData.GAME_STATE.NUMBER_OF_TILES_PLACED, Long.MIN_VALUE, Long.MAX_VALUE);
                Hexagon h = GameData.GAME_STATE.HEX_MAP_BY_ID.get(AIController.bestMove);
                BoardController.placeOnFreeTile(h, GameData.GAME_STATE, false);
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
            UnionFindTile uft = gs.UNION_FIND_MAP.get((byte) it.next());
            if (uft.getTileId() % 2 == 0)
                score[0] = score[0] * uft.getSize();
            else
                score[1] = score[1] * uft.getSize();
        }
        return score;
    }
}
