package backend.personalvars;

import backend.Session;

public class PersonalBoolean extends PersonalVar<Boolean>{
    public PersonalBoolean(Session session, String name){
        super(session, name, Boolean::parseBoolean);
    }
    public PersonalBoolean(Session session, String name, Boolean value){
        super(session, name, Boolean::parseBoolean, value);
    }
}
