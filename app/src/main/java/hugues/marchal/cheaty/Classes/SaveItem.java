package hugues.marchal.cheaty.Classes;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by makarov on 08/04/16.
 */
public class SaveItem {
    private static final String bluetoothFile = "BluetoothDevices.txt";
    private static final String wifiFile = "WiFiDevices.txt";
    private static final String mobileFile = "MobileDevices.txt";
    private static String dateDir;
    private static String timeDir;

    static Calendar calendar = Calendar.getInstance();

    /**
     * This method will create a string containing the current date followed (YYYY-MM-DD;) by the current time (HH:MM:SS)
     * @return complete current date and time
     * @precondition dateDir and timeDir are well created
     * @postcondition dateDir contains the current date at the format YYYY-MM-DD and timeDir contains the current time at the format HH:MM:SS
     */
    public static String getDateNTime (){
        dateDir = String.valueOf(calendar.get(Calendar.YEAR))+"-";
        dateDir = dateDir + String.valueOf(calendar.get(Calendar.MONTH)+1)+"-";
        dateDir = dateDir + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+" ; ";
        timeDir = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":";
        timeDir = timeDir + String.valueOf(calendar.get(Calendar.MINUTE))+":";
        timeDir = timeDir + String.valueOf(calendar.get(Calendar.SECOND));
        return dateDir+timeDir;
    }

    /**
     * This method will allow the user to save a Bluetooth device's characteristics (name and MAC) into a file, preceded by the complete time at which the device is saved.
     * @param listToSave the list containing all BluetoothDev objects to save
     * @return boolean (true) if the data has been saved
     * @precondition bluetoothFile is not empty and contains the path to the file in which data is gonna be saved.
     * @postcondition the file contains at least all the data of listToSave.
     */
    public static Boolean saveBluetoothDevice(ArrayList<String> listToSave) throws IOException {
        assert !bluetoothFile.equals(null);
        assert !bluetoothFile.equals("");
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Cheaty2_Trace");
        myDir.mkdirs();
        File file = new File (myDir, bluetoothFile);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter writer = new PrintWriter(fos);
            for (int i = 0; i<listToSave.size();i++){
                writer.println();
                writer.println(listToSave.get(i));
            }
            fos.flush();
            writer.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }



    /**
     * This method will save an ArrayList of ScanResult (from WiFi) into the wifi designated file.
     * @param listToSave the list which is gonna be saved into the file
     * @param context the context in which the method is being used
     * @return boolean (true) is the data has been saved
     * @precondition wifiFile is not null nor empty.
     * @postcondition wifiFile contains at least the data from the current listToSave, this listToSave hasn't
     * been modified.
     */
    public static Boolean saveWifiNetworks (ArrayList<ScanResult> listToSave, Context context){
        assert !wifiFile.equals(null);
        assert !wifiFile.equals("");

        String time = getDateNTime();
        FileOutputStream fos;
        try {

            fos = context.openFileOutput(wifiFile, Context.MODE_APPEND);
            PrintWriter writer = new PrintWriter(fos);
            for (int i = 0; i< listToSave.size();i++){
                writer.println();
                writer.println(time);
                if (listToSave.get(i).capabilities.contains("IBSS"))
                    writer.println("(*) SSID : "+listToSave.get(i).SSID);
                else
                    writer.println("SSID : "+listToSave.get(i).SSID);
                writer.println("BSSID : "+listToSave.get(i).BSSID);
                writer.println("Frequency : "+listToSave.get(i).frequency);
                writer.println("Level : "+listToSave.get(i).level);
                writer.println("Capabilities : "+listToSave.get(i).capabilities);
            }
            writer.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
