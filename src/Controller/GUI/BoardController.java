package Controller.GUI;

import Controller.GameLogicController;
import Controller.MapController;
import Model.*;
import Model.Point;
import View.Board;
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
        Board board = Menu.board;
        board.drawPolygon(board.getGraphics(), x, y, getColorByByte(b));
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
            Byte b = determineNextMoveColor();
            placeOnFreeTile(h, b);
        } else {
            GameLogicController.newTurn();
        }
    }

    public static void placeOnFreeTile(Hexagon h, Byte b) {
        Byte val = MapController.getHexMapColor(h);
        if (val != null && val == 0) {
            MapController.putHexMapValue(h, b, (byte) GameData.NUMBER_OF_TILES_PLACED);
            if (!GameData.FIRST_PIECE)
                GameData.HUMAN_PLAYER_TURN = !GameData.HUMAN_PLAYER_TURN;
            GameData.HEX_LIST.add(h);
            GameData.FIRST_PIECE = !GameData.FIRST_PIECE;
            GameData.NUMBER_OF_TILES_PLACED = GameData.NUMBER_OF_TILES_PLACED + 1;
            GameData.FREE_TILES_LEFT = GameData.FREE_TILES_LEFT - 1;
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

    public static void revertLastMovement() {
        if (!GameData.HEX_LIST.isEmpty()) {
            Hexagon h = GameData.HEX_LIST.remove(GameData.HEX_LIST.size() - 1);
            MapController.putHexMapValue(h, (byte) 0, (byte) 0);
            GameData.FIRST_PIECE = !GameData.FIRST_PIECE;
            GameData.NUMBER_OF_TILES_PLACED = GameData.NUMBER_OF_TILES_PLACED - 1;
            GameData.FREE_TILES_LEFT = GameData.FREE_TILES_LEFT + 1;
            drawHexTile(h, (byte) 0);
        }
    }
}
