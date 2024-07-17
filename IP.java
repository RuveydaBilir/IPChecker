public class IP {
    private String isp; 
    private String country; 
    private double score; // 0-100
    private String type; // Malicious, be careful, OK etc.
    private int abuseDBScore;
    private int vtScore;
    private int ipVoidScore;
    private final String ip;

    public IP(String newIp){
        ip = newIp;
        type = "OK";
        score = 0;
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
        double ipScore = ((abuseDBScore/100)+(vtScore/92)+(ipVoidScore/94))*(100/3);

        if(score<30){
            setType("Clean");
        }
        else if(score<50){
            setType("OK");
        }
        else if(score<80){
            setType("Suspicious");
        }
        else{
            setType("Malicious");
        }
    }
    private void setType(String newType){
        type = newType;
    }
    void setAbuseDBScore(int score){
        abuseDBScore = score;
    }
    void setVTScore(int score){
        vtScore = score;
    }
    void setIPVoidScore(int score){
        ipVoidScore = score;
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
    double getScore(){
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
