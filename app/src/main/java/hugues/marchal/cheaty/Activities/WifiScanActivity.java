package hugues.marchal.cheaty.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import hugues.marchal.cheaty.Adapters.WifiExpandableListViewAdapter;
import hugues.marchal.cheaty.Classes.SaveItem;
import hugues.marchal.cheaty.Classes.WifiNetwork;
import hugues.marchal.cheaty.R;

public class WifiScanActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveBTN;
    private Button scanBTN;
    private ExpandableListView expandableListView;
    private ArrayList<String> group = new ArrayList<>();
    private ArrayList<String> child = new ArrayList<>();
    private ArrayList<String> completeList = new ArrayList<>();
    private Map<String, String> allItems = new LinkedHashMap<String,String>();
    private WifiExpandableListViewAdapter adapter;
    private Boolean wasWifiEnabled = true;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);
        setTitle("Wifi Network Scanner");
        expandableListView = (ExpandableListView)findViewById(R.id.wifiExpListView);
        scanBTN = (Button)findViewById(R.id.wifiScanBTN);
        scanBTN.setOnClickListener(this);
        saveBTN = (Button)findViewById(R.id.wifiSaveBTN);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        //Enabling WiFi is it is not already
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            wasWifiEnabled = false;
            Toast.makeText(this, "Enabling WiFi...", Toast.LENGTH_SHORT).show();
        }

        adapter = new WifiExpandableListViewAdapter(this, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), allItems, group);
        //ExpendableListView Settings
        expandableListView.setDividerHeight(2);
        expandableListView.setGroupIndicator(null);
        expandableListView.setClickable(true);
        expandableListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        //Setting the receiver
        registerReceiver(wiFiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        Toast.makeText(this, "Scanning...." , Toast.LENGTH_LONG).show();
        wifiManager.startScan();
    }

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
                        for(int j = 0; j<group.size();j++){
                            if(group.get(j).contains(network.getBssid())){
                                add = false;
                            }
                        }
                        if(add) {
                            String tempGroup, tempChild;
                            if (network.getCapabilities().contains("IBSS")) {
                                tempGroup = "(*) SSID : " + network.getSsid() + "\n" +
                                        "BSSID : " + network.getBssid();
                                group.add(tempGroup);
                            } else {
                                tempGroup = "SSID : " + network.getSsid() + "\n" +
                                        "BSSID : " + network.getBssid();
                                group.add(tempGroup);
                            }
                            tempChild = "Capabilities : " + network.getCapabilities() + "\n" +
                                    "Level : " + network.getLevel() + "\n" +
                                    "Frequency : " + network.getFrequency();
                            child.add(tempChild);
                            allItems.put(tempGroup, tempChild);
                            completeList.add(tempGroup+"\n"+tempChild);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }};


    @OnClick(R.id.wifiSaveBTN)
    public void saveWifiNetwork(View view){
        if(completeList.isEmpty()){
            Toast.makeText(this,"Nothing to save !", Toast.LENGTH_SHORT).show();
        }else{
            Boolean bool = SaveItem.saveWifiNetworks(completeList,this);
            if (bool)
                Toast.makeText(this,"Data has been saved",Toast.LENGTH_SHORT).show();
            completeList.clear();
            allItems.clear();
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * This method will disable Wifi at the end of the activity if it was disabled before starting the activity
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(!wasWifiEnabled)
            wifiManager.setWifiEnabled(false);
        Toast.makeText(this, "Wifi has been disabled", Toast.LENGTH_SHORT).show();
    }
}
