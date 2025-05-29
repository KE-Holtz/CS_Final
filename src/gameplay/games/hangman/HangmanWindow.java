package gameplay.games.hangman;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HangmanWindow{
    private boolean isWordChooser;

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private WordPickingScreen wordPickingScreen;
    public HangmanWindow(boolean isWordChooser){
        this.isWordChooser = isWordChooser;

        frame = new JFrame("Hangman");
        mainPanel = new JPanel();
        cardLayout = new CardLayout();

        wordPickingScreen = new WordPickingScreen();

        mainPanel.setLayout(new CardLayout());
        if (isWordChooser) {
            mainPanel.add(wordPickingScreen);
            cardLayout.show(mainPanel, wordPickingScreen.getName());
        }
        frame.setVisible(true);
    }
}
