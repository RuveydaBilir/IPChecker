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
    //private String ipVoid_api;
    private final String ABUSE_IP_URL = "https://api.abuseipdb.com/api/v2/check?ipAddress="; 
    private final String VT_URL = "https://www.virustotal.com/api/v3/ip_addresses/"; 
    private final IP ip;

    public RequestResponse(IP ip) throws IOException{
        this.ip = ip;
        setAPIs();
    }

    public final void setAPIs() throws IOException{
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            prop.load(fis);
            abuseIPDB_api = prop.getProperty("ABUSEDB_API");
            //System.out.println("AbuseIPDB API: " + abuseIPDB_api);
            vt_api = prop.getProperty("VT_API");
            //System.out.println("VT API: " + vt_api);

        } catch (IOException e) {
            System.err.println("ERROR: Reading API keys failed. Please check config.properties file.");
        }
    }
    private StringBuilder sendGetRequest(String urlStr,String keyProperty, String api) throws Exception{
        URI uri = new URI(urlStr);
        URL url = uri.toURL();
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty(keyProperty, api);

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
        String urlStr = ABUSE_IP_URL + ip.getIP();
        String jsonContent= sendGetRequest(urlStr,"Key",abuseIPDB_api).toString();

        int abuseScore = Integer.parseInt(extractValue(jsonContent, "abuseConfidenceScore"));
        String coutry = extractValue(jsonContent, "countryCode");
        String isp = extractValue(jsonContent, "isp");
        boolean isTor = Boolean.parseBoolean(extractValue(jsonContent, "isTor"));
        ip.setAbuseDBScore(abuseScore);
        ip.setCountry(coutry);
        ip.setISP(isp);
        ip.setIsTor(isTor);

        //System.out.println(jsonContent);
    }  
    
    public void sendGetRequestVT() throws Exception{ // Should I add this?!: String urlStr, Map<String, String> headers
        String urlStr = VT_URL + ip.getIP();
        String jsonContent= sendGetRequest(urlStr,"x-apikey",vt_api).toString();
        System.out.println(jsonContent);
        int index = jsonContent.indexOf("last_analysis_stats", 0);
        jsonContent = jsonContent.substring(index);
        //System.out.println(jsonContent);

        int abuseScore = Integer.parseInt(extractValue(jsonContent, "malicious"))+Integer.parseInt(extractValue(jsonContent, "suspicious"));

        ip.setVTScore(abuseScore);
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
}
