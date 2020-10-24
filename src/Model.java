import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Xiling Wang
 * @author Aleksandar Veselinovic
 */
public class Model {

    private int playerNum;
    private ArrayList<Player> players;
    private Parser parser;
    private Player currentPlayer;
    private int currentPlayerIndex;
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

        //need to modify countryArmy and playerCountry two hashmaps


        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            processCommand(command);
            if (players.size() == 1) {//when only one player stands
                finished = true;
            }
        }
        System.out.println(players.get(0) + " is winner. Gamer Over!");
    }

    /**
     * the method prints the beginning of the game
     */
    public void printBegin() {
        System.out.println("Welcome to Risk.");
        System.out.println("Please enter player number(2-6):");
        playerNum = parser.getPlayerNum();
        players = new ArrayList<>();
        for (int i = 1; i <= playerNum; i++) {
            Player p = new Player(Integer.toString(i));
            players.add(p);//add player1, player2.... into playerList
            System.out.println("Player " + i + " has been added;");
        }

        currentPlayerIndex = 0;
        currentPlayer = players.get(0);

        setUp();
    }

    public void setUp() {
        Collections.shuffle(map.getCountries());
        int countryCount = map.getNumberOfCountries();
        while (countryCount != 0) {
            for (Player p : players) {
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

    private void getState() {
        for (Country country : map.getCountries()) {
            System.out.println(country.getName() + " is owned by Player " + country.getOwner().getName() + ", the number of troops: " + country.getArmySize());
        }
    }


    private void processCommand(Command command) {

        if (command.isUnknown()) {
            System.out.println("Invalid Command.");
            return;
        }

        String commandWord = command.getCommandWord();

        if (commandWord.equals("state")) {
            getState();
        } else if (commandWord.equals("attack")) {
            attack(command);
        } else if (commandWord.equals("pass")) {
            pass();
        } else {

        }

    }


    /**
     * @param command
     */
    private void attack(Command command) {//command description "attack attackedCountry attackCountry" second command represents the country will be attacked, third command represents the country will launch attack.
        if (isValidAttack(command)) {

            Country defendingCountry = map.getCountry(command.getSecondWord());
            Country attackingCountry = map.getCountry(command.getThirdWord());

            // take input from the user
            Integer attackDice[];
            if (attackingCountry.getArmySize() <= 4) {
                attackDice = new Integer[attackingCountry.getArmySize() - 1];
            } else {
                attackDice = new Integer[3];
            }
            Integer defendDice[];
            if (defendingCountry.getArmySize() == 1) {
                defendDice = new Integer[1];
            } else {
                defendDice = new Integer[2];
            }

            for (int i = 0; i < attackDice.length; i++) {
                attackDice[i] = ThreadLocalRandom.current().nextInt(1, 7);
            }

            for (int i = 0; i < defendDice.length; i++) {
                defendDice[i] = ThreadLocalRandom.current().nextInt(1, 7);
            }

            Arrays.sort(attackDice, Collections.reverseOrder());
            Arrays.sort(defendDice, Collections.reverseOrder());
            int lessDice = Math.min(attackDice.length, defendDice.length);
            for (int i = 0; i < lessDice; i++) {
                if (attackDice[i] > defendDice[i]) {
                    defendingCountry.removeTroops(1);
                    System.out.println("Attacker won");
                } else {
                    attackingCountry.removeTroops(1);
                    System.out.println("Defender won");
                }
            }
        }
    }


    /**
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
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        currentPlayer = players.get(currentPlayerIndex);
        System.out.println("It is Player " + currentPlayer.getName() + "'s turn");
    }


}
