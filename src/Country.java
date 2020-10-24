import java.util.ArrayList;
import java.util.List;

/**
 * @author: Xiling Wang
 */
public class Country {
    private String name;
    private Player owner;
    private int armySize;
    private List<Country> neighbours;

    public Country(String name) {
        this.name = name;
        armySize = 0;
        neighbours = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Player getOwner() {
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

    public void setOwner(Player p) {
        owner = p;
    }

    public void addTroops(int troops) {
        this.armySize += troops;
    }

    public void removeTroops(int troops) {
        this.armySize -= troops;
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
