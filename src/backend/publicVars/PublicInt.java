package backend.publicVars;
import backend.Session;

public class PublicInt extends PublicVar<Integer> {
    public PublicInt(Session session, String name) {
        super(session, name, Integer::parseInt);
    }

    public PublicInt(Session session, String name, Integer value) {
        super(session, name, Integer::parseInt, value);
    }
}
