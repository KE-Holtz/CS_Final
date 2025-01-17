import java.util.ArrayList;
import java.util.Scanner;

import Backend.Session;
import gameplay.Game;

public class Main {
    public static void main(String[] args) {

        Scanner temp = new Scanner(System.in);

        final String sessionSpacePath = "G:\\High School\\WuestC\\Drop Box\\KE_Multi_2";
        final boolean hosting = false;// DEBUGGING - change this locally to test hosting/player joining
        ArrayList<Game> games = new ArrayList<Game>();
        Session session;
        if (hosting) {
            session = new Session("A", sessionSpacePath, "Kyle", games);
        } else {
            session = new Session("A", sessionSpacePath, "Kyle");
        }
        temp.next();//DEBUG - SsWait to clean up 
        session.clean();
    }
}
