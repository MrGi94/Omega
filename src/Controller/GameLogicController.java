package Controller;

import AI.AIController;
import Controller.GUI.BoardController;
import Model.*;
import View.InfoBox;

import java.util.Iterator;

public class GameLogicController {

    private static boolean newRoundPossible() {
        return GameData.FREE_TILES_LEFT / 4 != 0;
    }

    public static void newTurn() {
        if (newRoundPossible()) {
            if (!GameData.HUMAN_PLAYER_TURN) {
                boolean maximizingPlayer = ((GameData.HUMAN_PLAYER_FIRST && !GameData.FIRST_PIECE) || (!GameData.HUMAN_PLAYER_FIRST && GameData.FIRST_PIECE));
                AIController ai = new AIController(GameData.CLUSTER_PARENT_ID_LIST, GameData.FREE_TILES_LEFT, GameData.FIRST_PIECE, maximizingPlayer);
                ai.AlphaBeta(GameData.UNION_FIND_TILE_ARRAY, 0, maximizingPlayer, Long.MIN_VALUE, Long.MAX_VALUE);
                Hexagon h;
                if (maximizingPlayer)
                    h = GameData.HEX_MAP_BY_ID.get(AIController.bestMove);
                else
                    h = GameData.HEX_MAP_BY_ID.get(AIController.worstMove);
                BoardController.placeOnFreeTile(h);
                newTurn();
            }
        } else {
            InfoBox.infoBox(generateScoreMessage(), Constants.INFO_BOX_GAME_END_TITLE);
        }
    }

    private static String generateScoreMessage() {
        int[] score = getScore();
        String message;
        if (score[0] > score[1]) {
            message = "White wins with " + score[0] + " points.\nWhereas black has " + score[1] + " points.";
        } else if (score[0] < score[1]) {
            message = "Black wins with " + score[1] + " points.\nWhereas white has " + score[0] + " points.";
        } else {
            message = "The match ended in a draw with both players scoring " + score[0] + " points.";
        }
        return message;
    }

    private static int[] getScore() {
        Iterator it = GameData.CLUSTER_PARENT_ID_LIST.iterator();
        int[] score = {1, 1};
        // score[0] white score | score[1] black score
        while (it.hasNext()) {
            UnionFindTile uft = GameData.UNION_FIND_MAP.get(it.next());
            if (uft.getColor() == 1)
                score[0] = score[0] * uft.getSize();
            else
                score[1] = score[1] * uft.getSize();
        }
        return score;
    }
}
