package hugues.marchal.cheaty.Classes;

import android.bluetooth.BluetoothDevice;

/**
 * Created by makarov on 09/04/16.
 */
public class BluetoothDev extends Item {
    public static String deviceAddress = "00:00:00:00:00:00";
    public static String deviceName = "";
    private static String discoveryTime;


    /**
     * This method will link the BluetoothDev object to the discovered BluetoothDevice and put the BluetoothDevice characteristics inside of the BluetoothDev object for later use.
     * @param device the BluetoothDevice object from which the data is extracted.
     * @precondition device is not null
     * @postcondition this contains the deviceAddress, the deviceName and the discoveryTime
     */
    public BluetoothDev(BluetoothDevice device) {
        assert device!=null;
        deviceAddress = device.getAddress();
        deviceName = device.getName();
        discoveryTime = SaveItem.getDateNTime();
    }

    /**
     * This method gives the Bluetooth device's name.
     * @return String deviceName
     */
    public String getDeviceName(){
        return deviceName;
    }

    /**
     * This method gives the Bluetooth device's MAC address
     * @return String deviceAddress
     */
    public String getDeviceAddress(){
        return deviceAddress;
    }


    /**
     * This method gives the time at which the device has been saved
     * @return String saveTime
     */
    public String getDiscoveryTime(){
        return discoveryTime;
    }



}
