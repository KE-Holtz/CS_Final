package backend.globalvars;

import backend.Session;

public class GlobalDouble extends GlobalVar<Double>{    
    public GlobalDouble(Session session, String name){
        super(session, name, Double::parseDouble);
    }
    public GlobalDouble(Session session, String name, Double value){
        super(session, name, Double::parseDouble, value);
    }
}
