package quiztastic.core;

import java.util.Objects;

public class Spiller {
    private final String name;

    public Spiller(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spiller spiller = (Spiller) o;
        return Objects.equals(name, spiller.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Spiller{" +
                "name='" + name + '\'' +
                '}';
    }
}
