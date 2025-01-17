import java.io.File;
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
            System.out.print("Enter a session name: ");
            String sessionName = temp.nextLine();
            while (new File(sessionSpacePath + "\\" + sessionName).exists()) {
                System.out.println("Session already exists. Please enter a valid session name: ");
                sessionName = temp.nextLine();
            }
            System.out.print("Enter your name: ");
            String name = temp.nextLine();
            session = new Session(sessionName, sessionSpacePath, name, games);
        } else {
            String sessionName = Session.getSessionChoice(sessionSpacePath, temp);
            System.out.print("Enter your name: ");
            String name = temp.nextLine();
            session = new Session(sessionName, sessionSpacePath, name);
        }
        temp.next();// DEBUG - SsWait to clean up
        session.clean();
    }
}
