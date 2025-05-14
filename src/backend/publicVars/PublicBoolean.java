package backend.publicVars;

import gameplay.Player;

public class PublicBoolean extends PublicVar<Boolean>{
    public PublicBoolean(Player player, String name){
        super(player, name, Boolean::parseBoolean);
    }
    public PublicBoolean(Player player, String name, Boolean value){
        super(player, name, Boolean::parseBoolean, value);
    }
}
