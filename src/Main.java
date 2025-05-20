import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import backend.Session;
import gameplay.games.Game;
import gameplay.games.ReadWriteGame;

public class Main {
    private static JFrame       frame            = new JFrame();
    private final static String sessionSpacePath = "S:\\High School\\WuestC\\Drop Box\\KE_Multi_2";

    public static void main(String[] args) {
        Scanner temp = new Scanner(System.in);
        String host = "";
        boolean valid = false;
        boolean hosting = false;

        JPanel labelPanel = new JPanel();
        JPanel fieldInputs = new JPanel();

        Font font = new Font("Arial", Font.PLAIN, 20);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        labelPanel.setLayout(new GridBagLayout());
        labelPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        fieldInputs.setLayout(new GridBagLayout());
        fieldInputs.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(labelPanel);
        frame.add(fieldInputs);
        frame.setSize(600, 600);

        JLabel label1 = new JLabel();

        JTextField userText = new JTextField();

        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        JButton enter = new JButton("Enter");

        yes.setFocusPainted(false);
        yes.setBackground(Color.WHITE);
        yes.setFont(font);

        no.setFocusPainted(false);
        no.setBackground(Color.WHITE);
        no.setFont(font);

        enter.setFocusPainted(false);
        enter.setBackground(Color.WHITE);
        enter.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldInputs.add(userText, gbc);
        gbc.gridy = 0;
        gbc.gridx = 0;
        fieldInputs.add(yes, gbc);
        gbc.gridx = 1;
        fieldInputs.add(no, gbc);
        gbc.gridx = 1;
        gbc.gridy = -1;
        fieldInputs.add(enter, gbc);

        label1.setFont(font);
        label1.setText("Are you hosting?");
        label1.setVisible(true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        labelPanel.add(label1);

        userText.setColumns(20);
        userText.setVisible(false);
        userText.setFont(font);
        enter.setVisible(false);

        final boolean[] hostingTemp = { false };
        yes.addActionListener(e -> {
            hostingTemp[0] = true;
            yes.setVisible(false);
            ;
            no.setVisible(false);
            ;
        });
        no.addActionListener(e -> {
            hostingTemp[0] = false;
            no.setVisible(false);
            ;
            yes.setVisible(false);
            ;
        });
        while (yes.isVisible() || no.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        hosting = hostingTemp[0];
        ArrayList<Game> games = new ArrayList<Game>();
        games.add(new ReadWriteGame());

        Session session;
        String sessionName;
        if (hosting) {
            label1.setText("Enter the session name: ");
            userText.setVisible(true);
            enter.setVisible(true);

            final String[] sessionNameTemp = { "" };
            enter.addActionListener(e -> {
                sessionNameTemp[0] = userText.getText();
            });
            while (sessionNameTemp[0].equals("")) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (new File(sessionSpacePath + "\\" + sessionNameTemp[0]).exists()) {
                label1.setText("Session already exists. Please enter a valid session name: ");
                sessionNameTemp[0] = "";
                enter.addActionListener(e -> {
                    sessionNameTemp[0] = userText.getText();
                });
                while (sessionNameTemp[0].equals("")) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            sessionName = sessionNameTemp[0];
            sessionName = encodeString(sessionName);
        } else {
            label1.setText("click on the session you would like to join");
            userText.setVisible(false);
            enter.setVisible(false);

            ArrayList<Button> sessionButtons = new ArrayList<>();
            final String[] sessionNameTemp = { "" };
            int numOfFiles = new File(sessionSpacePath).list().length;
            while (sessionNameTemp[0].equals("")) {
                for (String i : new File(sessionSpacePath).list()) {
                    final String rawSessionName = decodeString(i);
                    Button sessionButton = new Button(i);
                    boolean exists = false;
                    for (int j = 0; j < sessionButtons.size(); j++) {
                        if (sessionButtons.get(j)
                                          .getLabel()
                                          .equals(sessionButton.getLabel())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        sessionButton.setVisible(true);
                        sessionButtons.add(sessionButton);
                        fieldInputs.add(sessionButton, gbc);
                        sessionButton.addActionListener(e -> {
                            sessionNameTemp[0] = rawSessionName;
                            for (Component component : fieldInputs.getComponents()) {
                                if (component instanceof Button) {
                                    component.setVisible(false);
                                }
                            }
                        });
                    }
                }
                fieldInputs.revalidate();
                fieldInputs.repaint();
                while (sessionNameTemp[0].equals("")
                        && new File(sessionSpacePath).list().length == numOfFiles) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                numOfFiles = new File(sessionSpacePath).list().length;
            }
            sessionName = sessionNameTemp[0];
        }
        label1.setText("Enter your name:");
        userText.setVisible(true);
        enter.setVisible(true);
        final String[] nameTemp = { "" };
        enter.addActionListener(e -> {
            nameTemp[0] = userText.getText();
        });
        while (nameTemp[0].equals("")) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (new File(sessionSpacePath + "\\" + sessionName + "\\" + "players" + "\\"
                + nameTemp[0]).exists()) {
            label1.setText("Name already exists. Please enter a valid name: ");
            nameTemp[0] = "";
            enter.addActionListener(e -> {
                nameTemp[0] = userText.getText();
            });
            while (nameTemp[0].equals("")) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        String name = nameTemp[0];
        session = new Session(sessionName, sessionSpacePath, name, games, hosting);
        temp.next();// DEBUG - Wait to clean up
        temp.nextLine();
        session.runGame("ReadWrite");// TODO: Add games
        while (!session.clean()) {
            System.out.println("[DEBUG] Failed to clean up session. Enter \"exit\" to force quit or anything else to try again.");
            if (temp.nextLine()
                    .equalsIgnoreCase("exit")) {
                break;
            }
        }
    }

    public static String encodeString(String s) {
        s = s.replaceAll("#", "#" + "#");
        s = s.replaceAll("\\\\", "#" + "1");
        s = s.replaceAll("/", "#" + "2");
        s = s.replaceAll(":", "#" + "3");
        s = s.replaceAll("\\*", "#" + "4");
        s = s.replaceAll("\\?", "#" + "5");
        s = s.replaceAll("\"", "#" + "6");
        s = s.replaceAll("<", "#" + "7");
        s = s.replaceAll(">", "#" + "8");
        s = s.replaceAll("\\|", "#" + "9");
        return s;
    }

    public static String decodeString(String s) {
        s = s.replaceAll("#" + "#", "#");
        s = s.replaceAll("#" + "1", "\\\\");
        s = s.replaceAll("#" + "2", "/");
        s = s.replaceAll("#" + "3", ":");
        s = s.replaceAll("#" + "4", "*");
        s = s.replaceAll("#" + "5", "?");
        s = s.replaceAll("#" + "6", "\"");
        s = s.replaceAll("#" + "7", "<");
        s = s.replaceAll("#" + "8", ">");
        s = s.replaceAll("#" + "9", "|");
        return s;
    }
}
