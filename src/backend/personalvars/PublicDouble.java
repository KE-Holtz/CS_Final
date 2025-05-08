package backend.personalvars;

import backend.Session;

public class PublicDouble extends PublicVar<Double>{
    public PublicDouble(Session session, String name){
        super(session, name, Double::parseDouble);
    }
    public PublicDouble(Session session, String name, Double value){
        super(session, name, Double::parseDouble, value);
    }
}
