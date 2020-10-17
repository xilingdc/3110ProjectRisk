
import java.util.*;

/**
 * @author Xiling Wang
 */
public class Model {

    private int playerNum;
    private int armyDefaultNum;
    private HashMap<Country,Integer> countryArmy;//contain country with its relative army value
    private HashMap<Country,String> playerCountry;//player name with its relative
    private ArrayList<String> playerList;
    private Parser parser;
    private String currentPlayer;
    private int currentPlayerIndex;

    public static List<Country> countryList = Arrays.asList(Country.values());


    /**
     * @constructor
     */
    public Model(){
        parser = new Parser();
        countryArmy = new HashMap<>();
        playerCountry = new HashMap<>();


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
        Country[] c = Country.values();
        for (Country country : c) {
            System.out.println(country+": "+ playerCountry.get(country)+" "+countryArmy.get(country));
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

        if(containsCountry(command.getSecondWord())){//second or third command are not included in country list
            System.out.println("There is no such country.");
            return false;
        }

        if(containsCountry(command.getThirdWord())){//second or third command are not included in country list
            System.out.println("There is no such country.");
            return false;
        }

        if(countryArmy.get(Country.valueOf(command.getThirdWord()))==1){//if attack country's army is only 1 left
            System.out.println(command.getThirdWord()+"'s army number is not enough to attack.");
            return false;
        }
        if(CountryNeighbour.valueOf(command.getThirdWord()).isNeighbour(Country.valueOf(command.getSecondWord()))){//if attack country is neighbour country of attacked country
            return true;
        }else{
            System.out.println(command.getSecondWord()+" is not "+command.getThirdWord()+"'s neighbour country");
            return false;
        }

    }

    /**
     *
     * @param str
     * @return true/false
     */
    private boolean containsCountry(String str){
        Country c[] = Country.values();
        for (Country country : c) {
            if(str.equals(country.name())){
                return false;
            }
        }

        return true;
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
