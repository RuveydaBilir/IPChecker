import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static final String CONFIG_FILE = "config.prop"; //not to lose actual one change this after last commit
    private final Properties properties;
    //TODO: NOT ADDING PROPERTİES IN ORDER ALSO I WANT TO ADD COMMENT BETWEEN SOME LINED

    public ConfigManager() throws IOException {
        properties = new Properties();
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        } else {
            loadConfig(configFile);
        }
    }

    private void createDefaultConfig(File configFile) throws IOException {
        properties.setProperty("ABUSEDB_API", "");
        properties.setProperty("VT_API", "");
        properties.setProperty("RESTRICTED_COUNTRY", "");
        properties.setProperty("RESTRICTED_ISP", "");
        properties.setProperty("ABUSEDB_SEV", "30");
        properties.setProperty("VT_SEV", "35");
        properties.setProperty("COUNTRY_ISP_SEV", "2");
        properties.setProperty("LAST_DATE_SEV", "3");
        properties.setProperty("RELATED_SEV", "30");

        FileOutputStream out = new FileOutputStream(configFile);
        properties.store(out, "Default configuration");
        
    }

    private void loadConfig(File configFile) throws IOException {
        FileInputStream in = new FileInputStream(configFile);
        properties.load(in);
        
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) throws IOException {
        properties.setProperty(key, value);
        FileOutputStream out = new FileOutputStream(CONFIG_FILE);
        properties.store(out, "Updated configuration");
    }
}
