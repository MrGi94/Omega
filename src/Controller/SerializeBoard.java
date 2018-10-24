package Controller;

import Model.Constants;

import java.io.*;

public class SerializeBoard {

    public static boolean serializeBoard(Object o) {
        try {
            String s = Constants.SERIALIZE_PATH;
            FileOutputStream fileOut = new FileOutputStream(Constants.SERIALIZE_PATH);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(o);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
            return false;
        }
        return true;
    }

    public static Object deserializeBoard() {
        Object o = null;
        try {
            FileInputStream fileIn = new FileInputStream(Constants.SERIALIZE_PATH);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            o = in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Cannot find game state");
            c.printStackTrace();
        }
        return o;
    }
}
