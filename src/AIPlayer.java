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
}
