import java.util.ArrayList;

public class IP {
    private String isp; 
    private String country; 
    private String detail;
    private double score; // 0-100
    private String type; // Malicious, be careful, OK etc.
    private int abuseDBScore;
    private int vtScore;
    private boolean isTor;
    private boolean isFromResCountry;
    private ArrayList<Integer> relationScores = new ArrayList<>();
    private final String ip;

    public IP(String newIp){
        ip = newIp;
        type = "OK";
        score = 0;
        detail = "";
    }
    
    //setters
    void setISP(String newISP){
        isp = newISP;
    }
    void setCountry(String newCountry){
        country = newCountry;
    }
    void setIsTor(boolean isTor){
        this.isTor=isTor;
    }
    void setIsFromResCount(boolean is){
        isFromResCountry = is;
    }
    void addDetail(String d){
        detail += d;
        detail +="\n";
    }
    void addRelationScore(int newScore){
        relationScores.add(newScore);
    }
    void updateScore(double scoreAdd){
        score = scoreAdd;

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

    int getAbuseDBScore(){
        return abuseDBScore;
    }
    int getVTScore(){
        return vtScore;
    }
    String getDetail(){
        return detail;
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
    boolean getIsTor(){
        return isTor;
    }
    ArrayList<Integer> getRelatedScoreList(){
        return relationScores;
    }

    void print(){
        System.out.println("-------------------------");
        System.out.println("IP: " + getIP());
        System.out.println("ISP: " + getISP());
        System.out.println("Country: " + getCountry());
        System.out.println("Status: " + getType());
        System.out.println("Is Tor: " + getIsTor());
        System.out.println("Overall Score: % " + String.format("%.2f", getScore()));
        System.out.println("-------------------------");
        System.out.println("DETAILS: ");
        System.out.println("AbuseIPDB Confidence Score: " + abuseDBScore +"/100");
        System.out.println("VirusTotal Confidence Score: " + vtScore +"/92");

        if(isFromResCountry){
            System.out.println("\n!!! The IP is from a restricted country.");
        }
        
        if(detail.equals("")){
            System.out.println("No relations found.");
        }
        else{
            System.out.println("\nThe IP is related with the following:");
            System.out.println(getDetail());
        }
        //System.out.println("IPVoid Confidence Score: " + ipVoidScore +"/96");
        System.out.println("-------------------------");
    }

}
