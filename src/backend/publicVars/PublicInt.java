package backend.publicVars;
import gameplay.Player;

public class PublicInt extends PublicVar<Integer> {
    public PublicInt(Player player, String name) {
        super(player, name, Integer::parseInt);
    }

    public PublicInt(Player player, String name, Integer value) {
        super(player, name, Integer::parseInt, value);
    }
}
