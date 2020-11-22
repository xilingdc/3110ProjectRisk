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
    public void processBegin(int playerNum) {
        players = new ArrayList<>();
        for (int i = 0; i < playerNum; i++) {
            Player p = new Player(Integer.toString(i+1),colorPlayer[i]);
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
    public void attack(Country attacker, Country defender, boolean isAI) {//command description "attack defendCountry attackCountry" second command represents the country will be attacked, third command represents the country will launch attack.
        Integer attackDice[];
        int numberOfAttackDice, numberOfDefenceDice;
        if (attacker.getArmySize() == 2) {
            view.showMessage("Attacking country will get 1 dice");//notifies view to show message
            attackDice = new Integer[1];
        }
        else if (attacker.getArmySize() == 3) {
            if (isAI) {
                numberOfAttackDice = 2;
            } else {
                numberOfAttackDice = view.getDice("Attacking player, how many dice do you want to play?", 2);//notifies view to get number of dice
            }
            attackDice = new Integer[numberOfAttackDice];
        } else {
            if (isAI) {
                numberOfAttackDice = 3;
            } else {
                numberOfAttackDice = view.getDice("Attacking player, how many dice do you want to play?", 3);//notifies view to get number of dice
            }
            attackDice = new Integer[numberOfAttackDice];
        }

        Integer defendDice[];
        if (defender.getArmySize() == 1) {
            view.showMessage("Defending country will get 1 dice");
            defendDice = new Integer[1];
        } else {
            if (isAI) {
                numberOfDefenceDice = 2;
            } else {
                numberOfDefenceDice = view.getDice("Defending player, how many dice do you want to play?", 2);//notifies view to get number of dice
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
            defender.getOwner().removeCountry(defender);//remove captured country from defending country owner's country list

            if(defender.getOwner().getCountries().size()==0){//if defending country's owner does not have any other country
                view.showMessage("Player " + defender.getOwner().getName() + " has been eliminated.");//notify view to show message
                players.remove(defender.getOwner());//remove defending player from player list
            }
            defender.setOwner(currentPlayer);// update new owner
            int movingTroops = view.getTroops("Player " + currentPlayer.getName() + ", how many troops do you want to move to your new country?", attacker.getArmySize());
            defender.addTroops(movingTroops);//move all but 1 troop to new country
            attacker.removeTroops(movingTroops);//leave 1 troop in attacking country
            view.updateCountryButton(defender, attacker.getOwner().getColor(), defender.getArmySize());//update view (button color)
        } else {
            view.updateCountryButton(defender, defender.getOwner().getColor(), defender.getArmySize());//update view (number on button)
        }
        view.updateCountryButton(attacker, attacker.getOwner().getColor(), attacker.getArmySize());//update view (number on button)
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
//        if(!isAiMode||currentPlayer.equals(players.get(0))) {
            view.showMessage("Player " + currentPlayer.getName() + " has " + bonusTroopCalculator() + " troops to place.");
//        }
    }

    public void fortify(Country fromCountry, Country toCountry){
        int troops = view.dropTroops("How many troops do you want to move?", fromCountry.getArmySize() - 1);
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
            int input = view.dropTroops("How many troops do you want to place here?", newTroops);
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

    public void AiPlay(){

        isAiMode=true;

        for (int i = 0; i < players.size()-1; i++) {


        //place troop
        AiPlaceTroop();
        //attack
        AiAttack();

        if(new Random().nextBoolean()){//fortify
            AiFortify();
            AiPass();
        }else{//pass
            AiPass();
        }

        }
    }

    public void AiPlaceTroop(){
        Random random = new Random();
        int countrySize = currentPlayer.getCountries().size();
        int bonusTroop = bonusTroopCalculator();
        int temp;
        while(true) {
            temp = random.nextInt(bonusTroop)+1;
            Country country = currentPlayer.getCountries().get(random.nextInt(countrySize));
            country.addTroops(temp);
            System.out.println(temp);//test only
            view.updateCountryButton(country, currentPlayer.getColor(), country.getArmySize());
            bonusTroop-=temp;
            if(bonusTroop==0){
                break;
            }
        }

        placementPhase = false;

    }

    public void AiAttack(){
        System.out.println("Attacking!");//test only
        //TODO implement attack function for computer player, the key point is make sure how to do the dice
        for (Country country : currentPlayer.getCountries()) {
            for (Country neighbor : country.neighbours()) {
                if (!(currentPlayer.getCountries().contains(neighbor))) {
                    int difference = country.compareTroops(neighbor);
                    if (difference > 1) {
                        attack(country, neighbor, true);
                    }
                }
            }
        }
    }

    public void AiFortify(){
        System.out.println("fortifying");
        if(isCountriesIsolate(currentPlayer.getCountries())){
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

        }
    }


    public void AiDoFortify(Country fromCountry, Country toCountry){
        int MaxTroops = fromCountry.getArmySize() - 1;
        Random random = new Random();
        int tempTroop = random.nextInt(MaxTroops)+1;
        fromCountry.removeTroops(tempTroop);
        toCountry.addTroops(tempTroop);
        view.updateCountryButton(fromCountry, currentPlayer.getColor(), fromCountry.getArmySize());
        view.updateCountryButton(toCountry, currentPlayer.getColor(), toCountry.getArmySize());
        fortifyPhase = false;
    }


    /**
     * checks to see if the country can fortify to the other country
     */
    public boolean AiCanFortify(Country fromCountry, Country toCountry){
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

    }


    /**
     *
     * @param countries
     * @return true if the countries are isolated each other, false if the countries are connected somehow (not absolute connected)
     */
    public boolean isCountriesIsolate(List<Country> countries){
        //TODO has bug, i don't know why it can search out whether the list is connected or isolated
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(0);
            for(int j=0; j<countries.size();j++){
                boolean isolate=country.hasNeighbor(countries.get(j));//true if the two countries are neighbor
                if(isolate){
                    return false;
                }
            }
        }
        return true;
    }

    public void AiPass(){
        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;//go back to first player
        } else {
            currentPlayerIndex++;//move on to next player
        }
        currentPlayer = players.get(currentPlayerIndex);
        view.updatePlayerTurnTextHandler(currentPlayer);

        fortifyPhase = false;
        placementPhase = true;

        if(currentPlayer.equals(players.get(0))){
            view.showMessage("Player " + currentPlayer.getName() + " has " + bonusTroopCalculator() + " troops to place.");
        }
    }


}
