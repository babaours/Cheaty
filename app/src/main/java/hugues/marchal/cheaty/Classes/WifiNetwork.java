package hugues.marchal.cheaty.Classes;

import android.net.wifi.ScanResult;

/**
 * Created by makarov on 09/04/16.
 */
public class WifiNetwork extends Item{

    private String ssid;
    private String bssid;
    private String capabilities;
    private int level;
    private int frequency;
    private String discoveryTime;

    public WifiNetwork (ScanResult scanResult){
        ssid = scanResult.SSID;
        bssid = scanResult.BSSID;
        capabilities = scanResult.capabilities;
        level = scanResult.level;
        frequency = scanResult.frequency;
        discoveryTime = SaveItem.getDateNTime();
    }

    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public int getLevel() {
        return level;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getDiscoveryTime() {
        return discoveryTime;
    }
}
