package backend.publicVars;

import backend.Session;

public class PublicBoolean extends PublicVar<Boolean>{
    public PublicBoolean(Session session, String name){
        super(session, name, Boolean::parseBoolean);
    }
    public PublicBoolean(Session session, String name, Boolean value){
        super(session, name, Boolean::parseBoolean, value);
    }
}
