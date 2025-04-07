package backend.personalvars;

import java.util.function.Function;

public class ClientVar<T> {
    private String                    name;
    private T                         value;
    private final Function<String, T> valueParser;

    public ClientVar(String name, T value, Function<String, T> valueParser) {
        this.name = name;
        this.value = value;
        this.valueParser = valueParser;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return valueParser.apply(valueOf(value.toString()));
    }

    public void setValue(T value) {
        this.value = value;
    }

    private String valueOf(String value) {
        return value.substring(value.indexOf(")") + 1);
    }
}
