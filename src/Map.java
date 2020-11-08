import java.util.ArrayList;
import java.util.List;

    /**
    *@author Xiling Wang
    *@author Aleksandar Veselinovic
    *@author Ali Fahd
    */
public class Map {
    private ArrayList<Country> countries;//general country list
    /**
     *constructor Map()
    */
    public Map() {
        countries = new ArrayList<>();
        //North America
        Country Alaska = new Country("Alaska", 365, 90);//initialization and add the country into country list
        countries.add(Alaska);
        Country Alberta = new Country("Alberta", 440, 160);
        countries.add(Alberta);
        Country Ontario = new Country("Ontario", 510, 160);
        countries.add(Ontario);
        Country WesternAmerica = new Country("Western America", 445, 220);
        countries.add(WesternAmerica);
        Country EasternAmerica = new Country("Eastern America", 525, 240);
        countries.add(EasternAmerica);
        Country Quebec = new Country("Quebec", 575, 160);
        countries.add(Quebec);
        Country CentralAmerica = new Country("Central America", 455, 300);
        countries.add(CentralAmerica);
        Country Greenland = new Country("Greenland", 640, 50);
        countries.add(Greenland);
        Country NorthwestAmerica = new Country("Northwest America", 455, 95);
        countries.add(NorthwestAmerica);

        //South America
        Country Brazil = new Country("Brazil", 605, 425);
        countries.add(Brazil);
        Country Venezuela = new Country("Venezuela", 525, 360);
        countries.add(Venezuela);
        Country Peru = new Country("Peru", 535, 440);
        countries.add(Peru);
        Country Argentina = new Country("Argentina", 545, 525);
        countries.add(Argentina);

        //Australia
        Country WesternAustralia = new Country("Western Australia", 1150, 560);
        countries.add(WesternAustralia);
        Country EasternAustralia = new Country("Eastern Australia", 1235, 560);
        countries.add(EasternAustralia);
        Country Indonesia = new Country("Indonesia", 1110, 470);
        countries.add(Indonesia);
        Country NewGuinea = new Country("NewGuinea", 1190, 445);
        countries.add(NewGuinea);

        //Europe
        Country Ukraine = new Country("Ukraine", 870, 190);
        countries.add(Ukraine);
        Country Scandinavia = new Country("Scandinavia", 780, 140);
        countries.add(Scandinavia);
        Country Iceland = new Country("Iceland", 715, 120);
        countries.add(Iceland);
        Country GreatBritain = new Country("Great Britain", 700, 210);
        countries.add(GreatBritain);
        Country NorthernEurope = new Country("Northern Europe", 780, 210);
        countries.add(NorthernEurope);
        Country WesternEurope = new Country("Western Europe", 715, 300);
        countries.add(WesternEurope);
        Country SouthernEurope = new Country("Southern Europe", 790, 280);
        countries.add(SouthernEurope);

        //Asia
        Country Yakutsk = new Country("Yakustsk", 1100, 65);
        countries.add(Yakutsk);
        Country Siberia = new Country("Siberia", 1020, 80);
        countries.add(Siberia);
        Country Kamchatka = new Country("Kamchatka", 1180, 85);
        countries.add(Kamchatka);
        Country Irkutsk = new Country("Irkutsk", 1090, 160);
        countries.add(Irkutsk);
        Country Ural = new Country("Ural", 980, 150);
        countries.add(Ural);
        Country Japan = new Country("Japan", 1190, 220);
        countries.add(Japan);
        Country Mongolia = new Country("Mongolia", 1100, 220);
        countries.add(Mongolia);
        Country China = new Country("China", 1090, 280);
        countries.add(China);
        Country MiddleEast = new Country("Middle East", 900, 360);
        countries.add(MiddleEast);
        Country India = new Country("India", 1020, 350);
        countries.add(India);
        Country Siam = new Country("Siam", 1090, 360);
        countries.add(Siam);
        Country Afghanistan = new Country("Afghanistan", 960, 240);
        countries.add(Afghanistan);

        //Africa
        Country Congo = new Country("Congo", 825, 480);
        countries.add(Congo);
        Country EastAfrica = new Country("East Africa", 870, 440);
        countries.add(EastAfrica);
        Country Egypt = new Country("Egypt", 825, 370);
        countries.add(Egypt);
        Country Madagascar = new Country("Madagascar", 915, 560);
        countries.add(Madagascar);
        Country NorthAfrica = new Country("North Africa", 740, 400);
        countries.add(NorthAfrica);
        Country SouthAfrica = new Country("South Africa", 825, 560);
        countries.add(SouthAfrica);

        //North America
        Alaska.addNeighbours(new Country[]{Alberta, Kamchatka, NorthwestAmerica});//add coresponding countries as the certain country's neighbour
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
     * Return a country we are looking for based on its name
     * @param name - get country object based on name
    *@return Country object - from given name, return corresponding country
    */
    public Country getCountry(String name) {
        for (Country country : countries) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        return null;
    }


    public int getIndex(String name){
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Return a country we are looking for based on its index
     * @param index - the position of the country in the countries field
    *@return from given index number, return corresponding country
    */
    public Country getCountry(int index) {
        return countries.get(index);
    }
    
    
    /**
     * @param name - the name of the country we are looking for
    *@return true, if country exists; false, if country does not exist
    */
    public boolean hasCountry(String name) {
        return countries.contains(getCountry(name));
    }
    
    
    /**
    *@return general country list
    */
    public ArrayList<Country> getCountries() {
        return countries;
    }
    
    
    /**
    *@return country list's size
    */
    public int getNumberOfCountries() {
        return countries.size();
    }
}
