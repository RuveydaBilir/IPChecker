import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPWizard {
    //private static final String test_url = "https://example.com/";
    private static final UserPref userPref = new UserPref();
    //private static IPList ipList = new IPList();

    private static String correctIP(String ipStr) throws Exception{
        String regex = "^\\d+\\.\\d+\\.\\d+\\.\\d+$";
        Pattern pattern = Pattern.compile(regex);
        String letterRegex = "[a-zA-Z]";
        String charRegex = "[^0-9.]";

        while(true){
            Matcher matcher = pattern.matcher(ipStr);
            boolean isIpPattern = matcher.matches();

            if(isIpPattern){
                return ipStr;
            }

            if(ipStr.contains(" ")){
                ipStr = ipStr.replaceAll(" ", "");
            }
            else if(ipStr.matches(".*" + charRegex + ".*")){
                ipStr = ipStr.replaceAll(charRegex, "");
            }
            else if(ipStr.matches(".*" + letterRegex + ".*")){
                ipStr = ipStr.replaceAll(letterRegex, "");
            }
            else{
                throw new Exception("Error: Wrong IP format.");
            }

        }
    }

    private static void calculate(IP ip) {
        double abuseDBSev = userPref.getAbuseDBSev();
        double vtSev = userPref.getvtSev();
        double totalSev = abuseDBSev + vtSev;
    
        double abuseDBWeight = abuseDBSev / totalSev;
        double vtWeight = vtSev / totalSev;
    
        double abuseDBNormalizedScore = ip.getAbuseDBScore() / 100.0;
        double vtNormalizedScore = ip.getVTScore() / 92.0;
    
        double score = (abuseDBNormalizedScore * abuseDBWeight + vtNormalizedScore * vtWeight) * 100;
    
        if (userPref.getResCountryList().contains(ip.getCountry())) {
            score = (score + 50) / 2;
            ip.setIsFromResCount(true);
        } else {
            ip.setIsFromResCount(false);
        }
    
        ip.updateScore(score);
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to the IP Wizard.");
        System.out.println("It can check every IP score you need.\n");
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the IPv4 address: ");
        String ipStr = scan.nextLine();
        IP ip = new IP(correctIP(ipStr));
        System.out.println("Checking IPv4 address " + ip.getIP());
        
        RequestResponse reqRes = new RequestResponse(ip);
        reqRes.sendGetRequestAbuseDB();
        reqRes.sendGetRequestVT();
        calculate(ip);
        ip.print();
    }
}

/*
 * private static void calculate(IP ip){
        double totalSev = userPref.getAbuseDBSev()+userPref.getvtSev();
        double score = (((ip.getAbuseDBScore()/100)*(userPref.getAbuseDBSev()/totalSev))+((ip.getVTScore()/92)*(userPref.getAbuseDBSev()/totalSev)))*(100/2);

        if(userPref.getResCountryList().contains(ip.getCountry())){
            score = (score*100 + 50)/200;
            ip.setIsFromResCount(true);
        }
        else{
            ip.setIsFromResCount(false);
        }
        //((abuseDBScore/100)+(vtScore/92))*(100/2);

        ip.updateScore(score);
    }
 */
