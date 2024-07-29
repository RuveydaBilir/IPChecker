# IPWizard
An OSINT tool that analyzes and scores IP addresses based on user-specified factors.

## Project Description
This OSINT tool, designed for Windows, offers comprehensive IP address analysis for security analysts by querying multiple databases. It retrieves the IP's ISP, country, recency of activity, score, and related domains and files. It then calculates a maliciousness score based on these factors and a user-specified restricted country and ISP list. Users have the flexibility to customize the weight of each factor, ensuring that the scoring system aligns with their specific security requirements and threat assessment criteria. After calculations, it returns the overall score percentage and the detailed information retrieved for the IP.

For this project, AbuseIPDB and VirusTotal APIs are used. 

## How to install
Commands for installation and initial set-up:
1. Clone the repository on your computer:
```git clone https://github.com/RuveydaBilir/IPWizard.git ```

2. Navigate to the project folder:
```cd IPWizard ```

3. Setup configuration: Type
```IPWizard.bat``` or ```.\IPWizard.bat``` on the command line.

This initial run will create "config.properties" file in the folder for user-specifications. Make sure to fill API areas for connections, and other areas based on your needs.

After set-up, you can start using the tool from terminal by typing the same commands.

## Considerations & Prerequisities
* Make sure java jdk and javac are installed.
* If you are using free public APIs, check their quotas. This app makes approximately 10 requests per IP.


## For API keys
Please visit and sign-up to these websites to have API keys (with free and premium options): 
* VirusTotal: https://www.virustotal.com/gui/home/upload
* AbuseIPDB: https://www.abuseipdb.com/
  
For their API documentation:
* VirusTotal: https://docs.virustotal.com/reference/overview , https://docs.virustotal.com/reference/ip-object 
* AbuseIPDB: https://www.abuseipdb.com/api.html
