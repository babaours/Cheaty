package hugues.marchal.cheaty.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.OnClick;
import hugues.marchal.cheaty.Classes.SaveItem;
import hugues.marchal.cheaty.R;

public class BluetoothScanActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    private ListView listView;
    private Button scanBtn;
    private Button saveBtn;
    /*
        We have here an arrayList of ArrayList, from the outside to the inside, the first one is for devices and the second one is for details, respecting this specific
        order : Name, MAC.
     */
    private ArrayAdapter listAdapter;
    private ArrayList<String> listDevices = new ArrayList<>();
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);
        listView = (ListView) findViewById(R.id.btListView);
        scanBtn = (Button) findViewById(R.id.btScanButton);
        scanBtn.setOnClickListener(this);
        saveBtn = (Button) findViewById(R.id.btSaveButton);

        /*
        Activating Bluetooth if not enabled
        Check #onActivityResult for more
         */
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
        }
        //Setting the ListView adapter
        listAdapter = new ArrayAdapter<>(this, R.layout.bluetooth_list_item, R.id.btRecepter, listDevices);
        listView.setAdapter(listAdapter);
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
            Toast.makeText(BluetoothScanActivity.this, "Choose Activate to run the scan", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    /**
     * This method will launch a scan of the Bluetooth Network when the scanButton is pushed.
     * @precondition scanButton is pushed
     * @postcondition the discovery has started on bluetoothAdapter, childListFound has been cleared
     */
    @Override
    public void onClick(View v) {
        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        Toast.makeText(BluetoothScanActivity.this, "Loading devices, please wait", Toast.LENGTH_LONG).show();
        bluetoothAdapter.startDiscovery();
    }

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
                boolean add = true;
                for (int i = 0;i<listDevices.size(); i++){
                    if(listDevices.get(i).contains(btDevice.getAddress()))
                            add = false;
                }
                if(add){
                    String device = "Name : "+btDevice.getName()+" \n" +
                                    "MAC : "+btDevice.getAddress()+"\n" +
                                    "Discovery Time : "+SaveItem.getDateNTime();
                    listDevices.add(device);
                    listAdapter.notifyDataSetChanged();
                    Toast.makeText(BluetoothScanActivity.this, "Loading devices, please wait", Toast.LENGTH_LONG).show();
                }

            }
        }};

    /**
     * This method will allow the user to save the new found devices into the phone's memory.
     * @param view the view that will trigger the method
     * @see SaveItem#saveBluetoothDevice(ArrayList)
     * @precondition none
     * @postcondition childListSaved contains ONLY the data from the New Found devices list and this list (childListFound) is set empty. All content of childListFound is saved into the phone's memory.
     * if childListFound is empty, throws a Toast message and does nothing. Throws a message if everything went well.
     */
    @OnClick(R.id.btSaveButton)
    public void saveBluetoothDevice (View view){
        if (listDevices.size()<=0)
            Toast.makeText(this, "Nothing to save !", Toast.LENGTH_SHORT).show();
        else {
            try {
                Boolean bool = SaveItem.saveBluetoothDevice(listDevices);
                if (bool)
                    Toast.makeText(this,"Data has been saved",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            listDevices.clear();
            listAdapter.notifyDataSetChanged();
        }

    }

    /**
     * This method will unregister the custom Broadcast Receiver when the activity is killed.
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }
}
