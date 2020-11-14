import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Ali Fahd
 */
/**
 * Since number of players is chosen through the view, I can't set it manually here.
 * So please input 3 as number of players when you run the test for each of the views and just hit ok on all JOptionPanes.
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

    @Test
    public void testFortify() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Country c1 = v.getModel().getMap().getCountry("Alaska");
        v.getModel().getCurrentPlayer().addCountry(c1);
        c1.setOwner(v.getModel().getCurrentPlayer());
        Country c2 = v.getModel().getMap().getCountry("Northwest America");
        v.getModel().getCurrentPlayer().addCountry(c2);
        c2.setOwner(v.getModel().getCurrentPlayer());
        Country c3 = v.getModel().getMap().getCountry("Greenland");
        v.getModel().getCurrentPlayer().addCountry(c3);
        c3.setOwner(v.getModel().getCurrentPlayer());
        Country c4 = v.getModel().getMap().getCountry("Iceland");
        v.getModel().getCurrentPlayer().addCountry(c4);
        c4.setOwner(v.getModel().getCurrentPlayer());
        Country c5 = v.getModel().getMap().getCountry("Eastern Australia");
        v.getModel().getCurrentPlayer().addCountry(c5);
        c5.setOwner(v.getModel().getCurrentPlayer());
        Country c6 = v.getModel().getMap().getCountry("Siam");
        v.getModel().getNextPlayer().addCountry(c6);
        c6.setOwner(v.getModel().getNextPlayer());

        assertTrue(v.getModel().canFortify(c1, c4));
        assertFalse(v.getModel().canFortify(c1, c5));
        assertFalse(v.getModel().canFortify(c1, c6));
    }

    @Test
    public void testBonusPlacement() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        v.getModel().getCurrentPlayer().getCountries().clear();
        for(Country c : v.getModel().getMap().getCountries()){
            v.getModel().getCurrentPlayer().addCountry(c);
            c.setOwner(v.getModel().getCurrentPlayer());
        }
        assertTrue(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getAfrica()));
        assertTrue(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getSouthAmerica()));
        assertTrue(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getNorthAmerica()));
        assertTrue(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getEurope()));
        assertTrue(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getAsia()));
        assertTrue(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getAustralia()));
        assertEquals(((v.getModel().getCurrentPlayer().getCountries().size()/3)+2+2+3+5+5+7), v.getModel().bonusTroopCalculator());

        v.getModel().getCurrentPlayer().removeCountry(v.getModel().getMap().getCountry("Madagascar"));
        assertEquals(((v.getModel().getCurrentPlayer().getCountries().size()/3)+2+2+5+5+7), v.getModel().bonusTroopCalculator());
        assertFalse(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getAfrica()));

        v.getModel().getCurrentPlayer().removeCountry(v.getModel().getMap().getCountry("Peru"));
        assertEquals(((v.getModel().getCurrentPlayer().getCountries().size()/3)+2+5+5+7), v.getModel().bonusTroopCalculator());
        assertFalse(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getAfrica()));

        v.getModel().getCurrentPlayer().removeCountry(v.getModel().getMap().getCountry("Ontario"));
        assertEquals(((v.getModel().getCurrentPlayer().getCountries().size()/3)+2+5+7), v.getModel().bonusTroopCalculator());
        assertFalse(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getAfrica()));

        v.getModel().getCurrentPlayer().removeCountry(v.getModel().getMap().getCountry("Japan"));
        assertEquals(((v.getModel().getCurrentPlayer().getCountries().size()/3)+2+5), v.getModel().bonusTroopCalculator());
        assertFalse(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getAfrica()));

        v.getModel().getCurrentPlayer().removeCountry(v.getModel().getMap().getCountry("Iceland"));
        assertEquals(((v.getModel().getCurrentPlayer().getCountries().size()/3)+2), v.getModel().bonusTroopCalculator());
        assertFalse(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getAfrica()));

        v.getModel().getCurrentPlayer().removeCountry(v.getModel().getMap().getCountry("Indonesia"));
        assertEquals(((v.getModel().getCurrentPlayer().getCountries().size()/3)), v.getModel().bonusTroopCalculator());
        assertFalse(v.getModel().getCurrentPlayer().getCountries().containsAll(v.getModel().getMap().getAfrica()));
    }
}