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

    public String toSaveXML(){
        String str="\n\t\t\t<country>";
        str+="\n\t\t\t\t<countryname>"+name+"</countryname>";
        str+="\n\t\t\t\t<troop>"+armySize+"</troop>";
        str+="\n\t\t\t</country>";
        return str;
    }

    public String toSimpleXML(){
        String str="<country>";
        str+="<name>"+name+"</name>";
        str+="<x>"+getX()+"</x>";
        str+="<y>"+getY()+"</y>";
        str+="</country>";
        return str;
    }

    /**
     * get neighbours list
     * @return
     */
    public List<Country> getNeighbours() {
        return neighbours;
    }

    public String toXML(){
        String str="<country>";
        str+="<name>"+name+"</name>";
        str+="<player>"+owner.toSimpleXML()+"</player>";//TODO
        str+="<troop>"+armySize+"</troop>";
        str+="<color>"+color.toString()+"</color>";
        str+="<x>"+getX()+"</x>";
        str+="<y>"+getY()+"</y>";
        str+="<neighbours>";
        for (Country country : neighbours) {
            str+=country.toSimpleXML();
        }
        str+="</neighbours>";
        return str;

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
    *@return country x's dimension value
    */
    public int getX() {return location.x;}
    
    
    /**
    *@return country's y dimension value
    */
    public int getY() {return location.y;}
    
    /**
    *@return army number
    */
    public int getArmySize() {
        return armySize;
    }

    
    /**
    *@return color
    */
    public Color getColor() {
        return color;
    }
    
    
    /**
    *set color of country
    *@oaram country's color
    */
    public void setColor(Color color) {
        this.color = color;
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

    public void addNeighbour(Country country) {
        neighbours.add(country);
    }
    
    /**
     * @param country - which country we want to see is a neighbour
     * @return boolean - if the given country is the current country's neighbour then return true, otherwise return false
    */
    public boolean hasNeighbor(Country country) {
        return neighbours.contains(country);
    }

    /**
     * @return ArrayList<Country></Country> - the list of countries that are neighbours of the given country
     */
    public List<Country> neighbours() {
        return neighbours;
    }


    @Override
    public String toString() {
        return getName() + " is owned by Player " + getOwner().getName() + ", the number of troops: " + getArmySize();
    }

    public int compareTroops(Country country) {
        return this.armySize - country.getArmySize();
    }
}
