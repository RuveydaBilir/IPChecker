
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class UserPref {
    private int abuseDBSev;
    private int vtSev;
    private int ipVoidSev;
    private ArrayList<String> resCountryList = new ArrayList<>();
    private ArrayList<String> resIspList = new ArrayList<>();
    private ArrayList<String> allowCountryList = new ArrayList<>();
    private ArrayList<String> allowIspList = new ArrayList<>();
    private ArrayList<String> knownIPList = new ArrayList<>();

    public UserPref(){
        System.out.println("AAAAAA");
        setProperties();
    }

    private void setProperties(){
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            resCountryList = setPropertyLİst("RESTRICTED_COUNTRY", fis, prop);
            resIspList = setPropertyLİst("RESTRICTED_ISP", fis, prop);
            allowCountryList = setPropertyLİst("ALLOWED_COUNTRY", fis, prop);
            allowIspList = setPropertyLİst("ALLOWED_ISP", fis, prop);
            //knownIPList = setPropertyLİst("KNOWN_IPS", fis, prop);

            prop.load(fis);

            System.out.println(resCountryList);
            System.out.println(resIspList);
            System.out.println(knownIPList);

        } catch (IOException e) {
            System.err.println("ERROR: Setting properties failed. Please check config.properties file.");
        }
    }

    private ArrayList<String> setPropertyLİst(String key, FileInputStream fis, Properties prop) throws IOException{
        prop.load(fis);
        return convertToArrayList(prop.getProperty(key));
    }

    private ArrayList<String> convertToArrayList(String list){
        list = list.trim();
        String[] elements = list.replace("\"", "").split(",");

        // Convert the array to an ArrayList
        return new ArrayList<>(Arrays.asList(elements));
    }

    //setters
    void setAbuseDBSev(int severity){
        abuseDBSev = severity;
    }
    void setVTSev(int severity){
        vtSev = severity;
    }

    double getAbuseDBSev(){
        return abuseDBSev;
    }
    double getvtSev(){
        return vtSev;
    }
    ArrayList<String> getResCountryList(){
        return resCountryList;
    }
    ArrayList<String> getResISPList(){
        return resIspList;
    }
}
