package backend.globalvars;

public class GlobalDouble extends GlobalVar<Double>{    
    public GlobalDouble(String playerSpacePath, String clientName, String name){
        super(playerSpacePath, clientName, name, Double::parseDouble);
    }
    public GlobalDouble(String playerSpacePath, String clientName, String name, Double value){
        super(playerSpacePath, clientName, name, Double::parseDouble, value);
    }
}
