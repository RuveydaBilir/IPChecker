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
    private final String ABUSE_IP_URL = "https://api.abuseipdb.com/api/v2/check?ipAddress="; 
    private final String VT_URL = "https://www.virustotal.com/api/v3/"; 
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
            vt_api = prop.getProperty("VT_API");

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
        
        if (!content.contains(searchKey)) {
            return "";
        }

        int start = content.indexOf(searchKey) + searchKey.length();

        if (start >= content.length()) {
            return "";
        }

        int end1 = content.indexOf(",", start);
        int end2 = content.indexOf("}", start);
        int end3 = content.indexOf(";", start);

        int end = Math.min(content.length(), Math.min(end1 != -1 ? end1 : content.length(),
                                                    end2 != -1 ? end2 : content.length()));

        if (end3 != -1) {
            end = Math.min(end, end3);
        }

        if (start > end) {
            return ""; 
        }

        return content.substring(start, end).trim();
    }

    private int extractIntValue(String content, String key){
        String value = extractValue(content, key);
        if(value.equals("")){
            return 0;
        }
        return Integer.parseInt(value);
    }

    public void sendGetRequestAbuseDB() throws Exception{ // Should I add this?!: String urlStr, Map<String, String> headers
        String urlStr = ABUSE_IP_URL + ip.getIP();
        String jsonContent= sendGetRequest(urlStr,"Key",abuseIPDB_api).toString();

        int abuseScore = extractIntValue(jsonContent, "abuseConfidenceScore");
        String coutry = extractValue(jsonContent, "countryCode");
        String isp = extractValue(jsonContent, "isp");
        String isTor = extractValue(jsonContent, "isTor");
        ip.setAbuseDBScore(abuseScore);
        ip.setCountry(coutry);
        ip.setISP(isp);
        ip.setIsTor(isTor);

       // System.out.println(jsonContent);
    }  
    
    public void sendGetRequestVTforIP() throws Exception{
        String urlStr = VT_URL + "ip_addresses/" + ip.getIP();
        String jsonContent= sendGetRequest(urlStr,"x-apikey",vt_api).toString();
        //System.out.println(jsonContent);
        String date = extractValue(jsonContent, "last_analysis_date");
        if(date.length()>0){
            ip.setLastDate(Long.parseLong(date));
        }
        //System.out.println(jsonContent);
        int index = jsonContent.indexOf("last_analysis_stats", 0);
        jsonContent = jsonContent.substring(index);
        int abuseScore = extractIntValue(jsonContent, "malicious")+extractIntValue(jsonContent, "suspicious");
        ip.setVTScore(abuseScore);

        sendGetRequestVTforResolutions();
        sendGetRequestVTforComFiles();
        sendGetRequestVTforRefFiles();
    }

    public void sendGetRequestVTforResolutions() throws Exception{
        String urlStr = VT_URL + "ip_addresses/" + ip.getIP() + "/relationships/resolutions";
        String jsonContent= sendGetRequest(urlStr,"x-apikey",vt_api).toString();
        //System.out.println(jsonContent);
        int count = extractIntValue(jsonContent, "count");
        System.out.println("Number of resolution domains: " + count);
        for(int i=0; i<count && i<3; i++){
            
            String fileID = extractValue(jsonContent, "id");
            jsonContent = jsonContent.substring(jsonContent.indexOf(fileID));
            sendGetRequestVTforDomain(fileID);
        }
    }

    public void sendGetRequestVTforComFiles() throws Exception{
        String urlStr = VT_URL + "ip_addresses/" + ip.getIP() + "/relationships/communicating_files";
        //referrer_files
        String jsonContent= sendGetRequest(urlStr,"x-apikey",vt_api).toString();
        //System.out.println(jsonContent);
        int count = extractIntValue(jsonContent, "count");
        System.out.println("Number of communicating files: " + count);
        for(int i=0; i<count && i<3 ; i++){
            String fileID = extractValue(jsonContent, "id");
            jsonContent = jsonContent.substring(jsonContent.indexOf(fileID));
            sendGetRequestVTforFile(fileID);
        }
    }

    public void sendGetRequestVTforRefFiles() throws Exception{
        String urlStr = VT_URL + "ip_addresses/" + ip.getIP() + "/relationships/referrer_files";
        //referrer_files
        String jsonContent= sendGetRequest(urlStr,"x-apikey",vt_api).toString();
        //System.out.println(jsonContent);
        int count = extractIntValue(jsonContent, "count");
        System.out.println("Number of referrer files: " + count);
        for(int i=0; i<count && i<3 ; i++){
            String fileID = extractValue(jsonContent, "id");
            jsonContent = jsonContent.substring(jsonContent.indexOf(fileID));
            sendGetRequestVTforFile(fileID);
        }
    }

    public void sendGetRequestVTforFile(String fileID) throws Exception{
        String rule = "";
        String urlStr = VT_URL + "files/" + fileID;
        urlStr = urlStr.replace("\"", "");
        //String urlStr = VT_URL + "files/" + fileID + "/relationships/communicating_files";
        String jsonContent= sendGetRequest(urlStr,"x-apikey",vt_api).toString();
        String name = extractValue(jsonContent, "meaningful_name");
        //System.out.println(jsonContent);
        
        if(jsonContent.contains("type_description")){
            rule = extractValue(jsonContent, "type_description");
        }
        /*
        if(jsonContent.contains("rule_category")){
            rule = extractValue(jsonContent, "rule_category");
        }
            */
        else if(jsonContent.contains("ruleset_name")){
            rule = extractValue(jsonContent, "ruleset_name");
        }
        int index = jsonContent.indexOf("last_analysis_stats", 0);
        jsonContent = jsonContent.substring(index);
        int malScore = extractIntValue(jsonContent, "malicious")+extractIntValue(jsonContent, "suspicious");

        //System.out.println("Malware Score: " + malScore);
        String detail = """
                        File ID:\s""" + fileID.replace("\"", "").trim()
                        + "\n\tKnown Name: " + name
                        + "\n\tVirusTotal Score: " + malScore +"/92"
                        + "\n\tCategory: " + rule;
        
        ip.addDetail(detail);
        ip.addRelationScore(malScore);
    } 

    public void sendGetRequestVTforDomain(String domainID) throws Exception{
        String newDomain = domainID.replace(ip.getIP(), "");

        String urlStr = VT_URL + "domains/" + newDomain;
        urlStr = urlStr.replace("\"", "");
        //String urlStr = VT_URL + "files/" + fileID + "/relationships/communicating_files";
        String jsonContent= sendGetRequest(urlStr,"x-apikey",vt_api).toString();
        //System.out.println(jsonContent);
        int index = jsonContent.indexOf("last_analysis_stats", 0);
        jsonContent = jsonContent.substring(index);
        int malScore = extractIntValue(jsonContent, "malicious")+extractIntValue(jsonContent, "suspicious");
        String detail = """
                        Domain Name:\s""" + newDomain.replace("\"", "").trim()
                        + "\n\tVirusTotal Score: " + malScore +"/92";
                        //+ "\n\tRule: " + rule;
        
        ip.addDetail(detail);
        ip.addRelationScore(malScore);
    } 

    public void testFunc() throws Exception{
        //String newDomain = domainID.replace(ip.getIP(), "");
        String urlStr = VT_URL + "ip_addresses/" + ip.getIP() + "/resolutions";
        urlStr = urlStr.replace("\"", "");
        //String urlStr = VT_URL + "files/" + fileID + "/relationships/communicating_files";
        String jsonContent= sendGetRequest(urlStr,"x-apikey",vt_api).toString();

        System.out.println(jsonContent);
    }

    public void getTitle(StringBuilder htmlContent){ //Test amaçlı
        Pattern titlePattern = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE);
        Matcher titleMatcher = titlePattern.matcher(htmlContent.toString());
        if (titleMatcher.find()) {
            System.out.println(titleMatcher.group(1));
        } else {
            throw new RuntimeException("No title found in the HTML content");
        }

    }
}
