package backend.globalvars;
import backend.Session;

public class GlobalInt extends GlobalVar<Integer> {
    public GlobalInt(Session session, String name) {
        super(session, name, Integer::parseInt);
    }

    public GlobalInt(Session session, String name, Integer value) {
        super(session, name, Integer::parseInt, value);
    }
}
