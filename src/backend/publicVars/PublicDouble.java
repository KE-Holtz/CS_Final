package backend.publicVars;

import gameplay.Player;

public class PublicDouble extends PublicVar<Double>{
    public PublicDouble(Player player, String name){
        super(player, name, Double::parseDouble);
    }
    public PublicDouble(Player player, String name, Double value){
        super(player, name, Double::parseDouble, value);
    }
}
