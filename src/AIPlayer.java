import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer extends Player {
    private Model model;

    /**
     *
     * @param name
     * @param color
     * @param model
     */
    public AIPlayer(String name, Color color, Model model) {
        super(name, color);
        this.model = model;
    }

    /**
     *
     * @param bonusTroops
     */
    public void aiPlay(int bonusTroops) {
        aiPlaceTroops(bonusTroops);
        aiAttack();
        aiFortify();
    }

    /**
     * ai mode attack, two modes:
     * normal mode will choose attack the weaker country (when ai does not own a lot of counties).
     * berserker mode will choose attack any enemy no matter strong or weak (when ai owns more than 23 countries).
     */
    public void aiAttack(){
        boolean berserkerMode = false;
        if(countries.size()>23){
            berserkerMode=true;
        }
        ArrayList<Country> attackers = new ArrayList<>();
        ArrayList<Country> defenders = new ArrayList<>();
        for (Country country : countries) {
            for (Country neighbor : country.neighbours()) {
                if (!(countries.contains(neighbor))) {
                    int difference = country.compareTroops(neighbor);
                    if (difference > 1||berserkerMode) {
                        attackers.add(country);
                        defenders.add(neighbor);
                    }
                }
            }
        }
        for (int i = 0; i < attackers.size(); i++) {
            Country attacker = attackers.get(i);
            Country defender = defenders.get(i);
            while (!(countries.contains(defender))&&(attacker.compareTroops(defender) > 1||berserkerMode&&attacker.getArmySize()>1)) {
                model.attack(attacker, defender);
            }
        }
        berserkerMode=false;
    }


    /**
     *
     * @param maxTroops
     * @return
     */
    public int chooseNumberOfTroops(int maxTroops) {
        return (maxTroops + 1)/2;
    }

    /**
     *
     * @return
     */
    private ArrayList<Country> getVulnerableCountries() {
        boolean berserkerMode = false;
        if(countries.size()>20){
            berserkerMode=true;
        }
        ArrayList<Country> vulnerableCountries = new ArrayList<>();
        for (Country country : countries) {
            int i = 0;
            boolean safe = true;
            List<Country> neighbours = country.neighbours();
            while (safe && i < neighbours.size()) {
                if (!(countries.contains(neighbours.get(i)))) {
                    if (country.getArmySize() == 1) {
                        vulnerableCountries.add(0, country);
                        safe = false;
                    } else if (country.getArmySize() == 2) {
                        vulnerableCountries.add(country);
                        safe = false;
                    } else if(berserkerMode){
                        vulnerableCountries.add(0,country);
                        safe = false;
                    }
                }
                i++;
            }
        }
        berserkerMode=false;
        return vulnerableCountries;
    }

    /**
     * two modes for ai place troop:
     * normal mode: it will place troops to most vulnerable countries first
     * berserker mode: it will place troops to countries that have enemy nearby
     * @param bonusTroops
     */
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


    /**
     * ai fortify
     */
    public void aiFortify() {
        ArrayList<Country> vulnerableCountries = getVulnerableCountries();
        Country fromCountry = null;
        Country toCountry = null;
        if (!(vulnerableCountries.isEmpty())) {
            boolean notAbleToFortify = true;
            int i = 0;
            while (notAbleToFortify && i < vulnerableCountries.size()) {
                Country strongestCountry = getStrongestCountry(vulnerableCountries.get(i));
                if (strongestCountry != null) {
                    fromCountry = strongestCountry;
                    toCountry = vulnerableCountries.get(i);
                    notAbleToFortify = false;
                }
                i++;
            }
            if (notAbleToFortify) {
                model.pass();
            } else {
                model.fortify(fromCountry, toCountry);
            }
        } else {
            model.pass();
        }
    }

    /**
     * 
     * @param weakestCountry
     * @return
     */
    private Country getStrongestCountry(Country weakestCountry) {
        ArrayList<Country> strongestCountries = new ArrayList<>();
        Country strongCountry = null;
        for (Country country : countries) {
            if (country.getArmySize() - weakestCountry.getArmySize() > 1) {
                strongestCountries.add(country);
            }
        }
        boolean notConnected = true;
        int i = 0;
        while (notConnected && i < strongestCountries.size()) {
            if (model.canFortify(strongestCountries.get(i), weakestCountry)) {
                strongCountry = strongestCountries.get(i);
                notConnected = false;
            }
            i++;
        }
        return strongCountry;
    }
}
