import java.awt.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Xiling Wang
 * @author Aleksandar Veselinovic
 * @author Ali Fahd 
 */
public class Model {

    private ArrayList<Player> players;// player list
    private Player currentPlayer;//current player
    private int currentPlayerIndex;//current player index number 
    private Map map;
    private int[] troopAllocation = {50, 35, 30, 25, 20};
    private Color[] colorPlayer = {Color.red,Color.cyan,Color.green,Color.magenta, Color.orange, Color.pink};
    private View view;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * @constructor
     */
    public Model(int numberOfPlayers) {
        map = new Map();
        processBegin(numberOfPlayers);//let user enter player number
        setUp();
    }

    /**
     * the method prints the beginning of the game
     */
    private void processBegin(int playerNum) {
        players = new ArrayList<>();
        for (int i = 0; i < playerNum; i++) {
            Player p = new Player(Integer.toString(i+1),colorPlayer[i]);
            players.add(p);//add player1, player2.... into playerList
        }

        currentPlayerIndex = 0;//the game begins at player one
        currentPlayer = players.get(0);
    }
    
    /**
    *set up map, and randomly assign countries to each player
    */
    private void setUp() {
        Collections.shuffle(map.getCountries());//randomly shuffle countries so the distribution is different each time
        int countryCount = map.getNumberOfCountries();
        while (countryCount != 0) {
            for (Player p : players) {//assign them one by one to players,like deaing the country cards in the beginning of the game
                if (countryCount > 0) {
                    p.addCountry(map.getCountry(countryCount - 1));
                    map.getCountry(countryCount - 1).setOwner(p);
                    map.getCountry(countryCount-1).setColor(p.getColor());
                    countryCount--;
                }
            }
        }

        for (Player p : players) {
            int troops = troopAllocation[players.size() - 2];
            while(troops != 0){
                for (Country c : p.getCountries()){
                    if (troops > 0){
                        c.addTroops(1);
                        troops--;
                    }
                }
            }
        }
    }

    public Map getMap(){
        return map;
    }

    /**
     * This is the attack method, outcomes of battles are printed here, the map/countries are updated here
     * @param attacker - country attacking
     * @param defender - country defending
     */
    public void attack(Country attacker, Country defender) {//command description "attack defendCountry attackCountry" second command represents the country will be attacked, third command represents the country will launch attack.
        // take input from the user
        Integer attackDice[];
        if (attacker.getArmySize() == 2) {
            view.showMessage("Attacking country will get 1 dice");
            attackDice = new Integer[1];
        }
        else if (attacker.getArmySize() == 3) {
            int numberOfAttackDice = view.getDice("Attacking player, how many dice do you want to play?", 2);
            attackDice = new Integer[numberOfAttackDice];
        } else {
            int numberOfAttackDice = view.getDice("Attacking player, how many dice do you want to play?", 3);
            attackDice = new Integer[numberOfAttackDice];
        }

        Integer defendDice[];
        if (defender.getArmySize() == 1) {
            view.showMessage("Defending country will get 1 dice");
            defendDice = new Integer[1];
        } else {
            int numberOfDefenceDice = view.getDice("Defending player, how many dice do you want to play?", 2);
            defendDice = new Integer[numberOfDefenceDice];
        }

        for (int i = 0; i < attackDice.length; i++) {
            attackDice[i] = ThreadLocalRandom.current().nextInt(1, 7);
        }

        for (int i = 0; i < defendDice.length; i++) {
            defendDice[i] = ThreadLocalRandom.current().nextInt(1, 7);
        }

        //output results of battle
        Arrays.sort(attackDice, Collections.reverseOrder());
        Arrays.sort(defendDice, Collections.reverseOrder());
        int lessDice = Math.min(attackDice.length, defendDice.length);
        for (int i = 0; i < lessDice; i++) {
            String message = "Attack Dice: " + attackDice[i] + "   Defence dice: " + defendDice[i];
            if (attackDice[i] > defendDice[i]) {
                defender.removeTroops(1);
                message += "   Attacker wins";
            } else {
                attacker.removeTroops(1);
                message += "   Defender wins";
            }
            view.showMessage(message);
        }

        //output any changes to the map
        if (defender.getArmySize() == 0) {
            view.showMessage("Player " + currentPlayer.getName() + " captured " + defender.getName());

            currentPlayer.addCountry(defender);
            defender.getOwner().removeCountry(defender);//remove captured country from defending country owner's country list

            if(defender.getOwner().getCountries().size()==0){//if defending country's owner does not have any other country
                view.showMessage("Player " + defender.getOwner().getName() + " has been eliminated.");
                players.remove(defender.getOwner());
            }
            defender.setOwner(currentPlayer);// update new owner
            defender.addTroops(attacker.getArmySize() - 1);
            attacker.removeTroops(attacker.getArmySize() - 1);
            view.updateCountryButton(defender, attacker.getOwner().getColor(), defender.getArmySize());//update view (button color)
        } else {
            view.updateCountryButton(defender, defender.getOwner().getColor(), defender.getArmySize());
        }
        view.updateCountryButton(attacker, attacker.getOwner().getColor(), attacker.getArmySize());
        if (players.size() == 1) {
            view.showEndMessage("Player "+players.get(0).getName() + " is the winner. Game Over!");
        }
    }

    /**
     * Check to see if the attack can be done based on the rules of the game
     * @param attacker - country attacking
     * @param defender - country defending
     * @return true/false (valid attack)
     */
    public boolean canDefend(Country attacker, Country defender) {
        if (currentPlayer.getCountries().contains(defender)) {//if the player attacks their own country
            view.showMessage("You cannot attack a country you own.");
            return false;
        }

        if (attacker.hasNeighbor(defender)) {//if attack country is neighbour country of attacked country
            view.showMessage("Defending Country: " + defender.getName());
            return true;
        } else {
            view.showMessage("The country you selected is not a neighbor of " + attacker.getName());
            return false;
        }
    }

    public boolean isAttacker(Country country) {
        if (!(currentPlayer.getCountries().contains(country))) {
            view.showMessage("You do not own this country");
            return false;
        } else if (country.getArmySize() == 1) {
            view.showMessage("This country does not have enough troops to attack");
            return false;
        } else {
            view.showMessage("Attacking Country: "+ country.getName());
            return true;
        }
    }

    /**
     * pass to next player
     */
    public void pass() {
        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;//go back to first player
        } else {
            currentPlayerIndex++;//move on to next player
        }
        currentPlayer = players.get(currentPlayerIndex);
        view.updatePlayerTurnTextHandler(currentPlayer);
    }
}
