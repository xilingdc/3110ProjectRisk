import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private List<Country> countries = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void addCountry(Country c){
        countries.add(c);
    }

    public void removeCountry(Country c){
        countries.remove(c);
    }

    public void distributeTroops(int troops){
        while(troops != 0){
            for (Country c: countries){
                if (troops > 0){
                    c.addTroops(1);
                    troops--;
                }
            }
        }
    }

    public String getName(){
        return name;
    }

    public List<Country> getCountries(){
        return countries;
    }

}
