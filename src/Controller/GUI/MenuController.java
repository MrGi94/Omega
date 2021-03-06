package Controller.GUI;

import Controller.GUI.BoardController;
import Controller.GameLogicController;
import Controller.SerializeBoard;
import Model.Constants;
import Model.GameData;
import View.InfoBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Constants.MENU_ITEM_SECOND:
                GameData.HUMAN_PLAYER_FIRST = false;
                GameData.HUMAN_PLAYER_TURN = false;
                break;
            case Constants.MENU_ITEM_FIRST:
                GameData.HUMAN_PLAYER_FIRST = true;
                GameData.HUMAN_PLAYER_TURN = true;
                break;
            case Constants.MENU_ITEM_EXIT:
                System.exit(0);
            case Constants.MENU_ITEM_NEW_GAME:
//                GameData.BOARD_SIZE = 5;
//                BoardController.generateBoard(true);
//                GameLogicController.newTurn();
                try {
                    int game_size = Integer.parseInt(JOptionPane.showInputDialog(Constants.INPUT_BOARD_SIZE));
                    if (game_size >= 2 && game_size <= 10) {
                        GameData.BOARD_SIZE = game_size;
                        BoardController.generateBoard(true);
                        GameLogicController.newTurn();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case Constants.MENU_ITEM_OPEN_SAVED_GAME:
                Object o = SerializeBoard.deserializeBoard();
                if (o != null) {
                    InfoBox.infoBox(Constants.INFO_BOX_DESERIALIZE_MESSAGE_SUCCESS,
                            Constants.INFO_BOX_DESERIALIZE_TITLE_SUCCESS);
                    GameData.setGameData((GameData) o);
                    BoardController.generateBoard(false);
                }
                break;
            case Constants.MENU_ITEM_SAVE_GAME_AS:
                GameData gd = new GameData();
                if (SerializeBoard.serializeBoard(gd))
                    InfoBox.infoBox(Constants.INFO_BOX_SERIALIZE_MESSAGE_SUCCESS,
                            Constants.INFO_BOX_SERIALIZE_TITLE_SUCCESS);
                break;
            case Constants.MENU_ITEM_BACK:
                //BoardController.revertLastMovement();
                break;
        }
    }
}
