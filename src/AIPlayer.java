import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Aleksandar Veselinovic
 * @author Xiling
 */
public class AIPlayer extends Player {
    private Model model;

    /**
     *Constructor of AIPlayer objects
     *
     * @param name the name of the player
     * @param color the color of the player
     * @param model the model the player uses
     */
    public AIPlayer(String name, Color color, Model model) {
        super(name, color);//use the constructor of Player
        this.model = model;
    }

    /**
     * Does the AI player's turn
     *
     * @param bonusTroops the bonus troops the player has to place
     */
    public void aiPlay(int bonusTroops) {
        aiPlaceTroops(bonusTroops);//place the bonus troops
        aiAttack();//attack
        aiFortify();//fortify
    }

    /**
     * The AI player's attack mechanism has two modes:
     * normal mode will attack weaker countries when the player has less counties
     * berserker mode will attack any enemy no matter how strong or weak when the
     * player owns more than 23 countries
     */
    public void aiAttack(){
        boolean berserkerMode = false;
        if(countries.size()>23){//if the player owns more than 23 countries
            berserkerMode=true;//activate berseker mode
        }
        ArrayList<Country> attackers = new ArrayList<>();//list of countries to attack with
        ArrayList<Country> defenders = new ArrayList<>();//list of countries to attack
        for (Country country : countries) {//loop through the player's countries
            for (Country neighbor : country.neighbours()) {//loop through each country's neighbours
                if (!(countries.contains(neighbor))) {//if the neighbour is an enemy
                    int difference = country.compareTroops(neighbor);//find the difference in troops between the 2 countries
                    if (difference > 1||berserkerMode) {//if the country has at least 2 more troops than the neighbour or berseker mode is activated
                        attackers.add(country);//add the country to the attacking list
                        defenders.add(neighbor);//add the neighbor to the defending list
                    }
                }
            }
        }
        for (int i = 0; i < attackers.size(); i++) {//loop through the attacking countries
            Country attacker = attackers.get(i);
            Country defender = defenders.get(i);//get the attacking match up
            //verify that the attack is still viable
            while (!(countries.contains(defender))&&(attacker.compareTroops(defender) > 1||berserkerMode&&attacker.getArmySize()>1)) {
                model.attack(attacker, defender);
            }
        }
        berserkerMode=false;
    }


    /**
     * Returns the AI's decision of troops being moved
     *
     * @param maxTroops the highest number of troops that can be moved
     * @return the number of troops being moved
     */
    public int chooseNumberOfTroops(int maxTroops) {
        return (maxTroops + 1)/2;//move approximately half the troops
    }

    /**
     * Returns a list of countries with 1 or 2 troops that can be attacked,
     * or any country that can be attacked if berseker mode is activated
     *
     * @return the list of vulnerable countries
     */
    private ArrayList<Country> getVulnerableCountries() {
        boolean berserkerMode = false;
        if(countries.size()>20){//if the player has more than 20 countries
            berserkerMode=true;//activate berseker mode
        }
        ArrayList<Country> vulnerableCountries = new ArrayList<>();
        for (Country country : countries) {//loop through the player's countries
            int i = 0;
            boolean safe = true;
            List<Country> neighbours = country.neighbours();//get the countries neighbours
            while (safe && i < neighbours.size()) {//while the country is safe and there are more neighbours to check
                if (!(countries.contains(neighbours.get(i)))) {//if the country can be attacked
                    if (country.getArmySize() == 1) {//if the country has 1 troop
                        vulnerableCountries.add(0, country);//add the country to the front of the list
                        safe = false;//exit the while loop
                    } else if (country.getArmySize() == 2) {//ifthe country has 2 troops
                        vulnerableCountries.add(country);//add the country to the end of the list
                        safe = false;//exit the while loop
                    } else if(berserkerMode){//if berseker mode is activated
                        vulnerableCountries.add(0,country);//add the country to the front of the list
                        safe = false;//exit the while loop
                    }
                }
                i++;//increment the index of the neighbors list
            }
        }
        berserkerMode=false;
        return vulnerableCountries;
    }

