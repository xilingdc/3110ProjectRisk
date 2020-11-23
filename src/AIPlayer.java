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
        aiFortify();
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
                    }
                }
                i++;
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
                notConnected = false;
                strongCountry = strongestCountries.get(i);
            }
            i++;
        }
        return strongCountry;
    }
}
