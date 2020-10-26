import java.util.Scanner;

/**
 * @author  Xiling Wang
 */
public class Parser
{
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser()
    {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }


    /**
     *This will set the number of players the game will be played with and distribution is based on this.
     * @return player number
     */
    public int getPlayerNum(){
        int playerNum=0;
        boolean isValid = false;
        while(!isValid) {//2<=playerNum<=6
            playerNum = reader.nextInt();
            if(playerNum<=6&&playerNum>=2){
                isValid=true;
            } else {
                System.out.print("Make sure it is between 2 to 6 integer number: ");
            }
        }
        reader.nextLine();
        return playerNum;

    }

    /**
     * This method will give us the number of dice the player wants to use based on restrictions
     * @param maxDice - the max amount of dice the player can use
     * @return number of dice player wanted to use
     */
    public int getNumberOfDice(int maxDice) {
        int numberOfDice = 0;
        boolean isValid = false;
        while (!isValid) {
            numberOfDice = reader.nextInt();
            if (numberOfDice<= maxDice) {
                isValid = true;
            } else {
                System.out.print("You cannot choose more than " + maxDice + " dice. Choose again: ");
            }
        }
        reader.nextLine();
        return numberOfDice;
    }



    /**
     * Get the next part os the user's input
     * @return The next command from the user.
     */
    public Command getCommand()
    {
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;
        String word3 = null;

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next();      // get first word
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next();      // get second word
                // note: we just ignore the rest of the input line.
                if(tokenizer.hasNext()) {
                    word3 = tokenizer.next();      // get third word
                    // note: we just ignore the rest of the input line.
                }
            }
        }

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        if(commands.isCommand(word1)) {
            return new Command(word1, word2,word3);
        }
        else {
            return new Command(null, word2,word3);
        }
    }

    /**
     * Print out a list of valid command words.
     */
    public void showCommands()
    {
        commands.showAll();
    }
}
