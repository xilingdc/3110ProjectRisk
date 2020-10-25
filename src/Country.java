import java.util.ArrayList;
import java.util.List;

/**
 * @author: Xiling Wang
 */
public class Country {
    private String name;//country's name
    private Player owner;//currently choosing to have a list of countries in the player class of the countries the player owns
    private int armySize;//country's troop size
    private List<Country> neighbours;//country's neighbour country list
    
    /**
    *@Constructor
    */
    public Country(String name) {
        this.name = name;
        armySize = 0;
        neighbours = new ArrayList<>();
    }
    
    /**
    *@return name of country
    */
    public String getName() {
        return name;
    }
    /**
    *@return the player who owns the country
    */
    public Player getOwner() {
        return owner;
    }
    
    
    /**
    *@return armay number 
    */
    public int getArmySize() {
        return armySize;
    }
    
    
    /**
    *@return country 
    */
    public Country getCountry(String name) {
        if (this.name.equals(name)) {
            return this;
        } else return null;
    }
    
    
    /**
    *set up the country belongs to whom
    */
    public void setOwner(Player p) {
        owner = p;
    }
    
    
    /**
    *add certain amount of troop
    */
    public void addTroops(int troops) {
        this.armySize += troops;
    }
    
    
    /**
    *remove certain amount of troop
    */
    public void removeTroops(int troops) {
        this.armySize -= troops;
    }
    
    
    /**
    *set neighbours of country
    */
    public void addNeighbours(Country[] countries) {
        for (int i = 0; i < countries.length; i++) {
            neighbours.add(countries[i]);
        }
    }
    
    
    
    /**
    *@return if the given country is the current country's neighbour then return true, otherwise return false
    */
    public boolean hasNeighbor(Country country) {
        return neighbours.contains(country);
    }
}
