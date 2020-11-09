import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiling Wang
 * @author Aleksandar Veselinovic
 * @author Ali Fahd
 */
public class Country {
    private String name;//country's name
    private Player owner;//currently choosing to have a list of countries in the player class of the countries the player owns
    private int armySize;//country's troop size
    private List<Country> neighbours;//country's neighbour country list
    private Color color;
    private Point location;

    /**
    *@Constructor
    */
    public Country(String name, int x, int y) {
        this.name = name;
        location = new Point(x, y);
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

    public int getX() {return location.x;}

    public int getY() {return location.y;}
    
    /**
    *@return army number
    */
    public int getArmySize() {
        return armySize;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @param name - match the countries by their name property
    *@return country
    */
    public Country getCountry(String name) {
        //match name field
        if (this.name.equals(name)) {
            return this;
        } else return null;
    }
    
    
    /**
     * set up the country belongs to whom
     * @param p - passing a Player object who will be the owner of the country
    */
    public void setOwner(Player p) {
        owner = p;
    }
    
    
    /**
     *add certain amount of troop
     * @param troops - number of troops need to be added to a country
    */
    public void addTroops(int troops) {
        this.armySize += troops;
    }
    
    
    /**
     *remove certain amount of troop
     * @param troops - number of troops need to be removed from a country
    */
    public void removeTroops(int troops) {
        this.armySize -= troops;
    }
    
    
    /**
     *set neighbours of country
     * @param countries - array of countries that need to be added as neighbours of a country
    */
    public void addNeighbours(Country[] countries) {
        //loop though the entire array passed so all countries can be added as neighbours
        for (int i = 0; i < countries.length; i++) {
            neighbours.add(countries[i]);
        }
    }
    
    
    
    /**
     * @param country - which country we want to see is a neighbour
     * @return boolean - if the given country is the current country's neighbour then return true, otherwise return false
    */
    public boolean hasNeighbor(Country country) {
        return neighbours.contains(country);
    }

    @Override
    public String toString() {
        return getName() + " is owned by Player " + getOwner().getName() + ", the number of troops: " + getArmySize();
    }
}
