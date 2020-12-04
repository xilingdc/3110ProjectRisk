import java.awt.*;
import java.util.EventObject;
/*
*@author Xiling Wang
 */
public class Event extends EventObject {

    private String message,playerName;
    private int min, max, troop;
    private Color color;
    private Country country;
    private Player player;

    /**
     * Constructs a prototypical Event.
     *
     * @param model
     * @throws IllegalArgumentException if source is null
     */
    public Event(Model model,String message) {
        super(model);
        this.message=message;
    }

    public Event(Model model,Player player) {
        super(model);
        this.player=player;
    }

    public Event(Model model, String message, int min, int max){
        super(model);
        this.message=message;
        this.min = min;
        this.max = max;

    }

    public Event(Model model, Country country, Color color, int troop){
        super(model);
        this.color=color;
        this.country = country;
        this.troop = troop;

    }

    public Event(Model model,  int troop){
        super(model);
        this.troop = troop;

    }

    public Event(Model model, String playerName, Color color){
        super(model);
        this.color=color;
        this.playerName = playerName;

    }

    public int getTroop() {
        return troop;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Player getPlayer() {
        return player;
    }

    public Color getColor() {
        return color;
    }

    public Country getCountry() {
        return country;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getMessage(){
        return this.message;
    }

}
