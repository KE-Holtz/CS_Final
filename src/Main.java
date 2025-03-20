import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import backend.Session;
import gameplay.games.Game;
import gameplay.games.ReadWriteGame;

public class Main {
    public static void main(String[] args) {
        Scanner temp = new Scanner(System.in);

        final String sessionSpacePath = "S:\\High School\\WuestC\\Drop Box\\KE_Multi_2";
        String host = "";
        boolean valid = false;
        boolean hosting = false;
        System.out.print("Are you hosting? (Y/N)");
        while (!valid) {
            host = temp.nextLine();
            if (host.equalsIgnoreCase("y")) {
                hosting = true;
                valid = true;
            } else if (host.equalsIgnoreCase("n")) {
                hosting = false;
                valid = true;
            }
        }

        ArrayList<Game> games = new ArrayList<Game>();
        games.add(new ReadWriteGame());

        Session session;
        String sessionName;
        if (hosting) {
            System.out.print("Enter a session name: ");
            sessionName = temp.nextLine();
            while (new File(sessionSpacePath + "\\" + sessionName).exists()) {
                System.out.println("Session already exists. Please enter a valid session name: ");
                sessionName = temp.nextLine();
            }
        } else {
            sessionName = Session.getSessionChoice(sessionSpacePath, temp);
        }
        System.out.print("Enter your name: ");
        String name = temp.nextLine();
        session = new Session(sessionName, sessionSpacePath, name, games, hosting);
        temp.next();// DEBUG - Wait to clean up
        temp.nextLine();
        session.runGame("ReadWrite");//TODO: Add games
        while (!session.clean()) {
            System.out.println("Failed to clean up session. Enter \"exit\" to force quit or anything else to try again.");
            if (temp.nextLine()
                    .equalsIgnoreCase("exit")) {
                break;
            }
        }
    }
}
