import java.lang.reflect.Array;
import java.util.ArrayList;

import Backend.Session;
import gameplay.Game;

public class Main{
    public static void main(String[] args) {
        final String sessionSpacePath = "G:\\High School\\WuestC\\Drop Box\\KEHmultiplayer";
        ArrayList<Game> games = new ArrayList<Game>();

        Session session = new Session("My Test Session", sessionSpacePath, "Kyle", games);
    }
}
