package backend.globalvars;

public class GlobalInt extends GlobalVar{
    public GlobalInt(String playerSpacePath, String clientName, String name){
        super(playerSpacePath, clientName, name, Integer::parseInt);
    }
}
