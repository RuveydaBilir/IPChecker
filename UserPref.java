
import java.util.ArrayList;

public class UserPref {
    private String userPrefFile;
    private String okCountryFile;
    private String okISPFile;
    private float abuseDBSev;
    private float vtSev;
    private float ipVoidSev;
    private ArrayList<String> okCountryList = new ArrayList<>();
    private ArrayList<String> okISPList = new ArrayList<>();

    public UserPref(String userPrefFileName, String okCountryFileName, String okISPFileName){
        userPrefFile = userPrefFileName;
        okCountryFile = okCountryFileName;
        okISPFile = okISPFileName;
    }

    //setters
    void setAbuseDBSev(float severity){
        abuseDBSev = severity;
    }
    void setvtSev(float severity){
        abuseDBSev = severity;
    }
    void setipVoidSev(float severity){
        abuseDBSev = severity;
    }
    void setOKCountryList(){
        //TODO: read file and add them to list
    }
    void setOKISPList(){
        //TODO: read file and add them to list
    }

    //Getters //TODO: Read file!
    float getAbuseDBSev(float severity){
        return abuseDBSev;
    }
    float getvtSev(float severity){
        return vtSev;
    }
    float getipVoidSev(float severity){
        return ipVoidSev;
    }
    ArrayList<String> getOKCountryList(){
        return okCountryList;
    }
    ArrayList<String> getOKISPList(){
        return okISPList;
    }
}
