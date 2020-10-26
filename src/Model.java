import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Xiling Wang
 * @author Aleksandar Veselinovic
 * @author Ali Fahd 
 */
public class Model {

    private int playerNum;//player total number
    private ArrayList<Player> players;// player list
    private Parser parser;
    private Player currentPlayer;//current player
    private int currentPlayerIndex;//current player index number 
    private Map map;
    private int[] troopAllocation = {50, 35, 30, 25, 20};

    /**
     * @constructor
     */
    public Model() {
        parser = new Parser();
        map = new Map();
    }

    public static void main(String[] args) {
        Model model = new Model();
        model.play();
    }


    /**
     * Main play routine.  Loops until end of play.
     */
    public void play() {
        printBegin();//let user enter player number
        setUp();
        


        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            processCommand(command);
            if (players.size() == 1) {//when only one player stands
                finished = true;
            }
        }
        //output winner
        System.out.println("Player "+players.get(0).getName() + " is winner. Game Over!");
    }

    /**
     * the method prints the beginning of the game
     */
    public void printBegin() {
        System.out.println("Welcome to Risk.");
        System.out.print("Please enter player number(2-6): ");
        playerNum = parser.getPlayerNum();
        players = new ArrayList<>();
        for (int i = 1; i <= playerNum; i++) {
            Player p = new Player(Integer.toString(i));
            players.add(p);//add player1, player2.... into playerList
            System.out.println("Player " + i + " has been added;");
        }

        currentPlayerIndex = 0;//the game begins at player one
        currentPlayer = players.get(0);
        System.out.println("It is Player " + currentPlayer.getName() + "'s turn");
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
                    countryCount--;
                }
            }
        }

        for (Player p : players) {
            p.distributeTroops(troopAllocation[players.size() - 2]);
        }
    }
    
    
    /**
    *print current state of whole map with country and its owner, troop size.
    */
    private void getState() {
        //loop for every country on the map
        for (Country country : map.getCountries()) {
            System.out.println(country.getName() + " is owned by Player " + country.getOwner().getName() + ", the number of troops: " + country.getArmySize());
        }
    }

    /**
     *evaluate user input
     * @param command - user's input
     */
    private void processCommand(Command command) {

        //if the command didn't exist
        if (command.isUnknown()) {
            System.out.println("Invalid Command.");
            return;
        }

        String commandWord = command.getCommandWord();

        //match the command to the appropriate method
        if (commandWord.equals("state")) {
            getState();
        } else if (commandWord.equals("attack")) {
            attack(command);
        } else if (commandWord.equals("pass")) {
            pass();
        }
    }


    /**
     * This is the attack method, outcomes of battles are printed here, the map/countries are updated here
     * @param command - which countries are to attack and defend
     */
    private void attack(Command command) {//command description "attack defendCountry attackCountry" second command represents the country will be attacked, third command represents the country will launch attack.
        if (isValidAttack(command)) {

            Country defendingCountry = map.getCountry(command.getSecondWord());//defend country
            Country attackingCountry = map.getCountry(command.getThirdWord());//attack country

            // take input from the user
            Integer attackDice[];
            if (attackingCountry.getArmySize() == 2) {
                attackDice = new Integer[1];
            }
            else if (attackingCountry.getArmySize() == 3) {
                System.out.print("Player " + currentPlayer.getName() + ", how many dice do you want to play?: ");
                int numberOfAttackDice = parser.getNumberOfDice(2);
                attackDice = new Integer[numberOfAttackDice];
            } else {
                System.out.print("Player " + currentPlayer.getName() + ", how many dice do you want to play?: ");
                int numberOfAttackDice = parser.getNumberOfDice(3);
                attackDice = new Integer[numberOfAttackDice];
            }

            Integer defendDice[];
            if (defendingCountry.getArmySize() == 1) {
                defendDice = new Integer[1];
            } else {
                System.out.print("Player " + defendingCountry.getOwner().getName() + ", how many dice do you want to play?: ");
                int numberOfDefenceDice = parser.getNumberOfDice(2);
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
                System.out.print("Attack Dice: " + attackDice[i] + "\tDefence dice: " + defendDice[i]);
                if (attackDice[i] > defendDice[i]) {
                    defendingCountry.removeTroops(1);
                    System.out.println("\tAttacker wins");
                } else {
                    attackingCountry.removeTroops(1);
                    System.out.println("\tDefender wins");
                }
            }

            //output any changes to the map
            if (defendingCountry.getArmySize() == 0) {
                System.out.println("Player " + currentPlayer.getName() + " captured " + defendingCountry.getName());

                currentPlayer.addCountry(defendingCountry);
                defendingCountry.getOwner().removeCountry(defendingCountry);//remove captured country from defending country owner's country list

                if(defendingCountry.getOwner().getCountries().size()==0){//if defending country's owner does not have any other country
                    System.out.println("Player " + defendingCountry.getOwner().getName() + " has been eliminated.");
                    players.remove(defendingCountry.getOwner());
                }
                defendingCountry.setOwner(currentPlayer);// update new owner

                defendingCountry.addTroops(attackingCountry.getArmySize() - 1);
                attackingCountry.removeTroops(attackingCountry.getArmySize() - 1);
            }
        }
    }


    /**
     * Check to see if the attack can be done based on the rules of the game
     * @param command
     * @return true/false (valid attack)
     */
    private boolean isValidAttack(Command command) {
        if (!command.hasSecondWord()) {//no second command
            System.out.println("Which country will be attacked?");
            return false;
        }
        if (!command.hasThirdWord()) {//no third command
            System.out.println("Which country will launch attack?");
            return false;
        }

        if (!(map.hasCountry(command.getSecondWord()))) {//second or third command are not included in country list
            System.out.println("There is no such country to defend.");
            return false;
        }

        if (!(map.hasCountry(command.getThirdWord()))) {//second or third command are not included in country list
            System.out.println("There is no such country to attack with.");
            return false;
        }

        Country defendingCountry = map.getCountry(command.getSecondWord());
        Country attackingCountry = map.getCountry(command.getThirdWord());

        if (!(currentPlayer.getCountries().contains(attackingCountry))) {//if player doesn't control that country
            System.out.println("Player " + currentPlayer.getName() + " does not have this country.");
            return false;
        }

        if (currentPlayer.getCountries().contains(defendingCountry)) {//if the player attacks their own country
            System.out.println("You cannot attack a country you own.");
            return false;
        }

        if (attackingCountry.getArmySize() == 1) {//if attack country's army is only 1 left
            System.out.println(command.getThirdWord() + "'s army number is not enough to attack.");
            return false;
        }

        if (attackingCountry.hasNeighbor(defendingCountry)) {//if attack country is neighbour country of attacked country
            return true;
        } else {
            System.out.println(defendingCountry.getName() + " is not a neighbor of " + attackingCountry.getName());
            return false;
        }
    }


    /**
     * pass to next player
     */
    private void pass() {
        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;//go back to first player
        } else {
            currentPlayerIndex++;//move on to next player
        }
        currentPlayer = players.get(currentPlayerIndex);
        System.out.println("It is Player " + currentPlayer.getName() + "'s turn");//print whose turn it is
    }


}
