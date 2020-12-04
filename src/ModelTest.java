import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
/**
 * @author Ali Fahd
 */
public class ModelTest{
    View v;
    Model m;

    @Test
    public void testCountrySize() {
        try {
        v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Model m = new Model();
        m.addView(v);
        m.processBegin(3, 0);
        m.setUp();
        assertEquals(42, m.getMap().getCountries().size());
    }

    @Test
    public void testPass() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Model m = new Model();
        m.addView(v);
        m.processBegin(3, 0);
        m.setUp();
        assertEquals(3, m.getPlayers().size());
        assertEquals("1", m.getCurrentPlayer().getName());
        m.pass();
        m.pass();
        assertEquals("3", m.getCurrentPlayer().getName());
        m.pass();
        assertEquals("1", m.getCurrentPlayer().getName());
    }

    @Test
    public void testCanDefend() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Model m = new Model();
        m.addView(v);
        m.processBegin(3, 0);
        m.setUp();
        Country attacker = m.getMap().getCountry("Greenland");
        Country defender = m.getMap().getCountry("Quebec");
        if(!(m.getCurrentPlayer().getCountries().contains(attacker))){
            attacker.setOwner(m.getCurrentPlayer());
            m.getCurrentPlayer().addCountry(attacker);
        }
        if(!(m.getCurrentPlayer().getCountries().contains(defender))){
            defender.setOwner(m.getCurrentPlayer());
            m.getCurrentPlayer().addCountry(defender);
        }
        assertFalse(m.canDefend(attacker, defender));

        m.getCurrentPlayer().removeCountry(defender);
        defender.setOwner(m.getNextPlayer());
        m.getNextPlayer().addCountry(defender);
        assertTrue(m.canDefend(attacker, defender));


        defender = m.getMap().getCountry("Brazil");
        if(!(m.getNextPlayer().getCountries().contains(defender))){
            defender.setOwner(m.getNextPlayer());
            m.getNextPlayer().addCountry(defender);
        }
        assertFalse(m.canDefend(attacker, defender));
    }

    @Test
    public void testIsAttacker() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Model m = new Model();
        m.addView(v);
        m.processBegin(3, 0);
        m.setUp();
        Country attacker = m.getMap().getCountry("Greenland");
        if(!(m.getCurrentPlayer().getCountries().contains(attacker))){
            attacker.setOwner(m.getCurrentPlayer());
            m.getCurrentPlayer().addCountry(attacker);
        }
        m.getCurrentPlayer().removeCountry(attacker);
        attacker.setOwner(m.getNextPlayer());
        assertFalse(m.isAttacker(attacker));

        attacker.setOwner(m.getCurrentPlayer());
        m.getCurrentPlayer().addCountry(attacker);
        int num = attacker.getArmySize() - 1;
        attacker.removeTroops(num);
        assertFalse(m.isAttacker(attacker));

        attacker.addTroops(num);
        assertTrue(m.isAttacker(attacker));
    }

    @Test
    public void testFortify() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Model m = new Model();
        m.addView(v);
        m.processBegin(3, 0);
        m.setUp();
        Country c1 = m.getMap().getCountry("Alaska");
        m.getCurrentPlayer().addCountry(c1);
        c1.setOwner(m.getCurrentPlayer());
        Country c2 = m.getMap().getCountry("Northwest America");
        m.getCurrentPlayer().addCountry(c2);
        c2.setOwner(m.getCurrentPlayer());
        Country c3 = m.getMap().getCountry("Greenland");
        m.getCurrentPlayer().addCountry(c3);
        c3.setOwner(m.getCurrentPlayer());
        Country c4 = m.getMap().getCountry("Iceland");
        m.getCurrentPlayer().addCountry(c4);
        c4.setOwner(m.getCurrentPlayer());
        Country c5 = m.getMap().getCountry("Eastern Australia");
        m.getCurrentPlayer().addCountry(c5);
        c5.setOwner(m.getCurrentPlayer());
        Country c6 = m.getMap().getCountry("Siam");
        m.getNextPlayer().addCountry(c6);
        c6.setOwner(m.getNextPlayer());

        assertTrue(m.canFortify(c1, c4));
        assertFalse(m.canFortify(c1, c5));
        assertFalse(m.canFortify(c1, c6));
    }

    @Test
    public void testBonusPlacement() {
        try {
            v = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Model m = new Model();
        m.addView(v);
        m.processBegin(3, 0);
        m.setUp();
        m.getCurrentPlayer().getCountries().clear();
        for(Country c : m.getMap().getCountries()){
            m.getCurrentPlayer().addCountry(c);
            c.setOwner(m.getCurrentPlayer());
        }
        assertTrue(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getAfrica()));
        assertTrue(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getSouthAmerica()));
        assertTrue(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getNorthAmerica()));
        assertTrue(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getEurope()));
        assertTrue(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getAsia()));
        assertTrue(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getAustralia()));
        assertEquals(((m.getCurrentPlayer().getCountries().size()/3)+2+2+3+5+5+7), m.bonusTroopCalculator());

        m.getCurrentPlayer().removeCountry(m.getMap().getCountry("Madagascar"));
        assertEquals(((m.getCurrentPlayer().getCountries().size()/3)+2+2+5+5+7), m.bonusTroopCalculator());
        assertFalse(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getAfrica()));

        m.getCurrentPlayer().removeCountry(m.getMap().getCountry("Peru"));
        assertEquals(((m.getCurrentPlayer().getCountries().size()/3)+2+5+5+7), m.bonusTroopCalculator());
        assertFalse(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getSouthAmerica()));

        m.getCurrentPlayer().removeCountry(m.getMap().getCountry("Ontario"));
        assertEquals(((m.getCurrentPlayer().getCountries().size()/3)+2+5+7), m.bonusTroopCalculator());
        assertFalse(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getNorthAmerica()));

        m.getCurrentPlayer().removeCountry(m.getMap().getCountry("Japan"));
        assertEquals(((m.getCurrentPlayer().getCountries().size()/3)+2+5), m.bonusTroopCalculator());
        assertFalse(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getAsia()));

        m.getCurrentPlayer().removeCountry(m.getMap().getCountry("Iceland"));
        assertEquals(((m.getCurrentPlayer().getCountries().size()/3)+2), m.bonusTroopCalculator());
        assertFalse(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getEurope()));

        m.getCurrentPlayer().removeCountry(m.getMap().getCountry("Indonesia"));
        assertEquals(((m.getCurrentPlayer().getCountries().size()/3)), m.bonusTroopCalculator());
        assertFalse(m.getCurrentPlayer().getCountries().containsAll(m.getMap().getAustralia()));

        assertTrue(m.isPlaceable(m.getMap().getCountry("Western Australia")));
    }
}