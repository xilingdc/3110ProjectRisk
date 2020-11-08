import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiling Wang
 * @author Aleksandar Veselinovic
 * @author Ali Fahd
 */

public class Player {

    private String name;//player name
    private List<Country> countries = new ArrayList<>();//player's country list
    private Color color;
    
    /**
    * @Constructor
    */
    public Player(String name) {
        this.name = name;
    }
    
    public Player(String name, Color color){
        this.name = name;
        this.color = color;
    }
    /**
    *@param c Country, add c into list
    */
    public void addCountry(Country c){
        countries.add(c);
    }
    
    
    /**
    *@param c Country, remove c from list
    */
    public void removeCountry(Country c){
        countries.remove(c);
    }

    
    /**
    *@return player's name
    */
    public String getName(){
        return name;
    }
    
    
    /**
    *@return player's country list
    */
    public List<Country> getCountries(){
        return countries;
    }


    public Color getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }
}
