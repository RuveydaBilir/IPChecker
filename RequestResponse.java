import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;

public class RequestResponse {
    private String abuseIPDB_api; // TODO: get these apis.
    private String vt_api;
    private String ipVoid_api;
    private final String ABUSE_IP_URL = "https://api.abuseipdb.com/"+abuseIPDB_api+"/v2/check"; 
    private final String VT_URL = ""; 
    private final String IPVOID_URL = ""; 
    private int abuseDBScore;
    private int vtScore;
    private int ipVoidScore;
    private IP ip;

    public RequestResponse(IP ip){
        this.ip = ip;
    }

    public StringBuilder sendGetRequest(String urlStr) throws Exception{ //, IP ip, Map<String, String> headers) throws Exception{
        URI uri = new URI(urlStr);
        URL url = uri.toURL();
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

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

        // TODO: this part differs for json and html. check chat. I really simplified it to try. (also I changed the function signature)
        getTitle(htmlContent); // TODO: this is for testing. Don't forget to remove.
        return htmlContent;
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

    private void setISPofIP(StringBuilder content){
        //set ip's isp according to the retrived data.
    }
    
    private void setCountryofIP(StringBuilder content){
        //set ip's country according to the retrived data.
    }

    private void setAbuseDBScore() throws Exception{
        StringBuilder adbContent = sendGetRequest(ABUSE_IP_URL);
        setISPofIP(adbContent);
        setCountryofIP(adbContent);
        int score = 0;
        ip.setAbuseDBScore(score);
    }
    private void setVTScore() throws Exception{
        StringBuilder vtContent = sendGetRequest(VT_URL);

        int score = 0;
        ip.setVTScore(score);
    }
    private void setIPVoidScore() throws Exception{
        StringBuilder ipvContent = sendGetRequest(IPVOID_URL);

        int score = 0;
        ip.setIPVoidScore(score);
    }

}
