package Controller;

import Model.Constants;
import Model.GameData;
import View.InfoBox;

public class GameLogicController {

    public static boolean newRoundPossible() {
        if ((GameData.HUMAN_PLAYER_FIRST && GameData.HUMAN_PLAYER_TURN) || (!GameData.HUMAN_PLAYER_FIRST && !GameData.HUMAN_PLAYER_TURN)) {
            if (GameData.FREE_TILES_LEFT % 4 != GameData.FREE_TILES_LEFT)
                return true;
            else {
                InfoBox.infoBox(Constants.INFO_BOX_GAME_END_TITLE, generateScoreMessage());
            }
        }
        return false;
    }

    public static String generateScoreMessage(){
        return "";
    }
}
