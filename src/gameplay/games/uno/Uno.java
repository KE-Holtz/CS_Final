package gameplay.games.uno;
import backend.Session;
import gameplay.games.Game;
public class Uno extends Game{

    @Override
    public void initialize(Session session) {

    }

    @Override
    public void startGame() {
        UnoWindow uwu = new UnoWindow();
        
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
