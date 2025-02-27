package backend.globalvars;

public class GlobalInt extends GlobalVar<Integer>{
    public GlobalInt(String playerSpacePath, String clientName, String name){
        super(playerSpacePath, clientName, name, Integer::parseInt);
    }
    public GlobalInt(String playerSpacePath, String clientName, String name, Integer value){
        super(playerSpacePath, clientName, name, Integer::parseInt, value);
    }
}
