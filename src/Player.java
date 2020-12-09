import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ali Fahd
 */

public class Player {

    private String name;//player name
    protected List<Country> countries = new ArrayList<>();//player's country list
    private Color color;
    
    /**
    * @Constructor
    */
    public Player(String name) {
        this.name = name;
    }
    
    
    /**
    *@param name - player's name
    *@param color - player's color
    *@Constructor
    */
    public Player(String name, Color color){
        this.name = name;
        this.color = color;
    }

    public String toSimpleXML(){
        String str="<player>";
        str+="<name>"+this.name+"</name>";
        str+="<color>"+this.color.toString()+"</color>";
        str+="</player>";
        return str;
    }



    public String toSaveXML(){
        String str="<player>";
        str+="\n\t\t<name>"+this.name+"</name>";
        str+="\n\t\t<color>"+String.valueOf(this.color.getRGB())+"</color>";
        str+="\n\t\t<countries>";
        for (Country country : countries) {
            str+=country.toSaveXML();
        }
        str+="\n\t\t</countries>";
        str+="\n\t</player>";
        return str;
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

    
    /**
    *@return color
    */
    public Color getColor() {
        return color;
    }
    
    
    /**
    *the setter of name
    *@param name
    */
    public void setName(String name) {
        this.name = name;
    }
}
