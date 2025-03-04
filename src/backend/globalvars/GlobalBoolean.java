package backend.globalvars;

import backend.Session;

public class GlobalBoolean extends GlobalVar<Boolean>{
    public GlobalBoolean(Session session, String name){
        super(session, name, Boolean::parseBoolean);
    }
    public GlobalBoolean(Session session, String name, Boolean value){
        super(session, name, Boolean::parseBoolean, value);
    }
}
