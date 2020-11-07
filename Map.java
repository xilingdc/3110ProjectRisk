import java.util.ArrayList;
import java.util.List;

    /**
    *@author Xiling Wang
    *@author Aleksandar Veselinovic
    *@author Ali Fahd
    */
public class Map {
    private List<Country> countries;//general country list
    /**
     *constructor Map()
    */
    public Map() {
        countries = new ArrayList<>();
        //North America
        Country Alaska = new Country("Alaska");//initialization and add the country into country list
        countries.add(Alaska);
        Country Alberta = new Country("Alberta");
        countries.add(Alberta);
        Country Ontario = new Country("Ontario");
        countries.add(Ontario);
        Country WesternAmerica = new Country("WesternAmerica");
        countries.add(WesternAmerica);
        Country EasternAmerica = new Country("EasternAmerica");
        countries.add(EasternAmerica);
        Country Quebec = new Country("Quebec");
        countries.add(Quebec);
        Country CentralAmerica = new Country("CentralAmerica");
        countries.add(CentralAmerica);
        Country Greenland = new Country("Greenland");
        countries.add(Greenland);
        Country NorthwestAmerica = new Country("NorthwestAmerica");
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
        Country WesternAustralia = new Country("WesternAustralia");
        countries.add(WesternAustralia);
        Country EasternAustralia = new Country("EasternAustralia");
        countries.add(EasternAustralia);
        Country Indonesia = new Country("Indonesia");
        countries.add(Indonesia);
        Country NewGuinea = new Country("NewGuinea");
        countries.add(NewGuinea);

        //Europe
        Country Ukraine = new Country("Ukraine");
        countries.add(Ukraine);
        Country Scandinavia = new Country("Scandinavia");
        countries.add(Scandinavia);
        Country Iceland = new Country("Iceland");
        countries.add(Iceland);
        Country GreatBritain = new Country("GreatBritain");
        countries.add(GreatBritain);
        Country NorthernEurope = new Country("NorthernEurope");
        countries.add(NorthernEurope);
        Country WesternEurope = new Country("WesternEurope");
        countries.add(WesternEurope);
        Country SouthernEurope = new Country("SouthernEurope");
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
        Country EastAfrica = new Country("EastAfrica");
        countries.add(EastAfrica);
        Country Egypt = new Country("Egypt");
        countries.add(Egypt);
        Country Madagascar = new Country("Madagascar");
        countries.add(Madagascar);
        Country NorthAfrica = new Country("NorthAfrica");
        countries.add(NorthAfrica);
        Country SouthAfrica = new Country("SouthAfrica");
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
    public List<Country> getCountries() {
        return countries;
    }
    
    
    /**
    *@return country list's size
    */
    public int getNumberOfCountries() {
        return countries.size();
    }
}
