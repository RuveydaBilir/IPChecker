
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class UserPref {
    private int abuseDBSev;
    private int vtSev;
    private int countryISPSev;
    private int dateSev;
    private int relatedSev;
    private String resCountryList;
    private String resIspList;

    public UserPref(){
        setProperties();
    }

    private void setProperties(){
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            prop.load(fis);

            resCountryList = prop.getProperty("RESTRICTED_COUNTRY").trim();
            resIspList = prop.getProperty("RESTRICTED_ISP").trim();

            countryISPSev = Integer.parseInt(prop.getProperty("COUNTRY_ISP_SEV","5").trim());
            abuseDBSev = Integer.parseInt(prop.getProperty("ABUSEDB_SEV","30").trim());
            vtSev = Integer.parseInt(prop.getProperty("VT_SEV","35").trim());
            dateSev = Integer.parseInt(prop.getProperty("LAST_DATE_SEV","10").trim());
            relatedSev = Integer.parseInt(prop.getProperty("RELATED_SEV","20").trim());

        } catch (IOException e) {
            System.err.println("ERROR: Setting properties failed. Please check config.properties file.");
        }
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
}
