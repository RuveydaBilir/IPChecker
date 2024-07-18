import java.util.ArrayList;

public class IPList {
    private ArrayList<IP> ipList = new ArrayList<>();
    private ArrayList<IP> newIps = new ArrayList<>();
    private String ipFile;

    public IPList(String ipFileName){
        setIPList();
        ipFile = ipFileName;
        //System.out.println("IP list object created");
    }

    private boolean  setIPList(){
        // TODO: set IP list by reading the file, if successful return true

        return false;
    }

    boolean setIPFile(){
        // TODO: write ipList back to file. (use newIps to avoid unnecessary actions)
        return true;
    }

    boolean isIPExist(IP ip){
        return ipList.contains(ip);
    }

    ArrayList<IP> getIPList(){
        return ipList;
    }

    boolean addIP(IP ip){
        if(ipList.contains(ip)){
            return false;
        }
        newIps.add(ip);
        return ipList.add(ip);
    }
}
