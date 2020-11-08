/**
 * @author Xiling Wang
 * @author Aleksandar Veselinovic
 * @author Ali Fahd
 */

public class Command
{
    private String commandWord;
    private String secondWord;
    private String thirdWord;

    /**
     * @param firstWord The first word of the command. Null if the command
     *                  was not recognised.
     * @param secondWord The second word of the command (attacked country).
     * @param thirdWord The third word of the command (attack side country)
     */
    public Command(String firstWord, String secondWord, String thirdWord)
    {
        commandWord = firstWord;
        this.secondWord = secondWord;
        this.thirdWord = thirdWord;
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     * @return The command word.
     */
    public String getCommandWord()
    {
        return commandWord;
    }

    /**
     * @return The second word of this command. Returns null if there was no
     * second word.
     */
    public String getSecondWord()
    {
        return secondWord;
    }//for country defending

    /**
     * @return The third word of this command. Returns null if there was no
     * third word.
     */
    public String getThirdWord() { return thirdWord; }//for country attacking

    /**
     * @return The whole line of command. Returns null if there was no command.
     */
    public String getCommand() { return commandWord + " " + secondWord + " " + thirdWord; }//for whole line




    /**
     * @return true if this command was not understood.
     */
    public boolean isUnknown()
    {
        return (commandWord == null);
    }//the command doesn't exist

    /**
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord()
    {
        return (secondWord != null);
    }//for country defending



    /**
     * @return true if the command has a third word.
     */
    public boolean hasThirdWord() { return (thirdWord != null); }//for country attacking
}

