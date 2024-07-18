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
    void updateScore(int scoreAdd){
        score = ((abuseDBScore/100)+(vtScore/92))*(100/2);

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

    void print(){
        System.out.println("-------------------------");
        System.out.println("IP: " + getIP());
        System.out.println("ISP: " + getISP());
        System.out.println("Country: " + getCountry());
        System.out.println("Status: " + getType());
        System.out.println("Overall Score: " + getScore());
        System.out.println("-------------------------");
        System.out.println("DETAILS: ");
        System.out.println("AbuseIPDB Confidence Score: " + abuseDBScore +"/100");
        System.out.println("VirusTotal Confidence Score: " + vtScore +"/92");
        //System.out.println("IPVoid Confidence Score: " + ipVoidScore +"/96");
        System.out.println("-------------------------");
    }

}