    /**
     * The AI has two modes for placing troops:
     * normal mode: it will place troops to the most vulnerable countries first
     * berserker mode: it will place troops to countries that have enemy nearby
     * @param bonusTroops the number of troops to place
     */
    public void aiPlaceTroops(int bonusTroops){

        ArrayList<Country> vulnerableCountries = getVulnerableCountries();//get the vulnerable countries
        int i = 0;
        if (vulnerableCountries.isEmpty()) {//if there are no vulnerable countries
            while (bonusTroops > 0) {//while there are still troops to place
                bonusTroops = model.troopPlacement(bonusTroops, countries.get(i));//add troops randomly
                i = (i + 1) % countries.size();//increment the index
            }
        } else {//if there are vulnerable countries
            while (bonusTroops > 0) {//while there are still troops to place
                bonusTroops = model.troopPlacement(bonusTroops, vulnerableCountries.get(i));//add troops to the vulnerable country
                i = (i + 1) % vulnerableCountries.size();//increment the index
            }
        }
    }


    /**
     * Fortifies a weak country
     */
    public void aiFortify() {
        ArrayList<Country> vulnerableCountries = getVulnerableCountries();//get the vulnerable countries
        Country fromCountry = null;
        Country toCountry = null;
        if (!(vulnerableCountries.isEmpty())) {//if there are vulnerable countries
            boolean notAbleToFortify = true;//assume there is no path
            int i = 0;
            //while there is no path or there are more countries to check
            while (notAbleToFortify && i < vulnerableCountries.size()) {
                Country strongestCountry = getStrongestCountry(vulnerableCountries.get(i));//get the strongest connected country
                if (strongestCountry != null) {//if there is a strongest connected country
                    fromCountry = strongestCountry;
                    toCountry = vulnerableCountries.get(i);//store the countries for fortifying
                    notAbleToFortify = false;//exit the while loop
                }
                i++;//increment the index
            }
            if (notAbleToFortify) {//if there were no possible connections for fortifying
                model.pass();//pass the turn
            } else {//if there was a path for fortifyinh
                model.fortify(fromCountry, toCountry);//fortify with the stored countries
            }
        } else {//if there are no vulnerable countries
            model.pass();//pass the turn
        }
    }

    /**
     * Returns a strong country connected to weakestCountry.
     * If there are no possibilities, return null.
     *
     * @param weakestCountry the country to compare to
     * @return the strongest connected country to weakestCountry
     */
    private Country getStrongestCountry(Country weakestCountry) {
        ArrayList<Country> strongestCountries = new ArrayList<>();
        Country strongCountry = null;
        for (Country country : countries) {
            //if there is a country with at least 2 more troops than weakestCountry
            if (country.getArmySize() - weakestCountry.getArmySize() > 1) {
                strongestCountries.add(country);//add the country to the list of possible strong countries
            }
        }
        boolean notConnected = true;//assume there is no path
        int i = 0;
        //while there is no path or there are more countries to check
        while (notConnected && i < strongestCountries.size()) {
            //if there is a path between the strongest and weakest country
            if (model.canFortify(strongestCountries.get(i), weakestCountry)) {
                strongCountry = strongestCountries.get(i);//store the country as the strong country
                notConnected = false;//exit the while loop
            }
            i++;//increment the index
        }
        return strongCountry;//return the strongest country
    }

    public String toSaveXML(){
        String str="<player>";
        str +="\n\t\t<aiplayer>true</aiplayer>";
        str+="\n\t\t<name>"+getName()+"</name>";
        str+="\n\t\t<color>"+String.valueOf(getColor().getRGB())+"</color>";
        str+="\n\t\t<countries>";
        for (Country country : countries) {
            str+=country.toSaveXML();
        }
        str+="\n\t\t</countries>";
        str+="\n\t</player>";
        return str;
    }
}
