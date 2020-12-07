import java.util.ArrayList;

public class Continent {
    private String name;
    private ArrayList<Country> countries;
    int bonusTroops;

    public Continent(String name, int troops) {
        this.name = name;
        countries = new ArrayList<>();
        bonusTroops = troops;
    }

    public void addCountry(Country country) {
        countries.add(country);
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public int getBonusTroops() {
        return bonusTroops;
    }
}
