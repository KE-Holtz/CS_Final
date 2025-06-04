package backend.publicVars;

import gameplay.Player;

public class PublicDouble extends PublicVar<Double>{
    public PublicDouble(Player player, String name){
        super(player, name, Double::parseDouble);
    }
    public PublicDouble(Player player, String name, Double value){
        super(player, name, Double::parseDouble, value);
    }

    @Override
    public void setValue(Double value, Tag... tags) {
        Tag[] tags2 = new Tag[tags.length + 1];
        for (int i = 0; i < tags.length; i++) {
            tags2[i] = tags[i];
        }
        tags2[tags2.length - 1] = Tag.DOUBLE;
        super.setValue(value, tags2);
    }

}
