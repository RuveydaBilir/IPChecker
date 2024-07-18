import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPWizard {
    private static final String userPrefFileName = "user_pref.txt";
    private static final String knownIPsFileName = "known_abused_ips.txt";
    private static final String okCountryFileName = "allowed_countries.txt";
    private static final String okISPFileName = "allowed_ISPs.txt";
    private static final String test_url = "https://example.com/";
    private static UserPref userPref = new UserPref(userPrefFileName, okCountryFileName, okISPFileName);
    private static IPList ipList = new IPList(knownIPsFileName);

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
    
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to the IP Wizard.");
        System.out.println("It can check every IP score you need.\n");
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the IPv4 address: ");
        String ipStr = scan.nextLine();
        IP ip = new IP(correctIP(ipStr));
        System.out.println("Checking IPv4 address " + ip.getIP());
        System.out.println("-------------------------");
        
        RequestResponse reqRes = new RequestResponse(ip);
        reqRes.sendGetRequestAbuseDB();
        reqRes.sendGetRequestVT();
        ip.print();
    }
}
