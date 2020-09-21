package quiztastic.core;

import java.util.Objects;

public class Player {
    private final String name;
    private final boolean gameMaster;

    public Player(String name, boolean gameMaster) {
        this.name = name;
        this.gameMaster = gameMaster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player spiller = (Player) o;
        return Objects.equals(name, spiller.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
