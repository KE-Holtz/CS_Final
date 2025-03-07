package gameplay.games;

import java.util.Scanner;

import backend.Session;
import backend.globalvars.GlobalString;

enum State {
    READ_ONCE,
    WRITE_ONCE,
    READ_CONTINUOUS,
    WRITE_CONTINUOUS,
    EXITING,
}

public class ReadWriteGame extends Game {
    private static final String name = "ReadWrite";

    private State state;
    private final Scanner scanner = new Scanner(System.in);

    private GlobalString globalString;

    public ReadWriteGame() {
        setName(name);
    }

    @Override
    public void initialize(Session session) {
        globalString = new GlobalString(session, "globalString");
    }

    @Override
    public void startGame() {
        System.out.println("Starting ReadWrite game");
        while (setState());
    }

    @Override
    public boolean periodic() {
        switch (state) {
            case READ_ONCE:
                System.out.println("Reading once: " + globalString.getValue());
                setState();
                break;
            case WRITE_ONCE:
                System.out.println("Enter a value to write: ");
                globalString.setValue(scanner.nextLine());
                setState();
                break;
            case READ_CONTINUOUS:
                do {
                    System.out.println("Reading continuously: " + globalString.getValue());
                    System.out.println("Enter 'stop' to stop reading, anything else to continue");
                } while (scanner.next() != "stop");
                break;
            case WRITE_CONTINUOUS:
                do {
                    System.out.println("Enter a value to write continuously: ");
                    globalString.setValue(scanner.nextLine());
                    System.out.println("Enter 'stop' to stop writing, anything else to continue");
                } while (!scanner.next().equals("stop"));
                break;
            case EXITING:
                return false;
            default:
                break;
        }
        return true;
    }

    @Override
    public void endGame() {
        System.out.println("Ending ReadWrite game");
    }

    // True == failed
    public boolean setState() {
        System.out.println("Select a state: ");
        System.out.println("1. READ_ONCE -> ro");
        System.out.println("2. WRITE_ONCE -> wo");
        System.out.println("3. READ_CONTINUOUS -> rc");
        System.out.println("4. WRITE_CONTINUOUS -> wc");
        System.out.println("5. EXITING -> exit");
        String input = scanner.nextLine();
        switch (input) {
            case "ro":
                state = State.READ_ONCE;
                break;
            case "wo":
                state = State.WRITE_ONCE;
                break;
            case "rc":
                state = State.READ_CONTINUOUS;
                break;
            case "wc":
                state = State.WRITE_CONTINUOUS;
                break;
            case "exit":
                state = State.EXITING;
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                return true;
        }
        return false;
    }
}
