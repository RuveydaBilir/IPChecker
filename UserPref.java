
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class UserPref {
    private int abuseDBSev;
    private int vtSev;
    private int countryISPSev;
    private int dateSev;
    private int relatedSev;
    private String resCountryList;
    private String resIspList;
    //private ArrayList<String> resCountryList = new ArrayList<>();
    //private ArrayList<String> resIspList = new ArrayList<>();

    public UserPref(){
        setProperties();
    }

    private void setProperties(){
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            prop.load(fis);
            //resCountryList = setPropertyList("RESTRICTED_COUNTRY", fis, prop);
            //resIspList = setPropertyList("RESTRICTED_ISP", fis, prop);

            resCountryList = prop.getProperty("RESTRICTED_COUNTRY");
            resIspList = prop.getProperty("RESTRICTED_ISP");

            //System.out.println("Country list: " + resCountryList);
            //System.out.println("ISP list: " + resIspList);

            countryISPSev = Integer.parseInt(prop.getProperty("COUNTRY_ISP_SEV","5").trim());
            abuseDBSev = Integer.parseInt(prop.getProperty("ABUSEDB_SEV","30").trim());
            vtSev = Integer.parseInt(prop.getProperty("VT_SEV","35").trim());
            dateSev = Integer.parseInt(prop.getProperty("LAST_DATE_SEV","10").trim());
            relatedSev = Integer.parseInt(prop.getProperty("RELATED_SEV","20").trim());

            //knownIPList = setPropertyList("KNOWN_IPS", fis, prop);
            //System.out.println(resCountryList);
            //System.out.println(resIspList);
            //System.out.println(knownIPList);

        } catch (IOException e) {
            System.err.println("ERROR: Setting properties failed. Please check config.properties file.");
        }
    }

    private ArrayList<String> setPropertyList(String key, FileInputStream fis, Properties prop) throws IOException{
        prop.load(fis);
        return convertToArrayList(prop.getProperty(key));
    }

    private ArrayList<String> convertToArrayList(String list){
        list = list.trim();
        String[] elements = list.replace("\"", "").split(",");

        // Convert the array to an ArrayList
        return new ArrayList<>(Arrays.asList(elements));
    }

    int getAbuseDBSev(){
        return abuseDBSev;
    }
    int getvtSev(){
        return vtSev;
    }
    int getCountryISPSev(){
        return countryISPSev;
    }
    int getDateSev(){
        return dateSev;
    }
    int getRelatedSev(){
        return relatedSev;
    }

    String getResCountryList(){
        return resCountryList;
    }
    String getResISPList(){
        return resIspList;
    }

    /*
    ArrayList<String> getResCountryList(){
        return resCountryList;
    }
    ArrayList<String> getResISPList(){
        return resIspList;
    }
     */
}
