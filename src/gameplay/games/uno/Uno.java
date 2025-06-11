package gameplay.games.uno;
import java.util.ArrayList;
import java.util.Comparator;

import backend.*;
import gameplay.*;
import gameplay.games.*;
import backend.globalvars.*;
import backend.publicvars.*;

enum State{
    WAITING,
    TURN,
}

public class Uno extends Game{
    private UnoWindow uwu;

    private Lobby lobby;
    private Player self;
    private ArrayList<Player> players;
    private GlobalVar<Card> topCard;

    private PublicInt handSize;
    private ArrayList<Card> hand;

    private GlobalInt turnNum;

    private State state;

    public Uno() {
        setName("Uno");
    }

    @Override
    public void initialize(Session session) {
        lobby = session.getLobby();
        self = lobby.getClientPlayer();

        topCard = new GlobalVar<Card>(session, "card", Card::fromString, Card.random());

        handSize = new PublicInt(self, "handSize");
        self.addVariable(handSize);

        turnNum = new GlobalInt(session, "turnNum", 0);
    }

    @Override
    public void startGame() {
        uwu = new UnoWindow(this);
        uwu.updateTopCard(topCard.getValue().orElse(Card.randomNonWild()));
        hand = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            hand.add(Card.random());
        }
        handSize.setValue(hand.size());
        uwu.updateHand(hand);

        players = lobby.getPlayers();
        players.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
    }

    @Override
    public boolean periodic() {
        Player currentPlayer = players.get(turnNum.getValue().orElse(0));
        if(currentPlayer.equals(self)){
            state = state.TURN;
        } else {
            state = state.WAITING;
        }
        switch (state) {
            case WAITING:
                //Wait for it to be your turn
                break;
            case TURN:
                //Handle turn here
                break;
            default:
                break;
        }
        handSize.setValue(hand.size());
        return true;
    }

    @Override
    public void endGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

    public void playCard(Card card){
        System.out.println("PLAYYYYY");
        if (topCard.getValue().isEmpty() || state.equals(State.WAITING)) {
            System.out.println("silly silly");
            System.out.println("State = " + state);
            System.out.println("card = " + topCard.getValue().orElse(new Card("Red", 1)));
            return;
        } else if (card.matches(topCard.getValue().get())) {
            hand.remove(card);
            System.out.println(hand);
            handSize.setValue(hand.size());
            topCard.setValue(card);
            uwu.updateTopCard(card);
            System.out.println(topCard.getValue().get());
            passTurn();
        } else{
            System.out.println("Rong card dipass");
        }
        uwu.reDraw();
    }

    public void drawCard(){
        if(state.equals(State.TURN)){
            hand.add(Card.random());
            handSize.setValue(hand.size());
        }
        uwu.reDraw();
    }

    public void passTurn(){
        turnNum.setValue(turnNum.getValue().orElse(0) + 1 % players
        .size());
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Card getTopCard() {
        return topCard.getValue().orElse(Card.randomNonWild());
    }
}
