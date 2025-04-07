package backend.personalvars;
import backend.Session;

public class PersonalInt extends PersonalVar<Integer> {
    public PersonalInt(Session session, String name) {
        super(session, name, Integer::parseInt);
    }

    public PersonalInt(Session session, String name, Integer value) {
        super(session, name, Integer::parseInt, value);
    }
}
