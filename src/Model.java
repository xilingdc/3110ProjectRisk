//import jdk.swing.interop.SwingInterOpUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
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
    private Color[] colorPlayer = {Color.red,Color.cyan,Color.green,Color.magenta, Color.orange, Color.pink};//setting up an array of color for each player
    private View view;
    private boolean placementPhase;
    private boolean fortifyPhase;
    private boolean isAiMode=false;



    /**
     * @constructor
     */
    public Model() {
        map = new Map();
    }
    
    /**
    *@return currentPlayer
    */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
    *@return player list: players
    */
    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    
    /**
    *@param view
    */
    public void setView(View view) {
        this.view = view;
    }
    
    
    
    /**
     * the method prints the beginning of the game
     *
     * @param playerNum the number of players in the game
     */
    public void processBegin(int playerNum, int aiNum) {
        players = new ArrayList<>();
        for (int i = 0; i < playerNum; i++) {
            Player p;
            if (i < playerNum - aiNum) {
                p = new Player(Integer.toString(i+1),colorPlayer[i]);
            } else {
                p = new AIPlayer(Integer.toString(i+1),colorPlayer[i], this);
            }
            players.add(p);//add player1, player2.... into playerList
            view.addPlayer("Player " + (i+1), colorPlayer[i]);//display players in the view
        }

        currentPlayerIndex = 0;//the game begins at player one
        currentPlayer = players.get(0);
    }
    
    /**
    *set up map, and randomly assign countries to each player
    */
    public void setUp() {
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
    
    
    /**
    *@return map
    */
    public Map getMap(){
        return map;
    }

    /**
     * This is the attack method, outcomes of battles are determined here, the map/countries are notified to be updated here
     * @param attacker - country attacking
     * @param defender - country defending
     */
    public void attack(Country attacker, Country defender) {//command description "attack defendCountry attackCountry" second command represents the country will be attacked, third command represents the country will launch attack.
        Integer attackDice[];
        int numberOfAttackDice, numberOfDefenceDice;
        Player attackingPlayer = attacker.getOwner();
        Player defendingPlayer = defender.getOwner();
        boolean attackerAI = attackingPlayer instanceof AIPlayer;
        boolean defenderAI = defendingPlayer instanceof AIPlayer;
        if (attackerAI) {
            view.showMessage("Player " + currentPlayer.getName() + " is attacking " + defender.getName() + " from " + attacker.getName());
        }
        if (attacker.getArmySize() == 2) {
            view.showMessage("Attacking country will get 1 dice");//notifies view to show message
            attackDice = new Integer[1];
        }
        else if (attacker.getArmySize() == 3) {
            if (attackerAI) {
                numberOfAttackDice = 2;
                view.showMessage("Player " + currentPlayer.getName() + " will use " + numberOfAttackDice + " dice");
            } else {
                numberOfAttackDice = view.getNumber("Attacking player, how many dice do you want to play?", 1, 2);//notifies view to get number of dice
            }
            attackDice = new Integer[numberOfAttackDice];
        } else {
            if (attackerAI) {
                numberOfAttackDice = 3;
                view.showMessage("Player " + currentPlayer.getName() + " will use " + numberOfAttackDice + " dice");
            } else {
                numberOfAttackDice = view.getNumber("Attacking player, how many dice do you want to play?", 1, 3);//notifies view to get number of dice
            }
            attackDice = new Integer[numberOfAttackDice];
        }

        Integer defendDice[];
        if (defender.getArmySize() == 1) {
            view.showMessage("Defending country will get 1 dice");
            defendDice = new Integer[1];
        } else {
            if (defenderAI) {
                numberOfDefenceDice = 2;
                view.showMessage("Player " + defendingPlayer.getName() + " will use " + numberOfDefenceDice + " dice");
            } else {
                numberOfDefenceDice = view.getNumber("Defending player, how many dice do you want to play?", 1, 2);//notifies view to get number of dice
            }
            defendDice = new Integer[numberOfDefenceDice];
        }

        for (int i = 0; i < attackDice.length; i++) {
            attackDice[i] = ThreadLocalRandom.current().nextInt(1, 7);//roll the dice
        }

        for (int i = 0; i < defendDice.length; i++) {
            defendDice[i] = ThreadLocalRandom.current().nextInt(1, 7);//roll the dice
        }

        //output results of battle
        Arrays.sort(attackDice, Collections.reverseOrder());//sort in descending order
        Arrays.sort(defendDice, Collections.reverseOrder());//sort in descending order
        int lessDice = Math.min(attackDice.length, defendDice.length);
        for (int i = 0; i < lessDice; i++) {//loop through dice
            String message = "Attack Dice: " + attackDice[i] + "   Defence dice: " + defendDice[i];
            if (attackDice[i] > defendDice[i]) {//if attcking dice wins
                defender.removeTroops(1);
                message += "   Attacker wins";
            } else {//if defending dice wins
                attacker.removeTroops(1);
                message += "   Defender wins";
            }
            view.showMessage(message);//notify view to show message
        }

        //output any changes to the map
        if (defender.getArmySize() == 0) {
            view.showMessage("Player " + currentPlayer.getName() + " captured " + defender.getName());//notify view to show message

            currentPlayer.addCountry(defender);
            defendingPlayer.removeCountry(defender);//remove captured country from defending country owner's country list

            if(defendingPlayer.getCountries().size()==0){//if defending country's owner does not have any other country
                view.showMessage("Player " + defendingPlayer.getName() + " has been eliminated.");//notify view to show message
                players.remove(defendingPlayer);//remove defending player from player list
            }
            defender.setOwner(currentPlayer);// update new owner
            int movingTroops;
            if (attackerAI) {
                AIPlayer player = (AIPlayer) currentPlayer;
                movingTroops = player.chooseNumberOfTroops(attacker.getArmySize() - 1);
            } else {
                movingTroops = view.getNumber("Player " + currentPlayer.getName() + ", how many troops do you want to move to your new country?", 1, attacker.getArmySize() - 1);
            }
            defender.addTroops(movingTroops);//move all but 1 troop to new country
            attacker.removeTroops(movingTroops);//leave 1 troop in attacking country
            view.updateCountryButton(defender, attackingPlayer.getColor(), defender.getArmySize());//update view (button color)
        } else {
            view.updateCountryButton(defender, defendingPlayer.getColor(), defender.getArmySize());//update view (number on button)
        }
        view.updateCountryButton(attacker, attackingPlayer.getColor(), attacker.getArmySize());//update view (number on button)
        if (players.size() == 1) {
            view.showEndMessage("Player "+players.get(0).getName() + " is the winner. Game Over!");//notify view to show end message
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
            view.showMessage("You cannot attack a country you own.");//notify the view to show a message
            return false;
        }

        if (attacker.hasNeighbor(defender)) {//if attack country is neighbour country of attacked country
            view.showMessage("Defending Country: " + defender.getName());//notify the view to show a message
            return true;
        } else {
            view.showMessage("The country you selected is not a neighbor of " + attacker.getName());//notify the view to show a message
            return false;
        }
    }

    /**
     * Check if the country is able to attack
     *
     * @param country attacking country
     * @return true/false (country can attack)
     */
    public boolean isAttacker(Country country) {
        if (!(currentPlayer.getCountries().contains(country))) {//if the player doesn't own the country
            view.showMessage("You do not own this country");//notify the view to show a message
            return false;
        } else if (country.getArmySize() == 1) {
            view.showMessage("This country does not have enough troops to attack");//notify the view to show a message
            return false;
        } else {
            view.showMessage("Attacking Country: "+ country.getName());//notify the view to show a message
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

        fortifyPhase = false;
        placementPhase = true;
        view.showMessage("Player " + currentPlayer.getName() + " has " + bonusTroopCalculator() + " troops to place.");
        if (currentPlayer instanceof AIPlayer) {
            ((AIPlayer) currentPlayer).aiPlay(bonusTroopCalculator());
        }
    }

    public void fortify(Country fromCountry, Country toCountry){
        int troops = view.getNumber("How many troops do you want to move?", 1, fromCountry.getArmySize() - 1);
        fromCountry.removeTroops(troops);
        toCountry.addTroops(troops);
        view.updateCountryButton(fromCountry, currentPlayer.getColor(), fromCountry.getArmySize());
        view.updateCountryButton(toCountry, currentPlayer.getColor(), toCountry.getArmySize());
        fortifyPhase = false;
        pass();
    }

    public boolean isFortifying(Country country) {
        if (!(currentPlayer.getCountries().contains(country))) {//if the player doesn't own the country
            view.showMessage("You do not own this country");//notify the view to show a message
            return false;
        } else if (country.getArmySize() == 1) {
            view.showMessage("This country does not have enough troops to fortify.");//notify the view to show a message
            return false;
        } else {
            view.showMessage("Fortifying from Country: "+ country.getName());//notify the view to show a message
            return true;
        }
    }

    /**
     * checks to see if the country can fortify to the other country
     */
    public boolean canFortify(Country fromCountry, Country toCountry){
        ArrayList<Country> visited = new ArrayList<>();
        ArrayList<Country> deadEnd = new ArrayList<>();
        if (!currentPlayer.getCountries().contains(toCountry)) {//if the player attacks their own country
            view.showMessage("You cannot fortify to a country you do not own.");//notify the view to show a message
            return false;
        }else{
                visited.add(fromCountry);
                boolean result = findPath(fromCountry, toCountry, visited, deadEnd);
                if(result){
                    view.showMessage("Fortifying to Country: " + toCountry.getName());//notify the view to show a message
                    return true;
                }else{
                    view.showMessage("The country you selected is not connected to " + fromCountry.getName());//notify the view to show a message
                    return false;
            }
        }
    }

    /**
     * checks to see if the country can fortify another country through a connected path
     */
    public boolean findPath(Country fromCountry, Country toCountry, ArrayList<Country> visited, ArrayList<Country> deadEnd){
        if(fromCountry.neighbours().contains(toCountry)){
            return true;
        }
        for (Country c: fromCountry.neighbours()){
                if (c.getOwner().equals(currentPlayer)) {
                    if (!(visited.contains(c) || deadEnd.contains(c))) {
                        visited.add(c);
                        return findPath(c, toCountry, visited, deadEnd);
                    }
            }
        }
        deadEnd.add(fromCountry);
        visited.remove(fromCountry);
        if(visited.isEmpty()){
            return false;
        }
        return findPath(visited.get(visited.size()-1), toCountry, visited, deadEnd);
    }


    /**
     * player can place their bonus troops
     */
    public int troopPlacement(int newTroops, Country country) {

        if(isPlaceable(country)) {
            int input = 0;
            if (currentPlayer instanceof AIPlayer) {
                input = 1;
                view.showMessage("Player " + currentPlayer.getName() + " moved " + input + " troop to " + country.getName());
            } else {
                input = view.getNumber("How many troops do you want to place here?", 1, newTroops);
            }
            country.addTroops(input);
            view.updateCountryButton(country, currentPlayer.getColor(), country.getArmySize());
            newTroops -= input;
            if (newTroops == 0) {
                placementPhase = false;
                view.showMessage("Placement Phase is done, Attack Phase has begun!");
            } else {
                return newTroops;
            }
            return 0;
        }else{
            return newTroops;
        }
    }

    public boolean isPlaceable(Country country){
        if(country.getOwner().equals(currentPlayer)){
            return true;
        }else{
            view.showMessage("Placement can not be done (country is not yours)");
            return false;
        }
    }


    /**
     * assigns bonus troops based on territories and continents conquered
     */
    public int bonusTroopCalculator() {
        int bonusTroops = 3;
        if (currentPlayer.getCountries().containsAll(map.getAustralia())) {
            bonusTroops += 2;
        }
        if (currentPlayer.getCountries().containsAll(map.getAsia())) {
            bonusTroops += 7;
        }
        if (currentPlayer.getCountries().containsAll(map.getAfrica())) {
            bonusTroops += 3;//go back to first player
        }
        if (currentPlayer.getCountries().containsAll(map.getEurope())) {
            bonusTroops += 5;//go back to first player
        }
        if (currentPlayer.getCountries().containsAll(map.getNorthAmerica())) {
            bonusTroops += 5;//go back to first player
        }
        if (currentPlayer.getCountries().containsAll(map.getSouthAmerica())) {
            bonusTroops += 2;//go back to first player
        }

        int countriesConquered = currentPlayer.getCountries().size();
        if(countriesConquered > 11){
            bonusTroops += (countriesConquered / 3) - 3;
        }

        return bonusTroops;
    }


    public boolean isPlacementPhase(){ return placementPhase; }


    public boolean isFortifyPhase(){ return fortifyPhase; }

    public void activateFortify(){
        fortifyPhase = true;
    }

    public void activatePlacement(){
        placementPhase = true;
        view.showMessage("Player " + getCurrentPlayer().getName() + " has " + bonusTroopCalculator() + " troops to place.");
        if (currentPlayer instanceof AIPlayer) {
            ((AIPlayer) currentPlayer).aiPlay(bonusTroopCalculator());
        }
    }

    /**
     * for testing purposes
     */
    public Player getNextPlayer() {
        int nextPlayerIndex;
        if (currentPlayerIndex == players.size() - 1) {
            nextPlayerIndex = 0;//go back to first player
        } else {
            nextPlayerIndex = currentPlayerIndex++;//move on to next player
        }
        return players.get(nextPlayerIndex);
    }
}
