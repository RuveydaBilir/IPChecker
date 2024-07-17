public class IPWizard {
    private static final String userPrefFileName = "user_pref.txt";
    private static final String knownIPsFileName = "known_abused_ips.txt";
    private static final String okCountryFileName = "allow_country.txt";
    private static final String okISPFileName = "allow_ISP.txt";
    private static UserPref userPref = new UserPref(userPrefFileName, okCountryFileName, okISPFileName);
    private static IPList ipList = new IPList(knownIPsFileName);
    
    public static void main(String[] args) {
        
    }
}
