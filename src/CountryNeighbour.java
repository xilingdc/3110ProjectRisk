import java.util.ArrayList;

/**
 * @author: Xiling Wang
 */
public enum CountryNeighbour {

    //north America
    Alaska(new Country[]{Country.Alberta, Country.Kamchatka, Country.NorthwestAmerica}),
    Alberta(new Country[]{Country.Alaska,Country.NorthwestAmerica,Country.Ontario,Country.WesternAmerica}),
    Ontario(new Country[]{Country.Quebec,Country.WesternAmerica,Country.EasternAmerica,Country.Greenland,Country.NorthwestAmerica,Country.Alberta}),
    WesternAmerica(new Country[]{Country.Alberta,Country.Ontario,Country.EasternAmerica,Country.CentralAmerica}),
    EasternAmerica(new Country[]{Country.Quebec,Country.Ontario,Country.WesternAmerica,Country.CentralAmerica}),
    Quebec(new Country[]{Country.Ontario,Country.Greenland,Country.EasternAmerica}),
    CentralAmerica(new Country[]{Country.WesternAmerica, Country.EasternAmerica, Country.Venezuela}),
    Greenland(new Country[]{Country.Iceland, Country.Quebec, Country.Ontario, Country.NorthwestAmerica}),
    NorthwestAmerica(new Country[]{Country.Alaska, Country.Alberta, Country.Ontario, Country.Greenland}),
    //South America
    Brazil(new Country[]{Country.Venezuela, Country.Peru, Country.Argentina, Country.NorthAfrica}),
    Venezuela(new Country[]{Country.Brazil, Country.Peru, Country.CentralAmerica}),
    Peru(new Country[]{Country.Brazil, Country.Argentina, Country.Venezuela}),
    Argentina(new Country[]{Country.Peru, Country.Brazil}),

    //Australia
    WesternAustralia(new Country[]{Country.EasternAustralia, Country.Indoneasia, Country.NewGuinea}),
    EasternAustralia(new Country[]{Country.WesternAustralia, Country.NewGuinea}),
    Indoneasia(new Country[]{Country.Siam,Country.NewGuinea, Country.WesternAustralia}),
    NewGuinea(new Country[]{Country.EasternAustralia, Country.WesternAustralia, Country.Indoneasia}),

    //Europe
    Ukraine(new Country[]{Country.Ural, Country.Afganistan, Country.MiddleEast, Country.SouthernEurope, Country.NorthernEurope, Country.Scandinavia}),
    Scandinavia(new Country[]{Country.Ukraine, Country.NorthernEurope, Country.GreatBritain, Country.Iceland}),
    Iceland(new Country[]{Country.Greenland, Country.GreatBritain, Country.Scandinavia}),
    GreatBritain(new Country[]{Country.WesternEurope, Country.NorthernEurope, Country.Scandinavia, Country.Iceland}),
    NorthernEurope(new Country[]{Country.Scandinavia, Country.GreatBritain, Country.WesternEurope, Country.SouthernEurope, Country.Ukraine}),
    WesternEurope(new Country[]{Country.GreatBritain, Country.NorthernEurope, Country.SouthernEurope, Country.NorthAfrica}),
    SouthernEurope(new Country[]{Country.NorthAfrica, Country.NorthernEurope, Country.Egypt, Country.MiddleEast, Country.Ukraine, Country.WesternEurope}),

    //Asia
    Yakutsk(new Country[]{Country.Kamchatka, Country.Irkutsk, Country.Siberia}),
    Siberia(new Country[]{Country.Ural, Country.China, Country.Mongolia, Country.Irkutsk, Country.Yakutsk}),
    Kamchatka(new Country[]{Country.Alaska, Country.Japan, Country.Yakutsk, Country.Irkutsk, Country.Mongolia}),
    Irkutsk(new Country[]{Country.Yakutsk, Country.Siberia, Country.Kamchatka, Country.Mongolia}),
    Ural(new Country[]{Country.Siberia, Country.China, Country.Afganistan, Country.Ukraine}),
    Japan(new Country[]{Country.Kamchatka, Country.Mongolia}),
    Mongolia(new Country[]{Country.Irkutsk, Country.Kamchatka, Country.Siberia, Country.Japan}),
    China(new Country[]{Country.Mongolia, Country.Siberia, Country.Ural, Country.Afganistan, Country.India, Country.Siam}),
    MiddleEast(new Country[]{Country.India, Country.Afganistan, Country.Ukraine, Country.SouthernEurope, Country.Egypt, Country.EastAfrica}),
    India(new Country[]{Country.MiddleEast, Country.Afganistan, Country.China, Country.Siam}),
    Siam(new Country[]{Country.India, Country.Indoneasia, Country.China}),
    Afganistan(new Country[]{Country.Ukraine, Country.Ural, Country.China, Country.India, Country.MiddleEast}),

    //Africa
    Congo(new Country[]{Country.EastAfrica, Country.NorthAfrica, Country.SouthAfrica}),
    EastAfrica(new Country[]{Country.MiddleEast, Country.Egypt, Country.NorthAfrica, Country.Congo, Country.SouthAfrica, Country.Madagascar}),
    Egypt(new Country[]{Country.MiddleEast, Country.EastAfrica, Country.NorthAfrica, Country.SouthernEurope}),
    Madagascar(new Country[]{Country.EastAfrica, Country.SouthAfrica}),
    NorthAfrica(new Country[]{Country.Brazil, Country.WesternEurope, Country.Egypt, Country.EastAfrica, Country.Congo}),
    SouthAfrica(new Country[]{Country.Congo, Country.EastAfrica, Country.Madagascar});



    private Country[] neighbour;//neighbour country array

    /**
     * @Constructor
     * @param neighbour
     */
    CountryNeighbour(Country[] neighbour){
        this.neighbour=neighbour;
    }


    /**
     *
     * @param country
     * @return boolean value to descripe if a target country is the current country's neighbour.
     */
    public boolean isNeighbour (Country country){
        Country[] array = this.getNeibourCountry();
        for (Country country1 : array) {
            if(country1.equals(country)){
                return true;
            }
        }
        return  false;
    }

    /**
     *
     * @return neighbour country array
     */
    public Country[] getNeibourCountry(){
        return this.neighbour;
    }




}
