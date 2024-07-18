
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class UserPref {
    private String userPrefFile;
    private String okCountryFile;
    private String okISPFile;
    private int abuseDBSev;
    private int vtSev;
    private int ipVoidSev;
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<String> ispList = new ArrayList<>();
    private ArrayList<String> knownIPList = new ArrayList<>();

    public UserPref(String userPrefFileName, String okCountryFileName, String okISPFileName){
        //System.out.println("UserPref created.");
        userPrefFile = userPrefFileName;
        okCountryFile = okCountryFileName;
        okISPFile = okISPFileName;
        setProperties();
    }

    private void setProperties(){
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            countryList = setPropertyLİst("RESTRICTED_COUNTRY", fis, prop);
            ispList = setPropertyLİst("RESTRICTED_ISP", fis, prop);
            knownIPList = setPropertyLİst("KNOWN_IPS", fis, prop);

            prop.load(fis);

            System.out.println(countryList);
            System.out.println(ispList);
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
        abuseDBSev = severity;
    }
    void setipVoidSev(int severity){
        abuseDBSev = severity;
    }

    double getAbuseDBSev(){
        return abuseDBSev;
    }
    double getvtSev(){
        return vtSev;
    }
    double getipVoidSev(){
        return ipVoidSev;
    }
    ArrayList<String> getOKCountryList(){
        return countryList;
    }
    ArrayList<String> getOKISPList(){
        return ispList;
    }
}
