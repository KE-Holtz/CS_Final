package backend.personalvars;

import backend.Session;

public class PersonalDouble extends PersonalVar<Double>{
    public PersonalDouble(Session session, String name){
        super(session, name, Double::parseDouble);
    }
    public PersonalDouble(Session session, String name, Double value){
        super(session, name, Double::parseDouble, value);
    }
}
