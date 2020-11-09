import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Since number of players is chosen through the view, I can't set it manually here.
 * So please input 3 as number of players when you run the test.
 * I'm basically testing model through view because a lot of the methods require a view.
 * */
public class ModelTest{
    View v;

    @Test
    public void testCountrySize() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(42, v.getModel().getMap().getCountries().size());
    }

    @Test
    public void testPass() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(3, v.getModel().getPlayers().size());
        assertEquals("1", v.getModel().getCurrentPlayer().getName());
        v.getModel().pass();
        v.getModel().pass();
        assertEquals("3", v.getModel().getCurrentPlayer().getName());
        v.getModel().pass();
        assertEquals("1", v.getModel().getCurrentPlayer().getName());
    }

    @Test
    public void testCanDefend() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Country attacker = v.getModel().getMap().getCountry("Greenland");
        Country defender = v.getModel().getMap().getCountry("Quebec");
        if(!(v.getModel().getCurrentPlayer().getCountries().contains(attacker))){
            attacker.setOwner(v.getModel().getCurrentPlayer());
            v.getModel().getCurrentPlayer().addCountry(attacker);
        }
        if(!(v.getModel().getCurrentPlayer().getCountries().contains(defender))){
            defender.setOwner(v.getModel().getCurrentPlayer());
            v.getModel().getCurrentPlayer().addCountry(defender);
        }
        assertFalse(v.getModel().canDefend(attacker, defender));

        v.getModel().getCurrentPlayer().removeCountry(defender);
        defender.setOwner(v.getModel().getNextPlayer());
        v.getModel().getNextPlayer().addCountry(defender);
        assertTrue(v.getModel().canDefend(attacker, defender));


        defender = v.getModel().getMap().getCountry("Brazil");
        if(!(v.getModel().getNextPlayer().getCountries().contains(defender))){
            defender.setOwner(v.getModel().getNextPlayer());
            v.getModel().getNextPlayer().addCountry(defender);
        }
        assertFalse(v.getModel().canDefend(attacker, defender));
    }

    @Test
    public void testIsAttacker() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Country attacker = v.getModel().getMap().getCountry("Greenland");
        if(!(v.getModel().getCurrentPlayer().getCountries().contains(attacker))){
            attacker.setOwner(v.getModel().getCurrentPlayer());
            v.getModel().getCurrentPlayer().addCountry(attacker);
        }
        v.getModel().getCurrentPlayer().removeCountry(attacker);
        attacker.setOwner(v.getModel().getNextPlayer());
        assertFalse(v.getModel().isAttacker(attacker));

        attacker.setOwner(v.getModel().getCurrentPlayer());
        v.getModel().getCurrentPlayer().addCountry(attacker);
        int num = attacker.getArmySize() - 1;
        attacker.removeTroops(num);
        assertFalse(v.getModel().isAttacker(attacker));

        attacker.addTroops(num);
        assertTrue(v.getModel().isAttacker(attacker));
    }
}