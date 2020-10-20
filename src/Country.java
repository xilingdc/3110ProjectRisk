import java.util.ArrayList;
import java.util.List;

/**
 * @author: Xiling Wang
 */
public class Country {
    private String name;
    private String owner;
    private int armySize;
    private List<Country> neighbours;

    public Country(String name) {
        this.name = name;
        owner = "";
        armySize = 0;
        neighbours = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public int getArmySize() {
        return armySize;
    }

    public Country getCountry(String name) {
        if (this.name.equals(name)) {
            return this;
        } else return null;
    }

    public void setOwner(String player) {
        owner = player;
    }

    public void setArmySize(int armySize) {
        this.armySize = armySize;
    }

    public void addNeighbours(Country[] countries) {
        for (int i = 0; i < countries.length; i++) {
            neighbours.add(countries[i]);
        }
    }

    public boolean hasNeighbor(Country country) {
        return neighbours.contains(country);
    }
}
