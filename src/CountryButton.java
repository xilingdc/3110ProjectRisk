import javax.swing.*;

public class CountryButton extends JButton {
    private final Country country;

    public CountryButton(int troops, Country country) {
        super(""+troops);
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }
}
