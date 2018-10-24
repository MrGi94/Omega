package Model;

import java.io.File;
import java.util.ArrayList;

public class Constants {

    public static final String MENU_FILE = "File";
    public static final String MENU_PLAY = "Play";
    public static final String MENU_ITEM_OPEN_SAVED_GAME = "Open Saved Game...";
    public static final String MENU_ITEM_SAVE_GAME_AS = "Save Game As...";
    public static final String MENU_ITEM_EXIT = "Exit";
    public static final String MENU_ITEM_BACK = "Back";
    public static final String MENU_ITEM_NEW_GAME = "New Game";
    public static final String MENU_ITEM_CHOOSE_SIDE = "Choose Side...";
    public static final String MENU_ITEM_FIRST = "First";
    public static final String MENU_ITEM_SECOND = "Second";
    public static final String APP_TITLE = "Omega";
    public static final String INPUT_BOARD_SIZE = "Enter the board size:";
    public static final String INFO_BOX_SERIALIZE_MESSAGE_SUCCESS = "Current game state saved successfully";
    public static final String INFO_BOX_SERIALIZE_TITLE_SUCCESS = "Saved game...";
    public static final String INFO_BOX_DESERIALIZE_MESSAGE_SUCCESS = "Last game state loaded successfully";
    public static final String INFO_BOX_DESERIALIZE_TITLE_SUCCESS = "Loaded game...";
    public static final String INFO_BOX_GAME_END_TITLE = "Game has finished";
    public static final String SERIALIZE_PATH = new File("").getAbsolutePath() + "/game_states/omega_game.ser";

    public static final Orientation BOARD_ORIENTATION = new Orientation(Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0, 3.0 / 2.0, Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0, 0.5);
    public static final Point TILE_SIZE = new Point(25, 25);
    public static final Point BOARD_ORIGIN = new Point(430, 390);
    public static final ArrayList<Hexagon> HEXAGON_DIRECTIONS = new ArrayList<Hexagon>() {{
        add(new Hexagon(1, 0, -1));
        add(new Hexagon(1, -1, 0));
        add(new Hexagon(0, -1, 1));
        add(new Hexagon(-1, 0, 1));
        add(new Hexagon(-1, 1, 0));
        add(new Hexagon(0, 1, -1));
    }};

    public enum FLAG {EXACT, LOWERBOUND, UPPERBOUND}
}
