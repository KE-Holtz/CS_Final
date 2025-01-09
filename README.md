# Computer Science Final Project:

## Potential ideas:

Small/Easy to make:
- Uno
- Chutes and Ladders
- Mancala

Large/Difficult to make:
- Pool 
    - requires 2d physics engine, but could be reused
    - 2 Players
- Chess
    - Very complex to create
    - Only 2 players
    - Less interest
- Scrabble
    - Multiple players
    - Feasible to create
    - Needs a csv, etc to verify words are correct
- Bananagrams
    - Much more difficult than Scrabble
    - Needs a csv, etc to verify words
    - Not turn based
- **Clue**
    - Multiple players
    - Commonly known rules
    - Multiple game elements
    - Multiple actions need to be taken

## Flow

1. Create or join a session (user prompt)
2. Session has a name (User prompt) and ID
3. Creates a folder named the ID, which contains a text file with the metadata (Name, anything else needed)
4. Session also creates a player folder for the host, and new folders for joining players
5. The session contains all the players, the game (User prompt), and possibly serves as a lobby while waiting for a game to start
6. Once a game ends, all players are returned to the session
7. If a player leaves the session, their client needs to clean up all the files they made at any point. **This may mean the host must be the last person to leave, since the session folder they created may not be able to be deleted while other clients still have files inside it.**
