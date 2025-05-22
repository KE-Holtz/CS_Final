package backend.publicVars;

import gameplay.Player;

public class PublicBoolean extends PublicVar<Boolean>{
    public PublicBoolean(Player player, String name){
        super(player, name, Boolean::parseBoolean);
    }
    public PublicBoolean(Player player, String name, Boolean value){
        super(player, name, Boolean::parseBoolean, value);
    }

    @Override
    public void setValue(Boolean value, Tag... tags) {
        Tag[] tags2 = new Tag[tags.length - 1];
        for (int i = 0; i < tags.length; i++) {
            tags2[i] = tags[i];
        }
        tags2[tags2.length - 1] = Tag.BOOL;
        super.setValue(value, tags2);
    }
}
