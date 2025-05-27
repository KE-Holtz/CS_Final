package gameplay.games;

import backend.Lobby;
import backend.Session;

public class Hangman extends Game {

    public Lobby lobby;

    public Hangman(){
        setName("Hangman");
    }

    @Override
    public void initialize(Session session) {
        session.getLobby();
    }

    @Override
    public void startGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startGame'");
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
