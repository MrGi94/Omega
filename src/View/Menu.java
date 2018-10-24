package View;

import Controller.GUI.BoardController;
import Controller.GUI.MenuController;
import Model.Constants;

import javax.swing.*;

public class Menu extends JFrame {

    public static Board board;

    public Menu() {
        setTitle(Constants.APP_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(generateMenu(new MenuController()));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        board = new Board();
        getContentPane().add(board);
        addMouseListener(new BoardController());
        setBounds(0, 0, 900, 800);
    }

    private JMenuBar generateMenu(MenuController menuController) {
        JMenuBar menuBar = new JMenuBar();
        JMenu subMenu, menu = new JMenu(Constants.MENU_FILE);
        JMenuItem menuSubItem, menuItem = new JMenuItem(Constants.MENU_ITEM_OPEN_SAVED_GAME);
        menuItem.addActionListener(menuController);
        menu.add(menuItem);
        menuItem = new JMenuItem(Constants.MENU_ITEM_SAVE_GAME_AS);
        menuItem.addActionListener(menuController);
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem(Constants.MENU_ITEM_EXIT);
        menuItem.addActionListener(menuController);
        menu.add(menuItem);
        menuBar.add(menu);

        menu = new JMenu(Constants.MENU_PLAY);
        menuItem = new JMenuItem(Constants.MENU_ITEM_NEW_GAME);
        menuItem.addActionListener(menuController);
        menu.add(menuItem);
        subMenu = new JMenu(Constants.MENU_ITEM_CHOOSE_SIDE);
        menuSubItem = new JMenuItem(Constants.MENU_ITEM_FIRST);
        menuSubItem.addActionListener(menuController);
        subMenu.add(menuSubItem);
        menuSubItem = new JMenuItem(Constants.MENU_ITEM_SECOND);
        menuSubItem.addActionListener(menuController);
        subMenu.add(menuSubItem);
        menu.add(subMenu);
        menuItem = new JMenuItem(Constants.MENU_ITEM_BACK);
        menuItem.addActionListener(menuController);
        menu.add(menuItem);
        menuBar.add(menu);
        return menuBar;
    }
}
