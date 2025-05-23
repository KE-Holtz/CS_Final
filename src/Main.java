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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import backend.Session;
import gameplay.games.Game;
import gameplay.games.ReadWriteGame;

public class Main {
    private static JFrame frame = new JFrame();
    private final static String sessionSpacePath = "C:\\Users\\natha\\Downloads\\testServer";

    public static void main(String[] args) {
        frame.setLayout(new BorderLayout());

        Scanner temp = new Scanner(System.in);
        boolean hosting = false;

        JPanel screen = new JPanel();
        JPanel labelPanel = new JPanel();
        JPanel buttons = new JPanel();
        JPanel input = new JPanel();

        Font font = new Font("Arial", Font.PLAIN, 20);

        screen.setLayout(new GridBagLayout());
        screen.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        buttons.setLayout(new FlowLayout());
        buttons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        frame.add(screen);

        JLabel label = new JLabel();

        JTextField userText = new JTextField();

        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        JButton enter = new JButton("Enter");

        label.setFont(font);
        label.setText("Are you hosting?");
        label.setVisible(true);

        yes.setFocusPainted(false);
        yes.setBackground(Color.WHITE);
        yes.setFont(font);

        no.setFocusPainted(false);
        no.setBackground(Color.WHITE);
        no.setFont(font);

        enter.setFocusPainted(false);
        enter.setBackground(Color.WHITE);
        enter.setFont(font);

        labelPanel.add(label);

        buttons.add(yes);
        buttons.add(no);

        input.add(userText);
        input.add(enter);

        gbc.gridx = 0;
        gbc.gridy = 0;
        screen.add(labelPanel, gbc);
        gbc.gridy = 1;
        screen.add(buttons, gbc);
        screen.add(input, gbc);

        userText.setColumns(20);
        userText.setVisible(false);
        userText.setFont(font);
        enter.setVisible(false);

        frame.pack();
        frame.setSize(600, 200);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

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
            label.setText("Enter the session name: ");
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
                label.setText("Session already exists. Please enter a valid session name: ");
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
            label.setText("click on the session you would like to join");
            userText.setVisible(false);
            enter.setVisible(false);

            ArrayList<JButton> sessionButtons = new ArrayList<>();
            final String[] sessionNameTemp = { "" };
            int numOfFiles = new File(sessionSpacePath).list().length;
            while (sessionNameTemp[0].equals("")) {
                for (String i : new File(sessionSpacePath).list()) {
                    final String rawSessionName = decodeString(i);
                    JButton sessionButton = new JButton(i);
                    sessionButton.setName(i);
                    boolean exists = false;
                    for (int j = 0; j < sessionButtons.size(); j++) {
                        if (sessionButtons.get(j)
                                .getName()
                                .equals(sessionButton.getName())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        sessionButton.setVisible(true);
                        sessionButton.setFocusPainted(false);
                        sessionButton.setBackground(Color.WHITE);
                        Icon icon = new ImageIcon("src/images/folder.png");
                        sessionButton.setIcon(icon);
                        sessionButton.setHorizontalTextPosition(JButton.CENTER);
                        sessionButton.setVerticalTextPosition(JButton.BOTTOM);
                        sessionButtons.add(sessionButton);
                        buttons.add(sessionButton, gbc);
                        sessionButton.addActionListener(e -> {
                            sessionNameTemp[0] = rawSessionName;
                            for (Component component : screen.getComponents()) {
                                if (component instanceof Button) {
                                    component.setVisible(false);
                                }
                            }
                        });
                    }
                }
                screen.revalidate();
                screen.repaint();
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
        buttons.removeAll();
        buttons.revalidate();
        buttons.repaint();
        userText.setText("");
        label.setText("Enter your name:");
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
            label.setText("Name already exists. Please enter a valid name: ");
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
        frame.dispose();
        // todo add a lobby screen, different for hosting and joining, game selection,
        // player list, ect...
        if (hosting) {
            session.host();
        } else {
            session.join();
        }
        temp.next();// DEBUG - Wait to clean up
        temp.nextLine();
        session.runGame("ReadWrite");// TODO: Add games
        while (!session.clean()) {
            System.out.println(
                    "[DEBUG] Failed to clean up session. Enter \"exit\" to force quit or anything else to try again.");
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
