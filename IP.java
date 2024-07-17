public class IP {
    private String isp; 
    private String country; 
    private float score; // 0-100
    private String type; // Malicious, careful, OK etc.
    private final String ip;

    public IP(String newIp, String country, String isp){
        ip = newIp;
        this.country = country;
        this.isp = isp;
        type = "OK";
    }
    
    //setters
    void setISP(String newISP){
        isp = newISP;
    }
    void setCountry(String newCountry){
        country = newCountry;
    }
    void setScore(){
        // TODO: Math calculations
        // setType(..)
    }
    private void setType(String newType){
        type = newType;
    }

    //Getters
    String getISP(){
        return isp;
    }
    String getIP(){
        return ip;
    }
    String getCountry(){
        return country;
    }
    String getType(){
        return type;
    }
    float getScore(){
        return score;
    }

    void checkDB(){
        // Maybe these can be also ips feature
        // TODO: reqRes classtan sonra tanımlanacak
        // float AbuseDB score = ;
        // float VirusTotal score = ;
        // float IPVoid score = ;
    }

}
