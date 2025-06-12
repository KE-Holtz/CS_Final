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
    DRAW,
}

public class Uno extends Game{
    private UnoWindow uwu;

    private Lobby lobby;
    private Player self;
    private ArrayList<Player> players;
    private GlobalVar<Card> topCard;

    private PublicInt handSize;
    private ArrayList<Card> hand;

    private GlobalInt turnIndex;
    private GlobalInt numTurns;
    private int localNumTurns = 0;
    private GlobalInt drawCounter;

    private State state;

    public Uno() {
        setName("Uno");
    }

    @Override
    public void initialize(Session session) {
        lobby = session.getLobby();
        self = lobby.getClientPlayer();

        topCard = new GlobalVar<Card>(session, "card", Card::fromString, Card.randomNonWild());

        handSize = new PublicInt(self, "handSize");
        self.addVariable(handSize);

        turnIndex = new GlobalInt(session, "turnNum", 0);
        numTurns = new GlobalInt(session, "numTurns", 0);
        drawCounter = new GlobalInt(session, "drawCounter", 0);
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
        System.out.println(players);
    }

    @Override
    public boolean periodic() {
        if(numTurns.getValue().orElse(-1) > localNumTurns){
            uwu.reDraw();
            localNumTurns = numTurns.getValue().orElse(localNumTurns);
        }
        Player currentPlayer = players.get(turnIndex.getValue().orElse(0));
        // System.out.println("Current Player = " + currentPlayer.getName());
        // System.out.println("State = " + state);
        if(currentPlayer.equals(self)){
            state = State.TURN;
        } else if (drawCounter.getValue().orElse(0) > 0){
            state = State.DRAW;
        } else {
            state = State.WAITING;
        }
        switch (state) {
            case WAITING:
                //Wait for it to be your turn
                // System.out.println("Waiting");
                break;
            case TURN:
                // System.out.println("Taking turn");
                break;
            case DRAW:
                for(int i = 0; i < drawCounter.getValue().orElse(0); i++){
                    drawCard();
                }
                drawCounter.setValue(0);
                passTurn();
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
        System.out.println("Playing card, state = " + state);
        if (topCard.getValue().isEmpty() || state.equals(State.WAITING)) {
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
        } else if (card.isWild()) {
            hand.remove(card);
            System.out.println(hand);
            handSize.setValue(hand.size());
            topCard.setValue(card);
            uwu.updateTopCard(card);
            System.out.println(topCard.getValue().get());
            topCard.setValue(uwu.pickWildColor());
            System.out.println("Wild card played");
            if(card.getValue() == 14) {
                drawCounter.setValue(drawCounter.getValue().orElse(0) + 4);
            }
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
        if (turnIndex.getValue().isEmpty()) {
            System.out.println("Passing turn, turnNum is empty");
        } else {
            int nextTurnNum = (turnIndex.getValue().get() + 1) % players.size();
            System.out.println("Passing turn, nextTurnNum = " + nextTurnNum + ", numTurns = " + numTurns.getValue().orElse(-1) + ", players.size() = " + players.size());
            turnIndex.setValue(nextTurnNum);
        }
        numTurns.setValue(numTurns.getValue().orElse(-1) + 1);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Card getTopCard() {
        return topCard.getValue().orElse(Card.randomNonWild());
    }
}
