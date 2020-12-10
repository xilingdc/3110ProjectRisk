import java.util.ArrayList;

/**
 * @author Aleksandar Veselinovic
 */
public class Continent {
    private String name;
    private ArrayList<Country> countries;
    int bonusTroops;

    /**
     * constructor
     * @param name the name of the continent
     * @param troops the number of troops gained by owning the continent
     */
    public Continent(String name, int troops) {
        this.name = name;
        countries = new ArrayList<>();
        bonusTroops = troops;
    }

    /**
     * adds the country to the list of countries
     * @param country the country to be added
     */
    public void addCountry(Country country) {
        countries.add(country);
    }

    /**
     * Returns the continent's countries in an array list
     * @return the list of countries
     */
    public ArrayList<Country> getCountries() {
        return countries;
    }

    /**
     * Returns the bonus troops for owning the continent
     * @return bonus troops for owning the continent
     */
    public int getBonusTroops() {
        return bonusTroops;
    }
}
