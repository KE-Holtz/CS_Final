package gameplay.games.uno;
import java.util.ArrayList;

import backend.*;
import gameplay.*;
import gameplay.games.*;
import backend.globalvars.*;
import backend.publicvars.*;

enum State{
    WAITING,
    TURN,
    SPECIAL,
}
//TODO: 1) fix skip 2) test reverse 3) add other player UI with turns and card count 4) add turn indicator 5) add end message
public class Uno extends Game{
    private UnoWindow uwu;

    private Lobby lobby;
    private Player self;
    private ArrayList<Player> players;

    private GlobalVar<Card> topCard;
    private Card localTopCard;

    private PublicInt handSize;
    private ArrayList<Card> hand;

    private GlobalInt turnIndex;
    private GlobalInt numTurns;
    private int localTurnIndex = 0;
    private int localNumTurns = 0;

    private GlobalInt drawCounter;
    private GlobalBoolean skip;
    private GlobalBoolean reverse;

    private State state;

    private int safeTurnNum = -1;

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
        skip = new GlobalBoolean(session, "skip", false);
        reverse = new GlobalBoolean(session, "reverse", false);
    }

    @Override
    public void startGame() {
        System.out.println("This player is " + self.getName());
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
        // System.out.println(players);
    }

    @Override
    public boolean periodic() {
        syncVars();
        if(numTurns.getValue().orElse(0) > localNumTurns){
            uwu.reDraw();
            localNumTurns = numTurns.getValue().orElse(localNumTurns);
            // System.out.println("Redrew due to turn diff");
        }
        Player currentPlayer = players.get(localTurnIndex);
        // System.out.println("Current Player = " + currentPlayer.getName());
        // System.out.println("State = " + state);
        if(!currentPlayer.equals(self)){
            state = State.WAITING;
        } else if ((drawCounter.getValue().orElse(0) > 0 || skip.getValue().orElse(false)) && safeTurnNum < localNumTurns){
            state = State.SPECIAL;
        } else {
            state = State.TURN;
        }
        switch (state) {
            case WAITING:
                //Wait for it to be your turn
                // System.out.println("Waiting");
                break;
            case TURN:
                // System.out.println("Taking turn");
                break;
            case SPECIAL:
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Special case:");
                System.out.println("  drawCounter = " + drawCounter.getValue().orElse(-1));
                System.out.println("  skip = " + skip.getValue().orElse(false));
                for(int i = 0; i < drawCounter.getValue().orElse(0); i++){
                    drawCard();
                    System.out.println(self.getName() + ": Drew");
                }
                drawCounter.setValue(0);
                skip.setValue(false);
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

    public void syncVars(){
        if(topCard.getValue().isEmpty()){
            System.out.println("Top card is empty");
        } else {
            localTopCard = topCard.getValue().orElse(localTopCard);
        }

        if(turnIndex.getValue().isEmpty()){
            System.out.println("Turn index is empty");
        } else {
            localTurnIndex = turnIndex.getValue().orElse(localTurnIndex);
        }
    } 

    public void playCard(Card card){
        // System.out.println("Playing card, state = " + state);
        if (state.equals(State.WAITING)) {
            return;
        } else if (card.isWild()) {
            hand.remove(card);
            handSize.setValue(hand.size());
            topCard.setValue(card);
            uwu.updateTopCard(card);
            // System.out.println(topCard.getValue().get());
            topCard.setValue(uwu.pickWildColor());
            // System.out.println("Wild card played");
            if(card.getValue() == 14) {
                safeTurnNum = localNumTurns;
                drawCounter.setValue(4);
            }
            passTurn();
        } else if (card.matches(localTopCard)){
            hand.remove(card);
            handSize.setValue(hand.size());
            topCard.setValue(card);
            uwu.updateTopCard(card);
            switch (card.getValue()) {
                case 10:
                    skip.setValue(true);
                    break;
                case 11:
                    reverse.setValue(!reverse.getValue().orElse(false));
                    break;
                case 12:
                    safeTurnNum = localNumTurns;
                    drawCounter.setValue(2);
                default:
                    break;
            }
            // System.out.println(topCard.getValue().get());
            passTurn();
        } else{
            // System.out.println("Rong card dipass");
        }
        uwu.reDraw();
    }

    public void drawCard(){
        if(!state.equals(State.WAITING)){
            hand.add(Card.random());
            handSize.setValue(hand.size());
        }
        uwu.reDraw();
    }

    public void passTurn(){
            int nextTurnNum;
            if(reverse.getValue().orElse(false)){
                nextTurnNum = localTurnIndex - 1;
                if(nextTurnNum < 0){
                    nextTurnNum = players.size() - nextTurnNum;
                }else {
                    nextTurnNum = nextTurnNum % players.size();
                }
            } else {
                nextTurnNum = (localTurnIndex + 1) % players.size();
            }
            System.out.println("Passing turn, currentTurnNum = " + localTurnIndex + " nextTurnNum = " + nextTurnNum + ", numTurns = " + numTurns.getValue().orElse(0) + ", players.size() = " + players.size() + "reversing = " + reverse.getValue());
            turnIndex.setValue(nextTurnNum);
        if (numTurns.getValue().isEmpty()) {
            System.out.println("numTurns is empty");
        }
        else
        {
            localNumTurns++;
            numTurns.setValue(numTurns.getValue().orElse(0) + 1);
        }
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Card getTopCard() {
        return topCard.getValue().orElse(Card.randomNonWild());
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
