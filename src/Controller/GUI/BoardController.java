package Controller.GUI;

import Controller.GameLogicController;
import Controller.MapController;
import Model.*;
import Model.Point;
import View.Menu;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

public class BoardController extends MouseAdapter {

    public static void drawHexTile(Hexagon h, Byte b) {
        ArrayList<Point> corners = Layout.polygonCorners(h);
        int[] x = new int[6];
        int[] y = new int[6];
        int i = 0;
        for (Point p : corners) {
            x[i] = (int) Math.round(p.x);
            y[i] = (int) Math.round(p.y);
            i = i + 1;
        }
        Menu.board.drawPolygon(Menu.board.getGraphics(), x, y, getColorByByte(b));
    }

    public static void generateBoard(boolean createNew) {
        if (createNew)
            MapController.generateHexMap(GameData.BOARD_SIZE);
        for (Map.Entry<Hexagon, UnionFindTile> entry : MapController.getHexMapEntrySet()) {
            drawHexTile(entry.getKey(), entry.getValue().getColor());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (GameData.HUMAN_PLAYER_TURN) {
            Hexagon h = Layout.pixelToHex(new Point(e.getX(), e.getY() - 31)).hexRound();
            placeOnFreeTile(h);
        } else {
            GameLogicController.newTurn();
        }
    }

    public static void placeOnFreeTile(Hexagon h) {
        Byte b = determineNextMoveColor();
        Byte val = MapController.getHexMapColor(h);
        if (val != null && val == 0) {
            MapController.putHexMapValue(h, b);
            if (!GameData.FIRST_PIECE)
                GameData.HUMAN_PLAYER_TURN = !GameData.HUMAN_PLAYER_TURN;
            GameData.FIRST_PIECE = !GameData.FIRST_PIECE;
            GameData.GAME_STATE.NUMBER_OF_TILES_PLACED++;
            GameData.GAME_STATE.FREE_TILES_LEFT--;
            drawHexTile(h, b);
        }
    }

    private static Color getColorByByte(Byte b) {
        if (b == 0)
            return new Color(140, 184, 125);
        else if (b == 1)
            return Color.white;
        else
            return Color.black;
    }

    public static byte determineNextMoveColor() {
        Byte b = 2;
        if ((GameData.HUMAN_PLAYER_FIRST && GameData.FIRST_PIECE) ||
                (!GameData.HUMAN_PLAYER_FIRST && !GameData.FIRST_PIECE)) {
            b = 1;
        }
        return b;
    }

//    public static void revertLastMovement() {
//        if (!GameData.GAME_STATES.isEmpty()) {
//            GameData.GAME_STATES.pop();
//            generateBoard(false);
//            GameData.FIRST_PIECE = !GameData.FIRST_PIECE;
//        }
//    }
}
