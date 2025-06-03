package gameplay.games.uno;

import java.io.File;

public class Card {
    private String color;
    private int value;
    private File imageFile;

    public Card(String color, int value) {
        this.color = color;
        this.value = value;
        findImageFile();
    }

    public Card() {
        String[] colors = { "Red", "Green", "Blue", "Yellow" };
        this.color = colors[(int) (Math.random() * colors.length)];
        this.value = (int) (Math.random() * 14);
        if (value == 13) {
            color = "Wild";
            if (Math.random() < 0.5) {
                this.value = 13; // Wild
            } else {
                this.value = 14; // +4
            }
        }
        findImageFile();
    }

    private void findImageFile() {
        imageFile = new File("src\\gameplay\\games\\uno\\assets\\cards\\" + color + value + ".png");
    }

    public File getImageFile() {
        return imageFile;
    }

    public String getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return color + "_" + value;
    }

    public void updateCardFromString(String cardString) {
        String[] parts = cardString.split("_");
        if (parts.length == 2) {
            this.color = parts[0];
            this.value = Integer.parseInt(parts[1]);
            findImageFile();
        } else {
            throw new IllegalArgumentException("Invalid card string format: " + cardString);
        }
    }

    public void setColor(String color) {
        this.color = color;
    }
}
