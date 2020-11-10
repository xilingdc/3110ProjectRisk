import javax.swing.*;
/**
 * @author Aleksandar Veselinovic
 */
public class CountryButton extends JButton {
    private final Country country;
    
    
    /**
    *@Constructor
    *@param troops: country's troop size
    *@param country
    */
    public CountryButton(int troops, Country country) {
        super(""+troops);
        this.country = country;
    }
    
    
    /**
    *@return country
    */
    public Country getCountry() {
        return country;
    }
}
