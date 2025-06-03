package gameplay.games.uno;
import backend.*;
import gameplay.*;
import gameplay.games.*;
import backend.globalvars.*;
import backend.publicvars.*;

public class Uno extends Game{
    private Lobby lobby;
    private Player self;
    private Card card;
    private GlobalString cardName;

    public Uno() {
        setName("Uno");
    }

    @Override
    public void initialize(Session session) {
        lobby = session.getLobby();
        self = lobby.getClientPlayer();
        cardName = new GlobalString(session, "cardName");
        if(cardName.getValue().orElse("null").equals("")) {
            card = new Card();
            cardName.setValue(card.toString());
        } else {
            card = new Card();
            card.updateCardFromString(cardName.getValue().orElse(""));
        }
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
