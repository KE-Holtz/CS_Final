package gameplay.games.hangout;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import backend.Lobby;
import backend.Session;
import backend.publicvars.PublicInt;
import gameplay.Player;
import gameplay.games.Game;

public class Hangout extends Game {
    public Lobby lobby;

    public Player self;
    public JFrame myFrame;
    public ArrayList<Player> players;

    public long lastTimestamp;
    public long deltaT;
    public boolean grounded;
    public int x = 0;
    public double vx = 0;
    public double vy = 0;
    public final double gravityConstant = 0.02;
    public int y = 0;

    public PublicInt publicX;
    public PublicInt publicY;

    public PublicInt[] xs;
    public PublicInt[] ys;
    public JFrame[] frames;

    @Override
    public void initialize(Session session) {
        this.lobby = session.getLobby();
        this.self = lobby.getClientPlayer();
        players = lobby.getPlayers();
        publicX = new PublicInt(self, "x", 0);
        publicY = new PublicInt(self, "y", 0);
        frames = new JFrame[lobby.getPlayers().size()];
        xs = new PublicInt[lobby.getPlayers().size()];
        ys = new PublicInt[lobby.getPlayers().size()];
    }

    @Override
    public void startGame() {
        for (int i = 0; i < players.size(); i++) {
            JFrame frame = new JFrame(players.get(i).getName());
            if (players.get(i).equals(self)) {
                myFrame = frame;
                myFrame.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        int keyCode = e.getKeyCode();
                        if (keyCode == KeyEvent.VK_UP) {
                            if (grounded) {
                            vy = -5;
                            grounded = false;
                            }
                        }
                        if (keyCode == KeyEvent.VK_LEFT) {
                            vx = -1.5;
                        }
                        if (keyCode == KeyEvent.VK_RIGHT) {
                            vx = 1.5;
                        }
                    }
                    public void keyReleased(KeyEvent e) {
                        int keyCode = e.getKeyCode();
                        if(keyCode == KeyEvent.VK_LEFT) {
                            vx = 0;
                        }
                        if (keyCode == KeyEvent.VK_RIGHT) {
                            vx = 0;
                        }
                    }
                });
            }
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            frame.setSize(200, 200);
            frames[i] = frame;
            xs[i] = (PublicInt) players.get(i).getVariable("x").get();
            ys[i] = (PublicInt) players.get(i).getVariable("y").get();
            lastTimestamp = System.currentTimeMillis();
        }
    }

    @Override
    public boolean periodic() {
        deltaT = System.currentTimeMillis() - lastTimestamp;
        lastTimestamp = System.currentTimeMillis();
        if(!grounded) {
            vy += gravityConstant * deltaT;
            y += vy * deltaT;
        }
        x += vx * deltaT;
        if (y >= 1080 - 200) {
            grounded = true;
            y = 1080 - 200;
            vy = 0;
        }
        myFrame.setLocation(x, y);
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(self)) {
                publicX.setValue(x);
                publicY.setValue(y);
            } else {
                frames[i].setLocation(xs[i].getValue().orElse(0), ys[i].getValue().orElse(0));
            }
        }
        return true;
    }

    @Override
    public void endGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

    @Override
    public String getName() {
        return "Hangout";
    }

}
