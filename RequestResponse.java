import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;

public class RequestResponse {
    private String abuseIPDB_api;
    private String vt_api;
    private String ipVoid_api;
    private final String ABUSE_IP_URL = "https://api.abuseipdb.com/api/v2/check?ipAddress="; 
    private final String VT_URL = ""; 
    private final String IPVOID_URL = ""; 
    private int abuseDBScore;
    private int vtScore;
    private int ipVoidScore;
    private IP ip;

    public RequestResponse(IP ip){
        this.ip = ip;
    }

    public void setAPIs() throws IOException{
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            prop.load(fis);
            abuseIPDB_api = prop.getProperty("ABUSEDB_API");
            System.out.println("AbuseIPDB API: " + abuseIPDB_api);
        } catch (IOException e) {
            System.err.println("ERROR: Reading API keys failed. Please check config.properties file.");
        }
    }
    private StringBuilder sendGetRequest(String urlStr) throws Exception{
        URI uri = new URI(urlStr);
        URL url = uri.toURL();
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Key", abuseIPDB_api);

        int responseCode = connection.getResponseCode();
        if(responseCode != HttpURLConnection.HTTP_OK){
            throw new RuntimeException("GET request for "+urlStr+" failed. Response code: " + responseCode);
        }

        // Reading html part
        StringBuilder htmlContent = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                htmlContent.append(inputLine);
            }
        }
        return htmlContent;
    }

    private String extractValue(String content, String key){
        String searchKey = "\"" + key + "\":";
        int start = content.indexOf(searchKey) + searchKey.length();
        int end = content.indexOf(",", start);
        return content.substring(start,end).trim();
    }

    public void sendGetRequestAbuseDB() throws Exception{ // Should I add this?!: String urlStr, Map<String, String> headers
        setAPIs();
        String urlStr = ABUSE_IP_URL + ip.getIP();
        String jsonContent= sendGetRequest(urlStr).toString();

        int abuseScore = Integer.parseInt(extractValue(jsonContent, "abuseConfidenceScore"));
        String coutry = extractValue(jsonContent, "countryCode");
        String isp = extractValue(jsonContent, "isp");

        ip.setAbuseDBScore(abuseScore);
        ip.setCountry(coutry);
        ip.setISP(isp);

        System.out.println("abuse Confidence Score: " + abuseScore);
        System.out.println("country Code: " + coutry);
        System.out.println("isp: " + isp);
        ip.print();
    }        


    public void getTitle(StringBuilder htmlContent){
        Pattern titlePattern = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE);
        Matcher titleMatcher = titlePattern.matcher(htmlContent.toString());
        if (titleMatcher.find()) {
            System.out.println(titleMatcher.group(1));
        } else {
            throw new RuntimeException("No title found in the HTML content");
        }

    }

    private void setISPofIP(String content){
        //set ip's isp according to the retrived data.
    }
    
    private void setCountryofIP(String content){
        //set ip's country according to the retrived data.
    }

    private void setAbuseDBScore() throws Exception{
        //StringBuilder adbContent = sendGetRequest(ABUSE_IP_URL);
        //setISPofIP(adbContent);
        //setCountryofIP(adbContent);
        int score = 0;
        ip.setAbuseDBScore(score);
    }
    private void setVTScore() throws Exception{
        //StringBuilder vtContent = sendGetRequest(VT_URL);

        int score = 0;
        ip.setVTScore(score);
    }
    private void setIPVoidScore() throws Exception{
        //StringBuilder ipvContent = sendGetRequest(IPVOID_URL);

        int score = 0;
        ip.setIPVoidScore(score);
    }

}
