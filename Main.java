import java.util.ArrayList;
import java.util.Scanner;

import Backend.Session;
import gameplay.Game;

public class Main{
    public static void main(String[] args) {
        Scanner temp = new Scanner(System.in);
        final String sessionSpacePath = "/home/kyleh/Documents/adsfg"; //"G:\\High School\\WuestC\\Drop Box\\KE_Multi_2";
        ArrayList<Game> games = new ArrayList<Game>();
        Session session = new Session("A", sessionSpacePath, "Kyle", games);
        temp.next();
        session.clean();
    }
}
