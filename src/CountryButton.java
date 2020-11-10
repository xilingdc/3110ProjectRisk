import javax.swing.*;
/**
 * @author Aleksandar Veselinovic
 */
public class CountryButton extends JButton {
    private final Country country;//the country the button represents

    /**
     * Constructor for CountryButton objects
     *
     * @param troops the number to display on the button
     * @param country the country the button represents
     */
    public CountryButton(int troops, Country country) {
        super(""+troops);
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }
}
