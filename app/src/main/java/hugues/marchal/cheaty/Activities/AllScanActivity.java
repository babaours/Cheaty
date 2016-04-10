package hugues.marchal.cheaty.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import hugues.marchal.cheaty.Adapters.AllScanExpandableListViewAdapter;
import hugues.marchal.cheaty.Classes.BluetoothDev;
import hugues.marchal.cheaty.Classes.Mobile;
import hugues.marchal.cheaty.Classes.SaveItem;
import hugues.marchal.cheaty.Classes.WifiNetwork;
import hugues.marchal.cheaty.R;

public class AllScanActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<String> group = new ArrayList<>();
    private Map<String, ArrayList<String>> allElements = new LinkedHashMap<>();
    private ArrayList<String> bluetoothSaveList = new ArrayList<>();
    private ArrayList<String> wifiSaveList = new ArrayList<>();
    private ArrayList<String> mobileSaveList = new ArrayList<>();

    private final String mobileTitle = "Mobile Networks";
    private final String wifiTitle = "Wifi Networks";
    private final String bluetoothTitle = "Bluetooth Network";

    private Button saveButton;
    private Button scanButton;
    private ExpandableListView expandableListView;
    private AllScanExpandableListViewAdapter adapter;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    private WifiManager wifiManager;

    private boolean wasBluetoothEnabled = true;
    private boolean wasWifiEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_scan);
        setTitle("All Networks Scanner");

        saveButton = (Button)findViewById(R.id.allScanSaveBTN);
        expandableListView = (ExpandableListView)findViewById(R.id.allScanExpandableListView);
        scanButton = (Button)findViewById(R.id.allScanScanBTN);
        scanButton.setOnClickListener(this);
        setGroup();
        adapter = new AllScanExpandableListViewAdapter(this, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), allElements, group);
        expandableListView.setGroupIndicator(null);
        expandableListView.setClickable(true);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<group.size();i++){
            switch (group.get(i)){
                case bluetoothTitle :
                    allElements.put(group.get(i),bluetoothSaveList);
                    break;
                case wifiTitle :
                    allElements.put(group.get(i),wifiSaveList);
                    break;
                case mobileTitle :
                    allElements.put(group.get(i),mobileSaveList);
            }
        }
        adapter.notifyDataSetChanged();

        /*
            Bluetooth operations
         */
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
        }

        /*
            WiFi operations
         */
        wifiManager=(WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wasWifiEnabled = false;
            wifiManager.setWifiEnabled(true);
            Toast.makeText(AllScanActivity.this, "Enabling WiFi...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method will put the correct defined strings within the group ArrayList
     */
    private void setGroup(){
        group.add(mobileTitle);
        group.add(wifiTitle);
        group.add(bluetoothTitle);
    }

    /**
     * This method will create a dialog box asking to activate Bluetooth. If the user refuses, the activity will end.
     *
     * @param requestCode gives the code of the request (i.e. bluetooth activation)
     * @param resultCode  gives the code of the result of the request given by the user.
     * @param data        gives the necessary data for the request
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH)
            return;
        if (resultCode != RESULT_OK) {
            //User did not activate Bluetooth
            Toast.makeText(AllScanActivity.this, "Choose Activate to run the scan", Toast.LENGTH_LONG).show();
            this.finish();
        }
        wasBluetoothEnabled = false;
    }

    @Override
    public void onClick(View v) {
        //Setting the receiver
        registerReceiver(wiFiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        bluetoothAdapter.startDiscovery();
        mobileInformation();
        Toast.makeText(AllScanActivity.this, "Scanning...." , Toast.LENGTH_LONG).show();
    }

    /*
        ============================================================================================
                                Bluetooth Receiver
     */
    /**
     * The BroadcastReceiver implemented to get the results of the scan. It will add every result to the childListFound
     * if the device is not already in it (check is made with the Bluetooth MAC, which is unique). The BroadcastReceiver will
     * obviously work onReceive.
     */
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothDev device = new BluetoothDev(btDevice);
                boolean add = true;
                boolean found = false;
                int index = 0;
                while(!found && index<group.size()){
                    if (!group.get(index).equals(bluetoothTitle))
                        index++;
                    else found =true;
                }
                for (int i = 0;i<allElements.get(group.get(index)).size(); i++){
                    if(allElements.get(group.get(index)).get(i).contains(btDevice.getAddress()))
                        add = false;
                }
                if(add){
                    String dev = "Name : "+device.getDeviceName()+" \n" +
                            "MAC : "+device.getDeviceAddress()+"\n" +
                            "Discovery Time : "+device.getDiscoveryTime();
                    bluetoothSaveList.add(dev);
                    //allElements.get(group.get(index)).clear();
                    allElements.put(group.get(index), bluetoothSaveList);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(AllScanActivity.this, "Loading, please wait", Toast.LENGTH_LONG).show();
                }

            }
        }
    };

    /*
        ============================================================================================
                                            Wifi Receiver
     */
    /**
     * The BroadcastReceiver implemented to get the ScanResults. It will add every result to the childListWiFiFound
     * and delete old result of a the same network found when refresh (update of the list). The BroadcastReceiver will
     * obviously work onReceive.
     */
    private final BroadcastReceiver wiFiReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                List<ScanResult> results = wifiManager.getScanResults();
                boolean add = true;
                try
                {
                    for(int i = 0; i<results.size(); i++){
                        WifiNetwork network = new WifiNetwork(results.get(i));
                        boolean found = false;
                        int index = 0;
                        while(!found && index<group.size()){
                            if (!group.get(index).equals(wifiTitle))
                                index++;
                            else found =true;
                        }
                        for(int j = 0; j<allElements.get(group.get(index)).size();j++){
                            if(allElements.get(group.get(index)).get(j).contains(network.getBssid())){
                                add = false;
                            }
                        }
                        if(add) {
                            String tempGroup, tempChild;
                            if (network.getCapabilities().contains("IBSS")) {
                                tempGroup = "(*) SSID : " + network.getSsid() + "\n" +
                                        "BSSID : " + network.getBssid();
                            } else {
                                tempGroup = "SSID : " + network.getSsid() + "\n" +
                                        "BSSID : " + network.getBssid();
                            }
                            tempChild = "Capabilities : " + network.getCapabilities() + "\n" +
                                    "Level : " + network.getLevel() + "\n" +
                                    "Frequency : " + network.getFrequency() + "\n" +
                                    "Discovery Time : " + network.getDiscoveryTime();
                            wifiSaveList.add(tempGroup + "\n" + tempChild);
                            //allElements.get(group.get(index)).clear();
                            allElements.put(group.get(index), wifiSaveList);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(AllScanActivity.this, "Loading, please wait", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }};

    /*
        ============================================================================================
                                            Mobile Data Setter
     */
    private void mobileInformation(){
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String str = "Network Type : "+Mobile.getNetworkType(telephonyManager);
        mobileSaveList.add(str);
        boolean found = false;
        int index = 0;
        while(!found && index<group.size()){
            if (!group.get(index).equals(wifiTitle))
                index++;
            else found =true;
        }
        allElements.put(group.get(index),mobileSaveList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(!wasWifiEnabled && !wasBluetoothEnabled){
            wifiManager.setWifiEnabled(false);
            bluetoothAdapter.disable();
            Toast.makeText(AllScanActivity.this,"Wifi and Bluetooth have been disabled", Toast.LENGTH_LONG).show();
        }else{
            if (!wasWifiEnabled){
                wifiManager.setWifiEnabled(false);
                Toast.makeText(AllScanActivity.this,"Wifi has been disabled", Toast.LENGTH_SHORT).show();
            }else{
                if(!wasBluetoothEnabled){
                    bluetoothAdapter.disable();
                    Toast.makeText(AllScanActivity.this,"Bluetooth has been disabled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @OnClick(R.id.allScanSaveBTN)
    public void saveAllItems(View view){
        if(wifiSaveList.isEmpty() && bluetoothSaveList.isEmpty() && mobileSaveList.isEmpty())
            Toast.makeText(AllScanActivity.this,"Nothing to save !", Toast.LENGTH_SHORT).show();
        else{
            boolean done = false;
            boolean done2 = false;
            boolean done3 = false;
            if(!wifiSaveList.isEmpty()){
                done = SaveItem.saveWifiNetworks(wifiSaveList,this);
                wifiSaveList.clear();
                boolean found = false;
                int index = 0;
                while(!found && index<group.size()){
                    if (!group.get(index).equals(wifiTitle))
                        index++;
                    else found =true;
                }
                allElements.put(group.get(index), wifiSaveList);
                adapter.notifyDataSetChanged();
            }
            if(!bluetoothSaveList.isEmpty()){
                try {
                    done2 = SaveItem.saveBluetoothDevice(bluetoothSaveList, this);
                    boolean found = false;
                    int index = 0;
                    while(!found && index<group.size()){
                        if (!group.get(index).equals(bluetoothTitle))
                            index++;
                        else found =true;
                    }
                    bluetoothSaveList.clear();
                    allElements.put(group.get(index), bluetoothSaveList);
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(!mobileSaveList.isEmpty()){
                done3 = SaveItem.saveMobileNetworks(mobileSaveList, this);
                boolean found = false;
                int index = 0;
                while(!found && index<group.size()){
                    if (!group.get(index).equals(mobileTitle))
                        index++;
                    else found =true;
                }
                mobileSaveList.clear();
                allElements.put(group.get(index),mobileSaveList);
                adapter.notifyDataSetChanged();
            }
            if(done || done2 || done3){
                Toast.makeText(AllScanActivity.this,"Data Saved", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
