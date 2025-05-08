package gameplay.games;

import backend.Session;

/*The reason to use an abstract class is to serve as a template
 * for the different types of games that can be played.
 * Other classes can extend this class to create a new game
 * Then, any of the unique games' objects can be handled as if they were identical
 * Instead of having a method that needs a Clue game passed in and a method that needs an Uno game passed in,
 * you can have one method that takes a Game.
 *
 * It also gives a format for different types of games when creating a new game.
 */
public abstract class Game {

    private String name;
    private Session session;

    public abstract void initialize(Session session);
    public abstract void startGame();
    public abstract boolean periodic(); //Return false on game end
    public abstract void endGame();

    protected void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public Session getSession(){
        return session;
    }
}
