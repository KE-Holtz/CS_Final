package gameplay.games.uno;
import backend.*;
import gameplay.*;
import gameplay.games.*;
import backend.globalvars.*;
import backend.publicvars.*;

public class Uno extends Game{
    private Lobby lobby;
    private Player self;
    private GlobalVar<Card> topCard;

    public Uno() {
        setName("Uno");
    }

    @Override
    public void initialize(Session session) {
        lobby = session.getLobby();
        self = lobby.getClientPlayer();
        topCard = new GlobalVar<Card>(session, "card", Card::fromString);
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
