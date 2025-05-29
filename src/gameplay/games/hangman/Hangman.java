package gameplay.games.hangman;

import javax.swing.JFrame;

import backend.Lobby;
import backend.Session;
import backend.globalvars.GlobalString;
import gameplay.Player;
import gameplay.games.Game;
public class Hangman extends Game {

    private Lobby  lobby;
    private Player self;

    private boolean amWordChooser;

    GlobalString wordChooser;
    GlobalString word;

    private HangmanWindow window;

    public Hangman() {
        setName("Hangman");
    }

    @Override
    public void initialize(Session session) {
        lobby = session.getLobby();
        self = lobby.getClientPlayer();
        wordChooser = new GlobalString(session, "wordChooser");
        word = new GlobalString(session, "word");
    }

    @Override
    public void startGame() {
        if (wordChooser.getValue().isEmpty()) {
            wordChooser.setValue(self.getName());
            amWordChooser = true;
        } else{
            amWordChooser = false;
        }

        window = new HangmanWindow(amWordChooser);
    }

    @Override
    public boolean periodic() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'periodic'");
    }

    @Override
    public void endGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

}
