import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Xiling Wang
 * @author Aleksandar Veselinovic
 * @author Ali Fahd 
 */
public class Model implements Serializable {

    private ArrayList<Player> players;// player list
    private Player currentPlayer;//current player
    private int currentPlayerIndex, playerNum;//current player index number
    private Map map;
    private int[] troopAllocation = {50, 35, 30, 25, 20};
    private Color[] colorPlayer = {Color.red,Color.cyan,Color.green,Color.magenta, Color.orange, Color.pink};//setting up an array of color for each player
    private boolean placementPhase, fortifyPhase;
    private List<Views> viewLists;
    private int numberOfAttackDice, numberOfDefenceDice;
    private String customMapFileName;


    /**
     * @constructor
     */
    public Model() {
        map = new Map();
        viewLists=new ArrayList<>();
    }

    public Model(String mapFileName) {
        viewLists = new ArrayList<>();
        customMapFileName = mapFileName;
    }

    public void setCustomMap() {
        Map customMap = new Map(customMapFileName);
        while (!(customMap.isValid())) {
            for (Views view : viewLists) {
                String filename = view.getMapFileName();
                if (filename.equals("standard")) {
                    customMap = new Map();
                } else {
                    customMap = new Map(filename);
                }
            }
        }
        map = customMap;
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
     * @param view
     * add view into viewList
     */
    public void addView(View view){
        viewLists.add(view);
    }

    /**
     *
     * @param view
     * remove view from viewList
     */
    public void removeView(View view){
        viewLists.remove(view);
    }


    
    /**
     * the method prints the beginning of the game in view
     *
     * @param playerNum the number of players in the game
     */
    public void processBegin(int playerNum, int aiNum) {
        notifyMapSelection();

        pureProcessBegin(playerNum, aiNum);

        for (int i = 0; i < playerNum; i++) {
            for (Views view : viewLists) {
                view.addPlayer(new Event(this,"Player " + (i+1), colorPlayer[i]));
            }
        }
    }

    /**
     * the method sets up the beginning of the game
     * @param playerNum
     * @param aiNum
     */
    public void pureProcessBegin(int playerNum, int aiNum){
        players = new ArrayList<>();
        for (int i = 0; i < playerNum; i++) {
            Player p;
            if (i < playerNum - aiNum) {
                p = new Player(Integer.toString(i + 1), colorPlayer[i]);
            } else {
                p = new AIPlayer(Integer.toString(i + 1), colorPlayer[i], this);
            }
            players.add(p);//add player1, player2.... into playerList
        }
        currentPlayerIndex = 0;//the game begins at player one
        currentPlayer = players.get(0);
        setUp();
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
     * get the input number
     * @param str
     * @param min
     * @param max
     * @return
     */
    public int getNumber(String str, int min, int max){
        int userInput=-1;
        for (Views view : viewLists) {
            userInput = view.getNumberFromDiolog(new Event(this, str, min, max));
        }
        return userInput;
    }


    /**
     * This is the attack method, outcomes of battles are determined here, the map/countries are notified to be updated here
     * @param attacker - country attacking
     * @param defender - country defending
     */
    public void attack(Country attacker, Country defender) {//command description "attack defendCountry attackCountry" second command represents the country will be attacked, third command represents the country will launch attack.
        Integer attackDice[],  defendDice[];
        Player attackingPlayer = attacker.getOwner();
        Player defendingPlayer = defender.getOwner();
        boolean attackerAI = attackingPlayer instanceof AIPlayer;
        boolean defenderAI = defendingPlayer instanceof AIPlayer;
        numberOfAttackDice=0;
        numberOfDefenceDice=0;

        attackDice = attackerDice(attackerAI, defender, attacker);
        defendDice = defenderDice(defenderAI, defender, defendingPlayer, attackDice);
        //output results of battle
        Arrays.sort(attackDice, Collections.reverseOrder());//sort in descending order
        Arrays.sort(defendDice, Collections.reverseOrder());//sort in descending order
        int lessDice = Math.min(attackDice.length, defendDice.length);
        for (int i = 0; i < lessDice; i++) {//loop through dice
            String[] message = pureAttack(attacker,defender,attackDice,defendDice,i);
            sendMessage(message[0]);//notify view to show message
            sendMessage(message[1]);//notify view to show message
        }

        updateAttackMapChanges(defender,attacker, defendingPlayer,attackingPlayer, defenderAI, attackerAI, numberOfAttackDice);

        checkWinner();
    }

    /**
     * attack mechanism
     * @param attacker
     * @param defender
     * @param attackDice
     * @param defendDice
     * @param round
     * @return
     */
    public String[] pureAttack(Country attacker, Country defender, Integer attackDice[], Integer defendDice[], int round){
            String[] message= new String[2];
            message[0]= "Attack Dice: " + attackDice[round] + "   Defence dice: " + defendDice[round];
            if (attackDice[round] > defendDice[round]) {//if attacking dice wins
                defender.removeTroops(1);
                message[1] = "Attacker wins";
            } else {//if defending dice wins
                attacker.removeTroops(1);
                message[1] = "Defender wins";
            }

            return message;
    }






    /**
     * preparation for defender side
     * @param defenderAI
     * @param defender
     * @param defendingPlayer
     * @param attackDice
     * @return
     */
    private Integer[] defenderDice(Boolean defenderAI, Country defender, Player defendingPlayer, Integer[] attackDice){
        Integer defendDice[];
        if (defender.getArmySize() == 1) {
            sendMessage("Defending country will get 1 dice");
            defendDice = new Integer[1];
        } else {
            if (defenderAI) {//if the defender is an AI
                numberOfDefenceDice = 2;//use the maximum number of dice
                sendMessage("Player " + defendingPlayer.getName() + " will use " + numberOfDefenceDice + " dice");
            } else {
                numberOfDefenceDice = getNumber("Defending player, how many dice do you want to play?", 1, 2);//notifies view to get number of dice
            }
            defendDice = new Integer[numberOfDefenceDice];
        }

        for (int i = 0; i < attackDice.length; i++) {
            attackDice[i] = ThreadLocalRandom.current().nextInt(1, 7);//roll the dice
        }

        for (int i = 0; i < defendDice.length; i++) {
            defendDice[i] = ThreadLocalRandom.current().nextInt(1, 7);//roll the dice
        }

        return defendDice;
    }





    /**
     * preparation for attacker side
     * @param attackerAI
     * @param defender
     * @param attacker
     * @return
     */
    private Integer[] attackerDice(Boolean attackerAI, Country defender, Country attacker){
        Integer attackDice[];
        if (attackerAI) {//if the attacker is an AI
            sendMessage("Player " + currentPlayer.getName() + " is attacking " + defender.getName() + " from " + attacker.getName());//show their attack decision
        }
        if (attacker.getArmySize() == 2) {
            sendMessage("Attacking country will get 1 dice");//notifies view to show message
            attackDice = new Integer[1];
        } else if (attacker.getArmySize() == 3) {
            if (attackerAI) {//if the attacker is an AI
                numberOfAttackDice = 2;//use the maximum number of dice
                sendMessage("Player " + currentPlayer.getName() + " will use " + numberOfAttackDice + " dice");
            } else {
                numberOfAttackDice = getNumber("Attacking player, how many dice do you want to play?", 1, 2);//notifies view to get number of dice
            }
            attackDice = new Integer[numberOfAttackDice];
        } else {
            if (attackerAI) {
                numberOfAttackDice = 3;
                sendMessage("Player " + currentPlayer.getName() + " will use " + numberOfAttackDice + " dice");
            } else {
                numberOfAttackDice = getNumber("Attacking player, how many dice do you want to play?", 1, 3);//notifies view to get number of dice
            }
            attackDice = new Integer[numberOfAttackDice];
        }

        return attackDice;
    }

    public void notifyMapSelection() {
        for (Views view : viewLists) {
            view.handleCustomMap(map.getFilename());
        }
    }


    /**
     * update any changes in map after player finish a single attack
     * @param defender
     * @param attacker
     * @param defendingPlayer
     * @param attackingPlayer
     * @param defenderAI
     * @param attackerAI
     * @param numberOfAttackDice
     */
    private void updateAttackMapChanges(Country defender, Country attacker, Player defendingPlayer, Player attackingPlayer, Boolean defenderAI, Boolean attackerAI, int numberOfAttackDice){
        //output any changes to the map
        if (defender.getArmySize() == 0) {
            sendMessage("Player " + currentPlayer.getName() + " captured " + defender.getName());//notify view to show message

            currentPlayer.addCountry(defender);
            defendingPlayer.removeCountry(defender);//remove captured country from defending country owner's country list

            if(defendingPlayer.getCountries().size()==0){//if defending country's owner does not have any other country
                sendMessage("Player " + defendingPlayer.getName() + " has been eliminated.");//notify view to show message
                players.remove(defendingPlayer);//remove defending player from player list
            }
            defender.setOwner(currentPlayer);// update new owner
            int movingTroops;
            if (attackerAI) {
                AIPlayer player = (AIPlayer) currentPlayer;
                //get the AI's choice for troops to move
                movingTroops = player.chooseNumberOfTroops(attacker.getArmySize() - 1);
            } else {
                movingTroops = getNumber("Player " + currentPlayer.getName() + ", how many troops do you want to move to your new country?", numberOfAttackDice, attacker.getArmySize() - 1);
            }
            defender.addTroops(movingTroops);//move all but 1 troop to new country
            attacker.removeTroops(movingTroops);//leave 1 troop in attacking country
            updateCountryButton(defender, attackingPlayer.getColor(), defender.getArmySize());//update view (button color)
        } else {
            updateCountryButton(defender, defendingPlayer.getColor(), defender.getArmySize());//update view (number on button)
        }
        updateCountryButton(attacker, attackingPlayer.getColor(), attacker.getArmySize());//update view (number on button)

    }



    /**
     * check if there is winner
     */
    public void checkWinner(){
        if (players.size() == 1) {
            for (Views view : viewLists) {
                view.showEndMessage(new Event(this,"Player "+players.get(0).getName() + " is the winner. Game Over!"));
            }
//            view.showEndMessage("Player "+players.get(0).getName() + " is the winner. Game Over!");//notify view to show end message
        }
    }











    /**
     * update Country Button
     * @param country
     * @param color
     * @param troop
     */
    public void updateCountryButton(Country country, Color color, int troop){
        for (Views view : viewLists) {
            view.updateCountryButton(new Event(this, country, color, troop));
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
            sendMessage("You cannot attack a country you own.");//notify the view to show a message
            return false;
        }

        if (attacker.hasNeighbor(defender)) {//if attack country is neighbour country of attacked country
            sendMessage("Defending Country: " + defender.getName());//notify the view to show a message
            return true;
        } else {
            sendMessage("The country you selected is not a neighbor of " + attacker.getName());//notify the view to show a message
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
            sendMessage("You do not own this country");//notify the view to show a message
            return false;
        } else if (country.getArmySize() == 1) {
            sendMessage("This country does not have enough troops to attack");//notify the view to show a message
            return false;
        } else {
            sendMessage("Attacking Country: "+ country.getName());//notify the view to show a message
            return true;
        }
    }

    /**
     * pass to next player in view
     */
    public void pass() {
        purePass();
        for (Views view : viewLists) {
            view.updatePlayerTurnTextHandler(new Event(this,currentPlayer));
        }

        sendMessage("Player " + currentPlayer.getName() + " has " + bonusTroopCalculator() + " troops to place.");
        if (currentPlayer instanceof AIPlayer) {//if the current player is AI
            ((AIPlayer) currentPlayer).aiPlay(bonusTroopCalculator());//start their turn
        }
        updateBonusTroopView(bonusTroopCalculator());
    }


    /**
     * pass to next function
     */
    public void purePass(){
        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;//go back to first player
        } else {
            currentPlayerIndex++;//move on to next player
        }
        currentPlayer = players.get(currentPlayerIndex);
        fortifyPhase = false;
        placementPhase = true;
    }

    /**
     * send message to view
     * @param message
     */
    public void sendMessage(String message){
        for (Views view : viewLists) {
            view.showMessage(new Event(this,message));
        }
    }

    /**
     * Moves troops from one country to the other, then passes the turn.
     *
     * @param fromCountry the country sending troops
     * @param toCountry the country receiving troops
     */
    public void fortify(Country fromCountry, Country toCountry){
        int troops = 0;
        if (currentPlayer instanceof AIPlayer) {//if the current player is AI
            AIPlayer player = (AIPlayer) currentPlayer;
            //get the AI's choice of troops to move
            troops = player.chooseNumberOfTroops(fromCountry.getArmySize() - 1);
            sendMessage("Player " + currentPlayer.getName() + " is moving " + troops + " troops from " + fromCountry.getName() + " to " + toCountry.getName());
        } else {
            troops = getNumber("How many troops do you want to move?", 1, fromCountry.getArmySize() - 1);
        }
        pureFortify(fromCountry,toCountry,troops);
        updateCountryButton(fromCountry, currentPlayer.getColor(), fromCountry.getArmySize());
        updateCountryButton(toCountry, currentPlayer.getColor(), toCountry.getArmySize());//update the buttons
        pass();
    }

    /**
     * fortifying certain amount of troops from certain country to target country
     * @param fromCountry
     * @param toCountry
     * @param troops
     */
    public void pureFortify(Country fromCountry, Country toCountry, int troops){
        fromCountry.removeTroops(troops);
        toCountry.addTroops(troops);//move the troops
        fortifyPhase = false;
    }

    /**
     * Determines if the country can send troops
     * for fortifying.
     *
     * @param country the country to check
     * @return true if the country can send troops, false otherwise
     */
    public boolean isFortifying(Country country) {
        if (!(currentPlayer.getCountries().contains(country))) {//if the player doesn't own the country
            sendMessage("You do not own this country");//notify the view to show a message
            return false;
        } else if (country.getArmySize() == 1) {
            sendMessage("This country does not have enough troops to fortify.");//notify the view to show a message
            return false;
        } else {
            sendMessage("Fortifying from Country: "+ country.getName());//notify the view to show a message
            return true;
        }
    }


    /**
     *
     * @param troop
     */
    public void updateBonusTroopView(int troop){
        for (Views view : viewLists) {
            view.updatePlaceNum(new Event(this, troop));
        }
    }



    /**
     * checks to see if the country can fortify to the other country
     * @param fromCountry the country sending troops
     * @param toCountry the country receiving troops
     * @return
     */
    public boolean canFortify(Country fromCountry, Country toCountry){
        ArrayList<Country> visited = new ArrayList<>();
        ArrayList<Country> deadEnd = new ArrayList<>();
        if (!currentPlayer.getCountries().contains(toCountry)) {//if the player attacks their own country
            sendMessage("You cannot fortify to a country you do not own.");//notify the view to show a message
            return false;
        }else{
                visited.add(fromCountry);
                boolean result = findPath(fromCountry, toCountry, visited, deadEnd);
                if(result){
                    sendMessage("Fortifying to Country: " + toCountry.getName());//notify the view to show a message
                    return true;
                }else{
                    if (!(currentPlayer instanceof AIPlayer)) {
                        sendMessage("The country you selected is not connected to " + fromCountry.getName());//notify the view to show a message
                    }
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
     * Places bonus troops in the country
     *
     * @param newTroops the number of troops that can be placed
     * @param country the country to receive troops
     */
    public int troopPlacement(int newTroops, Country country) {

        if(isPlaceable(country)) {
            int input = 0;
            if (currentPlayer instanceof AIPlayer) {//if the current player is AI
                input = 1;//add 1 troop
            } else {
                input = getNumber("How many troops do you want to place here?", 1, newTroops);
            }

            newTroops = purePlacingTroop(newTroops,input,country);
            updateCountryButton(country, currentPlayer.getColor(), country.getArmySize());
            updateBonusTroopView(newTroops);
            if (newTroops == 0) {//once there are no more troops to place
                placementPhase = false;//end the placement phase
                sendMessage("Placement Phase is done, Attack Phase has begun!");
            } else {
                return newTroops;
            }
            return 0;
        }else{
            return newTroops;
        }
    }

    /**
     * placing troops
     * @param newTroops
     * @param input
     * @param country
     * @return
     */
    public int purePlacingTroop(int newTroops, int input, Country country){
        country.addTroops(input);
        newTroops -= input;
        return newTroops;
    }


    /**
     * Determines if the country can receive troops in
     * the bonus troop placement phase.
     *
     * @param country the country to check
     * @return true if the country can receive troops, false otherwise
     */
    public boolean isPlaceable(Country country){
        if(country.getOwner().equals(currentPlayer)){
            return true;
        }else{
            sendMessage("Placement can not be done (country is not yours)");
            return false;
        }
    }


    /**
     * assigns bonus troops based on territories and continents conquered
     * @return the number of bonus troops the player will get
     */
    public int bonusTroopCalculator() {
        int bonusTroops = 3;
        for (Continent continent : map.getContinents()) {
            if (currentPlayer.getCountries().containsAll(continent.getCountries())) {
                bonusTroops += continent.getBonusTroops();
            }
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

    public void deactiveFortify(){
        fortifyPhase = false;
    }

    public void deactivatePlacement(){
        placementPhase = false;
    }

    /**
     * Starts the placement phase of the first turn
     */
    public void activatePlacement(){
        placementPhase = true;
        sendMessage("Player " + getCurrentPlayer().getName() + " has " + bonusTroopCalculator() + " troops to place.");
        if (currentPlayer instanceof AIPlayer) {//if the first player is AI
            ((AIPlayer) currentPlayer).aiPlay(bonusTroopCalculator());//start their turn
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

    /*
    public void save(String filename) throws IOException {
        filename += ".txt";
        FileOutputStream fileStream = new FileOutputStream(filename);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
        outputStream.writeObject(this);
        outputStream.close();
    }

    public static Model load(String filename) throws Exception {
        filename += ".txt";
        FileInputStream stream = new FileInputStream(filename);
        ObjectInputStream inputStream = new ObjectInputStream(stream);
        Model model = (Model) inputStream.readObject();
        inputStream.close();
        return model;
    }*/

    public void save(String filename) throws IOException{
        filename += ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(toSaveXML());
        writer.close();
    }

    public String toSaveXML() {
        String xml = "<Model>";
        for (Player p : players) {
            xml += "\n\t" + p.toSaveXML();
        }
        xml += "\n\t<playerturn>"+currentPlayer.getName()+"</playerturn>";
        xml += "\n\t<placementphase>"+placementPhase+"</placementphase>";
        xml += "\n\t<fortifyphase>"+fortifyPhase+"</fortifyphase>";
        if (customMapFileName == null){
            xml += "\n\t<custom>none</custom>";
        }else{
            xml += "\n\t<custom>"+customMapFileName+"</custom>";
        }
        xml += "\n</Model>";
        return xml;
    }

    public void loadGame(String filename) throws Exception{
        players.clear();
        Model m = this;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser s = spf.newSAXParser();
        File f = new File(filename);

        DefaultHandler dh = new DefaultHandler() {
            String aiplayer = "";
            String name = "";
            String color = "";
            String countryname = "";
            String troop = "";
            String playerturn = "";
            String placement = "";
            String fortify = "";
            boolean isAiplayer = false;
            boolean isName = false;
            boolean isColor = false;
            boolean isCountryname = false;
            boolean isTroop = false;
            boolean isPlayerturn = false;
            boolean isPlacement = false;
            boolean isFortify = false;
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equals("aiplayer")) {
                    isAiplayer = true;
                    isTroop = false;
                }else if (qName.equals("name")) {
                    isName = true;
                    isAiplayer = false;
                } else if (qName.equals("color")) {
                    isColor = true;
                    isName = false;
                } else if (qName.equals("countryname")) {
                    isCountryname = true;
                    isTroop = false;
                    isColor = false;
                } else if (qName.equals("troop")) {
                    isTroop = true;
                    isCountryname = false;
                }else if (qName.equals("playerturn")) {
                    isPlayerturn = true;
                }else if (qName.equals("placementphase")) {
                    isPlacement = true;
                    isPlayerturn = false;
                }else if (qName.equals("fortifyphase")) {
                    isFortify = true;
                    isPlacement = false;
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if (qName.equals("color")) {
                    Color colour = new Color(Integer.parseInt(color));
                    if(aiplayer.equals("true")){
                        players.add(new AIPlayer(name, colour, m));
                        currentPlayer = players.get(players.size()-1);
                    }else{
                        players.add(new Player(name, colour));
                        currentPlayer = players.get(players.size()-1);
                    }
                }else if(qName.equals("countryname")) {
                    currentPlayer.addCountry(map.getCountry(countryname));
                    troop = "";
                }else if(qName.equals("troop")) {
                    Country c = currentPlayer.getCountries().get(currentPlayer.getCountries().size()-1);
                    c.removeTroops(c.getArmySize());
                    c.addTroops(Integer.parseInt(troop));
                }else if(qName.equals("country")) {
                    countryname = "";
                }else if(qName.equals("player")) {
                    aiplayer = "";
                    name = "";
                    color = "";
                    isName = false;
                    isTroop = false;
                    isCountryname = false;
                    isColor = false;
                }else if(qName.equals("playerturn")) {
                    for (Player p: players){
                        if(p.getName().equals(playerturn)){
                            currentPlayer = p;
                            break;
                        }
                    }
                }else if(qName.equals("placementphase")) {
                    if(placement.equals("true")){
                        activatePlacement();
                        for (Views view : viewLists) {
                            view.updatePlayerTurnTextHandler(new Event(m,currentPlayer));
                            view.updatePlaceNum(new Event(m, 0));
                        }
                    }else{
                        deactivatePlacement();
                    }
                }else if(qName.equals("fortifyphase")) {
                    if(fortify.equals("true")){
                        activateFortify();
                        for (Views view : viewLists) {
                            view.updatePlayerTurnTextHandler(new Event(m,currentPlayer));
                            view.updatePlaceNum(new Event(m, 0));
                        }
                    }else{
                        deactiveFortify();
                    }
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                String string = new String(ch, start, length);
                if (isPlayerturn) {
                    if (playerturn.isEmpty()) playerturn = string;
                } else if (isPlacement) {
                    if (placement.isEmpty()) placement = string;
                } else if (isFortify) {
                    if (fortify.isEmpty()) fortify = string;
                } else if (isAiplayer) {
                    if (aiplayer.isEmpty()) aiplayer = string;
                } else if (isName) {
                    if (name.isEmpty()) name = string;
                } else if (isColor) {
                    if (color.isEmpty()) color = string;
                } else if (isCountryname) {
                    if (countryname.isEmpty()) countryname = string;
                } else if (isTroop) {
                    if (troop.isEmpty()) troop = string;
                }
            }
        };
        s.parse(f, dh);
        for(Player p: players){
            for(Country c: p.getCountries()){
                c.setOwner(p);
                updateCountryButton(c, p.getColor(), c.getArmySize());
            }
        }
    }
}
