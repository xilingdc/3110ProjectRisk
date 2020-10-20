
import java.util.*;

/**
 * @author Xiling Wang
 * @author Aleksandar Veselinovic
 */
public class Model {

    private int playerNum;
    private int armyDefaultNum;
    private ArrayList<String> playerList;
    private Parser parser;
    private String currentPlayer;
    private int currentPlayerIndex;
    private List<Country> countries;

    /**
     * @constructor
     */
    public Model(){
        parser = new Parser();
        countries = new ArrayList<>();
    }

    public static void main(String[] args) {
        Model model = new Model();
        model.play();
    }



    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play(){

        printBegin();//let user enter player number



        //default army setting, need to implement another method to distribute army number by given player number(not finish yet)
       //need to modify countryArmy and playerCountry two hashmaps


        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            processCommand(command);
            if(playerList.size()==1){//when only one player stands
                finished=true;
            }
        }
        System.out.println(playerList.get(0)+" is winner. Gamer Over!");
    }

    public void createMap() {
        //North America
        Country Alaska = new Country("Alaska");
        countries.add(Alaska);
        Country Alberta = new Country("Alberta");
        countries.add(Alberta);
        Country Ontario = new Country("Ontario");
        countries.add(Ontario);
        Country WesternAmerica = new Country("Western America");
        countries.add(WesternAmerica);
        Country EasternAmerica = new Country("Eastern America");
        countries.add(EasternAmerica);
        Country Quebec = new Country("Quebec");
        countries.add(Quebec);
        Country CentralAmerica = new Country("Central America");
        countries.add(CentralAmerica);
        Country Greenland = new Country("Greenland");
        countries.add(Greenland);
        Country NorthwestAmerica = new Country("Northwest America");
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
        Country WesternAustralia = new Country("Western Australia");
        countries.add(WesternAustralia);
        Country EasternAustralia = new Country("Eastern Australia");
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
        Country GreatBritain = new Country("Great Britain");
        countries.add(GreatBritain);
        Country NorthernEurope = new Country("Northern Europe");
        countries.add(NorthernEurope);
        Country WesternEurope = new Country("Western Europe");
        countries.add(WesternEurope);
        Country SouthernEurope = new Country("Southern Europe");
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
        Country EastAfrica = new Country("East Africa");
        countries.add(EastAfrica);
        Country Egypt = new Country("Egypt");
        countries.add(Egypt);
        Country Madagascar = new Country("Madagascar");
        countries.add(Madagascar);
        Country NorthAfrica = new Country("North Africa");
        countries.add(NorthAfrica);
        Country SouthAfrica = new Country("South Africa");
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
    public void printBegin(){
        System.out.println("Welcome to Risk.");
        System.out.println("Please enter player number(2-6):");
        playerNum = parser.getPlayerNum();
        playerList = new ArrayList<>();
        for (int i = 1; i <=playerNum; i++) {
            playerList.add("player"+i);//add player1, player2.... into playerList
            System.out.println("player"+i+" has been added;");
        }
        currentPlayerIndex=0;
        currentPlayer=playerList.get(0);

    }


    private void processCommand (Command command){

        if(command.isUnknown()){
            System.out.println("Invalid Command.");
            return;
        }

        String commandWord = command.getCommandWord();

        if(commandWord.equals("state")){
            getState();
        }else if(commandWord.equals("attack")){
            attack(command);
        }else if(commandWord.equals("pass")){
            pass();
        }

    }


    private void getState(){
        for (Country country : countries) {
            System.out.println(country.getName() + ", owned by: " + country.getOwner() + ", number of armies" + country.getArmySize());
        }
    }

    /**
     *
     * @param command
     * @return true/false (valid attack)
     */
    private boolean isValidAttack(Command command){
        if(!command.hasSecondWord()){//no second command
            System.out.println("Which country will be attacked?");
            return false;
        }
        if(!command.hasThirdWord()){//no third command
            System.out.println("Which country will launch attack?");
            return false;
        }

        Country defendingCountry = new Country(command.getSecondWord());
        Country attackingCountry = new Country(command.getThirdWord());

        if(!(countries.contains(defendingCountry))) {//second or third command are not included in country list
            System.out.println("There is no such country.");
            return false;
        }

        if(!(countries.contains(attackingCountry))){//second or third command are not included in country list
            System.out.println("There is no such country.");
            return false;
        }

        if(attackingCountry.getArmySize() == 1){//if attack country's army is only 1 left
            System.out.println(command.getThirdWord()+"'s army number is not enough to attack.");
            return false;
        }
        if(attackingCountry.hasNeighbor(defendingCountry)){//if attack country is neighbour country of attacked country
            return true;
        }else{
            System.out.println(defendingCountry + " is not a neighbor of " + attackingCountry);
            return false;
        }

    }

    /**
     *
     * @param command
     */
    private void attack(Command command){//command description "attack attackedCountry attackCountry" second command represents the country will be attacked, third command represents the country will launch attack.
        if(isValidAttack(command)){//when attack's choice is valid
            //dice rules, to decide who will win and lose how many army, and leave how many army at the country
            //not finish yet
            //make sure to check if the player still exists at least one country, otherwise delete the certain player from playerList
        }
    }


    /**
     * pass to next player
     */
    private void pass(){
        if(currentPlayerIndex==playerList.size()-1){
            currentPlayerIndex=0;
            currentPlayer=playerList.get(currentPlayerIndex);
        }else{
            currentPlayerIndex++;
            currentPlayer=playerList.get(currentPlayerIndex);
        }

    }







}
