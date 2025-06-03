package backend.publicvars;
import gameplay.Player;

public class PublicInt extends PublicVar<Integer> {
    public PublicInt(Player player, String name) {
        super(player, name, Integer::parseInt);
    }

    public PublicInt(Player player, String name, Integer value) {
        super(player, name, Integer::parseInt, value);
    }
@Override
    public void setValue(Integer value, Tag... tags) {
        Tag[] tags2 = new Tag[tags.length + 1];
        for (int i = 0; i < tags.length; i++) {
            tags2[i] = tags[i];
        }
        tags2[tags2.length - 1] = Tag.INT;
        super.setValue(value, tags2);
    }

}
