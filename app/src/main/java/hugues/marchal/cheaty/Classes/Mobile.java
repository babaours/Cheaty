package hugues.marchal.cheaty.Classes;

import android.telephony.TelephonyManager;

/**
 * Created by makarov on 09/04/16.
 */
public class Mobile extends Item{
    private static int networkType;
    private static String version = "2";
    private static String netwkType;

    private static void findNetworkDetails(TelephonyManager telephonyManager){
        networkType = telephonyManager.getNetworkType();
        switch (networkType){
            case TelephonyManager.NETWORK_TYPE_EDGE:
            {
                netwkType = "EDGE ";
                version = "2.5";
                break;}
            case TelephonyManager.NETWORK_TYPE_GPRS:
            {
                netwkType = "GPRS ";
                break;}
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            {
                netwkType = "HSDPA ";
                version = "3";
                break;}
            case TelephonyManager.NETWORK_TYPE_HSPA:
            {
                netwkType = "HSPA ";
                version = "3";
                break;}
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            {
                netwkType = "HSPAP ";
                version ="3";
                break;}
            case TelephonyManager.NETWORK_TYPE_UMTS:
            {
                netwkType = "UMTS ";
                version = "3";
                break;}
            case TelephonyManager.NETWORK_TYPE_LTE:
            {
                netwkType = "LTE ";
                version = "4";
                break;}
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            {
                netwkType = "UNKNOWN ";
                version = "0";
                break;}
        }
    }

    public static String getNetworkType(TelephonyManager telephonyManager){
        findNetworkDetails(telephonyManager);
        String str = netwkType+"("+version+"G)";
        return str;
    }
}
