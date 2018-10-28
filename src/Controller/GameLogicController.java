package Controller;

import AI.AIController;
import Controller.GUI.BoardController;
import Model.*;
import View.InfoBox;

import java.util.Iterator;

/*
 * checks if new turn can be started, publishes the score and handles the AI controller
 * */
public class GameLogicController {

    // assuming it is a two player game where each player places two stones
    private static boolean newTurnPossible() {
        return (GameData.HEX_MAP.size() - GameData.HEX_MAP.size() / 4 * 4) != GameData.FREE_TILES_LEFT;
    }

    // starts a game between random AI and negamax AI
    public static void testAIPlayer() {
        while (newTurnPossible()) {
            if (!GameData.HUMAN_PLAYER_TURN) {
                // negamax AI
                AIController ai = new AIController(GameData.FREE_TILES_LEFT);
                byte[] move_array = ai.OmegaPrime(3);
                Hexagon h = GameData.HEX_MAP_BY_ID.get(move_array[0]);
                BoardController.placeOnFreeTile(h);
                h = GameData.HEX_MAP_BY_ID.get(move_array[1]);
                BoardController.placeOnFreeTile(h);
            } else {
                // random AI
                AIController ai = new AIController(GameData.FREE_TILES_LEFT);
                byte move = ai.playRandomMove();
                Hexagon h = GameData.HEX_MAP_BY_ID.get(move);
                BoardController.placeOnFreeTile(h);
                move = ai.playRandomMove();
                h = GameData.HEX_MAP_BY_ID.get(move);
                BoardController.placeOnFreeTile(h);
            }
        }
        InfoBox.infoBox(generateScoreMessage(), Constants.INFO_BOX_GAME_END_TITLE);
    }

    // for playing human vs negamax AI
    public static void newTurn() {
        if (!newTurnPossible())
            InfoBox.infoBox(generateScoreMessage(), Constants.INFO_BOX_GAME_END_TITLE);
        else if (!GameData.HUMAN_PLAYER_TURN) {
            AIController ai = new AIController(GameData.FREE_TILES_LEFT);
            byte[] move_array = ai.OmegaPrime(5);
            Hexagon h = GameData.HEX_MAP_BY_ID.get(move_array[0]);
            BoardController.placeOnFreeTile(h);
            h = GameData.HEX_MAP_BY_ID.get(move_array[1]);
            BoardController.placeOnFreeTile(h);
            newTurn();
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
            UnionFindTile uft = GameData.UNION_FIND_MAP_BY_PLACEMENT.get(it.next());
            if (uft.getColor() == 1)
                score[0] = score[0] * uft.getSize();
            else
                score[1] = score[1] * uft.getSize();
        }
        return score;
    }
}
