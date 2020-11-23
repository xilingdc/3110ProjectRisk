import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer extends Player {
    private Model model;

    public AIPlayer(String name, Color color, Model model) {
        super(name, color);
        this.model = model;
    }

    public void aiPlay(int bonusTroops) {
        aiPlaceTroops(bonusTroops);
        aiAttack();
        model.pass();
    }

    public void aiAttack(){
        ArrayList<Country> attackers = new ArrayList<>();
        ArrayList<Country> defenders = new ArrayList<>();
        for (Country country : countries) {
            for (Country neighbor : country.neighbours()) {
                if (!(countries.contains(neighbor))) {
                    int difference = country.compareTroops(neighbor);
                    if (difference > 1) {
                        attackers.add(country);
                        defenders.add(neighbor);
                    }
                }
            }
        }
        for (int i = 0; i < attackers.size(); i++) {
            Country attacker = attackers.get(i);
            Country defender = defenders.get(i);
            if (!(countries.contains(defender))) {
                if (attacker.compareTroops(defender) > 1) {
                    model.attack(attacker, defender);
                }
            }
        }
    }

    public int chooseNumberOfTroops(int maxTroops) {
        return (maxTroops + 1)/2;
    }

    private ArrayList<Country> getVulnerableCountries() {
        ArrayList<Country> vulnerableCountries = new ArrayList<>();
        for (Country country : countries) {
            if (country.getArmySize() < 2) {
                int i = 0;
                boolean safe = true;
                List<Country> neighbours = country.neighbours();
                while (safe && i < neighbours.size()) {
                    if (!(countries.contains(neighbours.get(i)))) {
                        vulnerableCountries.add(country);
                        safe = false;
                    }
                    i++;
                }
            }
        }
        return vulnerableCountries;
    }

    public void aiPlaceTroops(int bonusTroops){
        ArrayList<Country> vulnerableCountries = getVulnerableCountries();
        int i = 0;
        if (vulnerableCountries.isEmpty()) {
            while (bonusTroops > 0) {
                bonusTroops = model.troopPlacement(bonusTroops, countries.get(i));
                i = (i + 1) % countries.size();
            }
        } else {
            while (bonusTroops > 0) {
                bonusTroops = model.troopPlacement(bonusTroops, vulnerableCountries.get(i));
                i = (i + 1) % vulnerableCountries.size();
            }
        }
    }

    public void aiFortify() {
        ArrayList<Country> vulnerableCountries = getVulnerableCountries();

        /*if(isCountriesIsolate(currentPlayer.getCountries())){
            System.out.println("fortifyable");
            Country fromCountry=null;
            Country toCountry=null;
            Random random = new Random();
            while(true){
                int tempSize = currentPlayer.getCountries().size();
                fromCountry = currentPlayer.getCountries().get(random.nextInt(tempSize));
                toCountry = currentPlayer.getCountries().get(random.nextInt(tempSize));
                if(AiCanFortify(fromCountry,toCountry)){
                    break;
                }
            }

            System.out.println("Player "+currentPlayer+" fortify troops from "+fromCountry.getName()+" to "+toCountry.getName());
            AiDoFortify(fromCountry,toCountry);

        }*/
    }

    private Country getMaxTroops() {
        int maxTroops = 0;
        Country bestDefended = null;
        for (Country country : countries) {
            if (country.getArmySize() > maxTroops) {
                maxTroops = country.getArmySize();
                bestDefended = country;
            }
        }
        return bestDefended;
    }


    /*public void aiDoFortify(Country fromCountry, Country toCountry){
        int MaxTroops = fromCountry.getArmySize() - 1;
        Random random = new Random();
        int tempTroop = random.nextInt(MaxTroops)+1;
        fromCountry.removeTroops(tempTroop);
        toCountry.addTroops(tempTroop);
        view.updateCountryButton(fromCountry, currentPlayer.getColor(), fromCountry.getArmySize());
        view.updateCountryButton(toCountry, currentPlayer.getColor(), toCountry.getArmySize());
        fortifyPhase = false;
    }*/


    /**
     * checks to see if the country can fortify to the other country
     */
    /*public boolean aiCanFortify(Country fromCountry, Country toCountry){
        ArrayList<Country> visited = new ArrayList<>();
        ArrayList<Country> deadEnd = new ArrayList<>();
        if(fromCountry.getArmySize()<2){
            return false;
        }
        visited.add(fromCountry);
        boolean result = findPath(fromCountry, toCountry, visited, deadEnd);
        if(result){

            return true;
        }else{

            return false;
        }

    }*/


    /**
     *
     * @param countries
     * @return true if the countries are isolated each other, false if the countries are connected somehow (not absolute connected)
     */
    /*public boolean isCountryIsolated(Country country){
        boolean connected = true;
        int i = 0;
        while (connected) {
            if (country.neighbours().get(i).getOwner()) {
                vulnerableCountries.add(country);
                safe = false;
            }
            i++;
        }
        for (Country country : countries) {
            for (){
                boolean isolate=country.hasNeighbor(countries.get(j));//true if the two countries are neighbor
                if(isolate){
                    return false;
                }
            }
        }
        return true;
    }*/
}
