/**
 * @author Xiling Wang
 * @author Aleksandar Veselinovic
 * @author Ali Fahd
 */

public class CommandWords
{
    // a constant array that holds all valid command words
    private static final String[] validCommands = {
            "state", "attack", "pass"
    };

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // nothing to do at the moment...
    }

    /**
     * Check whether a given String is a valid command word.
     * @param aString - the user's input
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        //loop through all valid commands to see if it matches the user's input
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    /**
     * Print all valid commands to System.out.
     */
    public void showAll()
    {
        //a help option to tell the user all the commands the can use, loop through all available commands and display them
        for(String command: validCommands) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}
