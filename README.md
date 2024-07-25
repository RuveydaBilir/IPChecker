# IPChecker
A Java terminal application that analyzes and scores IP addresses based on user-specified factors.

## Project Description
This Java terminal application, designed for Windows, offers comprehensive IP address analysis for security analysts by querying multiple databases. It retrieves the IP's ISP, country, recency of its activity, score, and related domains and files. It then calculates a maliciousness score based on these factors and a user-specified restricted country and ISP list. Users have the flexibility to customize the weight of each factor, ensuring that the scoring system aligns with their specific security requirements and threat assessment criteria. After calculations, it returns an overall score percentage and the detailed information retrieved for the IP.

For this project, AbuseIPDB and VirusTotal APIs are used. 

## How to install

## How to use
After installation, first run the program, this will create "config.properties" file in the folder for user-specifications. Make sure to fill API areas for connections, and other areas based on your needs. 

## Considerations


## For API keys
Please visit and sign-up to these websites to have free API keys: 
* VirusTotal: https://www.virustotal.com/gui/home/upload
* AbuseIPDB: https://www.abuseipdb.com/
  
For their API documentation:
* VirusTotal: https://docs.virustotal.com/reference/overview , https://docs.virustotal.com/reference/ip-object 
* AbuseIPDB: https://www.abuseipdb.com/api.html
