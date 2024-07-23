import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class IP {
    private String isp; 
    private String country; 
    private String detail;
    private long timestamp;
    private double score; // 0-100
    private String type; 
    private int abuseDBScore;
    private int vtScore;
    private boolean isTor;
    private boolean isFromResCountry;
    private boolean isFromResISP;
    private boolean isActive;
    private final ArrayList<Integer> relationScores = new ArrayList<>();
    private final String ip;

    public IP(String newIp){
        ip = newIp;
        type = "OK";
        score = 0;
        detail = "";
        timestamp=0;
        isFromResCountry=false;
        isFromResISP=false;
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

        if(score<10){
            setType("Clean");
        }
        else if(score<30){
            setType("OK");
        }
        else if(score<60){
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
    void setIsFromResISP(boolean is){
        isFromResISP = is;
    }
    void setLastDate(long time){
        this.timestamp = time;
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
    boolean isActive(){
        return isActive;
    }
    ArrayList<Integer> getRelatedScoreList(){
        return relationScores;
    }
    String getDuration(){
        String output;
        /*
        ZoneId zoneId = ZoneId.systemDefault(); // Get the system's default time zone
        Instant ipInstant = Instant.ofEpochSecond(timestamp);
        ZonedDateTime ipZonedDateTime = ZonedDateTime.ofInstant(ipInstant, zoneId);
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now(zoneId);

        Duration duration = Duration.between(ipZonedDateTime, currentZonedDateTime);
        Instant compCurrent = Instant.now();
        Instant ipInstant = Instant.ofEpochSecond(timestamp);
        Duration duration = Duration.between(ipInstant, compCurrent);
        */

        ZoneId systemTimeZone = ZoneId.systemDefault();

        Instant ipInstant = Instant.ofEpochSecond(timestamp);
        ZonedDateTime ipZonedDateTime = ZonedDateTime.ofInstant(ipInstant, systemTimeZone);

        ZonedDateTime currentZonedDateTime = ZonedDateTime.now(systemTimeZone);

        Duration duration = Duration.between(ipZonedDateTime, currentZonedDateTime);

        if(duration.isNegative()){
            duration = duration.negated();
        }
        
        long seconds = duration.getSeconds();
        long years = seconds / (365 * 24 * 3600);
        long remainingSecondsAfterYears = seconds % (365 * 24 * 3600);
        long days = remainingSecondsAfterYears / (24 * 3600);
        long hours = (remainingSecondsAfterYears % (24 * 3600)) / 3600;
        long minutes = (remainingSecondsAfterYears % 3600) / 60;

        if(years>0){
            isActive = false;
            output = "More than 1 year ago";
        }
        else if(days>210){
            isActive = false;
            output = "More than 7 months ago";
        }
        else{
            isActive = true;
            output =  days + " days, " + hours + " hours, " + minutes + " minutes ago";
        }
        //System.out.println("Difference: " + years + " years, " + days + " days, " + hours + " hours, " + minutes + " minutes, ");
        return output;

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
        String duration = getDuration();
        System.out.println("Is Active: " + isActive);
        System.out.println("[Last Analysis: " +duration+"] ");
        if(isFromResCountry){
            System.out.println("\nThis IP is from a restricted country.");
        }
        if(isFromResISP){
            System.out.println("\nThis IP is from a restricted ISP.");
        }
        
        if(detail.equals("")){
            System.out.println("No relations found.");
        }
        else{
            System.out.println("\nThe IP is related with the following:");
            System.out.println(getDetail());
        }
        System.out.println("-------------------------");
    }

}
