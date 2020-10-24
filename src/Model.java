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
    private List<Country> countries;
    private int[] troopAllocation = {50, 35, 30, 25, 20};

    /**
     * @constructor
     */
    public Model() {
        parser = new Parser();
        countries = new ArrayList<>();
    }

    public static void main(String[] args) {
        Model model = new Model();
        model.play();
    }


    /**
     * Main play routine.  Loops until end of play.
     */
    public void play() {

        createMap();
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

    public void createMap() {
        //North America
        Country Alaska = new Country("Alaska");
        countries.add(Alaska);
        Country Alberta = new Country("Alberta");
        countries.add(Alberta);
        Country Ontario = new Country("Ontario");
        countries.add(Ontario);
        Country WesternAmerica = new Country("WesternAmerica");
        countries.add(WesternAmerica);
        Country EasternAmerica = new Country("EasternAmerica");
        countries.add(EasternAmerica);
        Country Quebec = new Country("Quebec");
        countries.add(Quebec);
        Country CentralAmerica = new Country("CentralAmerica");
        countries.add(CentralAmerica);
        Country Greenland = new Country("Greenland");
        countries.add(Greenland);
        Country NorthwestAmerica = new Country("NorthwestAmerica");
        countries.add(NorthwestAmerica);

        //South America
        Country Brazil = new Country("Brazil");
        countries.add(Brazil);
        Country Venezuela = new Country("Venezuela");
        countries.add(Venezuela);
        Country Peru = new Country("Peru");
        countries.add(Peru);
        Country Argentina = new Country("Argentina");
        countries.add(Argentina);

        //Australia
        Country WesternAustralia = new Country("WesternAustralia");
        countries.add(WesternAustralia);
        Country EasternAustralia = new Country("EasternAustralia");
        countries.add(EasternAustralia);
        Country Indonesia = new Country("Indonesia");
        countries.add(Indonesia);
        Country NewGuinea = new Country("New Guinea");
        countries.add(NewGuinea);

        //Europe
        Country Ukraine = new Country("Ukraine");
        countries.add(Ukraine);
        Country Scandinavia = new Country("Scandinavia");
        countries.add(Scandinavia);
        Country Iceland = new Country("Iceland");
        countries.add(Iceland);
        Country GreatBritain = new Country("GreatBritain");
        countries.add(GreatBritain);
        Country NorthernEurope = new Country("NorthernEurope");
        countries.add(NorthernEurope);
        Country WesternEurope = new Country("WesternEurope");
        countries.add(WesternEurope);
        Country SouthernEurope = new Country("SouthernEurope");
        countries.add(SouthernEurope);

        //Asia
        Country Yakutsk = new Country("Yakustsk");
        countries.add(Yakutsk);
        Country Siberia = new Country("Siberia");
        countries.add(Siberia);
        Country Kamchatka = new Country("Kamchatka");
        countries.add(Kamchatka);
        Country Irkutsk = new Country("Irkutsk");
        countries.add(Irkutsk);
        Country Ural = new Country("Ural");
        countries.add(Ural);
        Country Japan = new Country("Japan");
        countries.add(Japan);
        Country Mongolia = new Country("Mongolia");
        countries.add(Mongolia);
        Country China = new Country("China");
        countries.add(China);
        Country MiddleEast = new Country("MiddleEast");
        countries.add(MiddleEast);
        Country India = new Country("India");
        countries.add(India);
        Country Siam = new Country("Siam");
        countries.add(Siam);
        Country Afghanistan = new Country("Afghanistan");
        countries.add(Afghanistan);

        //Africa
        Country Congo = new Country("Congo");
        countries.add(Congo);
        Country EastAfrica = new Country("EastAfrica");
        countries.add(EastAfrica);
        Country Egypt = new Country("Egypt");
        countries.add(Egypt);
        Country Madagascar = new Country("Madagascar");
        countries.add(Madagascar);
        Country NorthAfrica = new Country("NorthAfrica");
        countries.add(NorthAfrica);
        Country SouthAfrica = new Country("SouthAfrica");
        countries.add(SouthAfrica);

        //North America
        Alaska.addNeighbours(new Country[]{Alberta, Kamchatka, NorthwestAmerica});
        Alberta.addNeighbours(new Country[]{Alaska, NorthwestAmerica, Ontario, WesternAmerica});
        Ontario.addNeighbours(new Country[]{Quebec, WesternAmerica, EasternAmerica, Greenland, NorthwestAmerica, Alberta});
        WesternAmerica.addNeighbours(new Country[]{Alberta, Ontario, EasternAmerica, CentralAmerica});
        EasternAmerica.addNeighbours(new Country[]{Quebec, Ontario, WesternAmerica, CentralAmerica});
        Quebec.addNeighbours(new Country[]{Ontario, Greenland, EasternAmerica});
        CentralAmerica.addNeighbours(new Country[]{WesternAmerica, EasternAmerica, Venezuela});
        Greenland.addNeighbours(new Country[]{Iceland, Quebec, Ontario, NorthwestAmerica});
        NorthwestAmerica.addNeighbours(new Country[]{Alaska, Alberta, Ontario, Greenland});

        //South America
        Brazil.addNeighbours(new Country[]{Venezuela, Peru, Argentina, NorthAfrica});
        Venezuela.addNeighbours(new Country[]{Brazil, Peru, CentralAmerica});
        Peru.addNeighbours(new Country[]{Brazil, Argentina, Venezuela});
        Argentina.addNeighbours(new Country[]{Peru, Brazil});

        //Australia
        WesternAustralia.addNeighbours(new Country[]{EasternAustralia, Indonesia, NewGuinea});
        EasternAustralia.addNeighbours(new Country[]{WesternAustralia, NewGuinea});
        Indonesia.addNeighbours(new Country[]{Siam, NewGuinea, WesternAustralia});
        NewGuinea.addNeighbours(new Country[]{EasternAustralia, WesternAustralia, Indonesia});

        //Europe
        Ukraine.addNeighbours(new Country[]{Ural, Afghanistan, MiddleEast, SouthernEurope, NorthernEurope, Scandinavia});
        Scandinavia.addNeighbours(new Country[]{Ukraine, NorthernEurope, GreatBritain, Iceland});
        Iceland.addNeighbours(new Country[]{Greenland, GreatBritain, Scandinavia});
        GreatBritain.addNeighbours(new Country[]{WesternEurope, NorthernEurope, Scandinavia, Iceland});
        NorthernEurope.addNeighbours(new Country[]{Scandinavia, GreatBritain, WesternEurope, SouthernEurope, Ukraine});
        WesternEurope.addNeighbours(new Country[]{GreatBritain, NorthernEurope, SouthernEurope, NorthAfrica});
        SouthernEurope.addNeighbours(new Country[]{NorthAfrica, NorthernEurope, Egypt, MiddleEast, Ukraine, WesternEurope});

        //Asia
        Yakutsk.addNeighbours(new Country[]{Kamchatka, Irkutsk, Siberia});
        Siberia.addNeighbours(new Country[]{Ural, China, Mongolia, Irkutsk, Yakutsk});
        Kamchatka.addNeighbours(new Country[]{Alaska, Japan, Yakutsk, Irkutsk, Mongolia});
        Irkutsk.addNeighbours(new Country[]{Yakutsk, Siberia, Kamchatka, Mongolia});
        Ural.addNeighbours(new Country[]{Siberia, China, Afghanistan, Ukraine});
        Japan.addNeighbours(new Country[]{Kamchatka, Mongolia});
        Mongolia.addNeighbours(new Country[]{Irkutsk, Kamchatka, Siberia, Japan});
        China.addNeighbours(new Country[]{Mongolia, Siberia, Ural, Afghanistan, India, Siam});
        MiddleEast.addNeighbours(new Country[]{India, Afghanistan, Ukraine, SouthernEurope, Egypt, EastAfrica});
        India.addNeighbours(new Country[]{MiddleEast, Afghanistan, China, Siam});
        Siam.addNeighbours(new Country[]{India, Indonesia, China});
        Afghanistan.addNeighbours(new Country[]{Ukraine, Ural, China, India, MiddleEast});

        //Africa
        Congo.addNeighbours(new Country[]{EastAfrica, NorthAfrica, SouthAfrica});
        EastAfrica.addNeighbours(new Country[]{MiddleEast, Egypt, NorthAfrica, Congo, SouthAfrica, Madagascar});
        Egypt.addNeighbours(new Country[]{MiddleEast, EastAfrica, NorthAfrica, SouthernEurope});
        Madagascar.addNeighbours(new Country[]{EastAfrica, SouthAfrica});
        NorthAfrica.addNeighbours(new Country[]{Brazil, WesternEurope, Egypt, EastAfrica, Congo});
        SouthAfrica.addNeighbours(new Country[]{Congo, EastAfrica, Madagascar});
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
        Collections.shuffle(countries);
        int countryCount = countries.size();
        while (countryCount != 0) {
            for (Player p : players) {
                if (countryCount > 0) {
                    p.addCountry(countries.get(countryCount - 1));
                    countries.get(countryCount - 1).setOwner(p);
                    countryCount--;
                }
            }
        }

        for (Player p : players) {
            p.distributeTroops(troopAllocation[players.size() - 2]);
        }
    }

    private void getState() {
        for (Country country : countries) {
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

            Country defendingCountry = new Country("");
            Country attackingCountry = new Country("");

            for (Country c : countries) {
                if (c.getName().equals(command.getSecondWord())) {
                    defendingCountry = c;
                }
                if (c.getName().equals(command.getThirdWord())) {
                    attackingCountry = c;
                }
            }

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

            for (Country c : countries) {
                if (c.getName().equals(attackingCountry.getName())) {
                    c = attackingCountry;
                }
                if (c.getName().equals(defendingCountry.getName())) {
                    c = defendingCountry;
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

        ArrayList<String> nameChecker = new ArrayList<String>();

        for (Country c : countries) {
            nameChecker.add(c.getName());
        }

        if (!(nameChecker.contains(command.getSecondWord()))) {//second or third command are not included in country list
            System.out.println("There is no such country.");
            return false;
        }

        if (!(nameChecker.contains(command.getThirdWord()))) {//second or third command are not included in country list
            System.out.println("There is no such country.");
            return false;
        }

        Country defendingCountry = new Country("");
        Country attackingCountry = new Country("");


        for (Country c : countries) {
            if (c.getName().equals(command.getSecondWord())) {
                defendingCountry = c;
            }
            if (c.getName().equals(command.getThirdWord())) {
                attackingCountry = c;
            }
        }

        if (!(currentPlayer.getCountries().contains(attackingCountry))) {//if player doesn't control that country
            System.out.println("Player " + currentPlayer.getName() + " does not have this country.");
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
