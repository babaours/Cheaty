package hugues.marchal.cheaty;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;

/**
 * This class will make the user choose a network to scan and start the relative activity, knowing that if Bluetooth feature is not supported by the phone,
 * the checkbox relative to Bluetooth will be set GONE and unclickable. The possible networks are (by order of appearance) : WiFi Networks, Bluetooth and
 * Mobile Networks. This activity is also the starter activity.
 */
public class Selecter extends AppCompatActivity implements View.OnClickListener{

    //The Booleans to know if the checkbox is selected
    private Boolean allSelected;
    private Boolean wifiChecked;
    private Boolean bluetoothChecked;
    private Boolean mobileChecked;
    private Boolean isBluetoothSupported = true;

    //The explanatory text
    private final String explanationText = "Please select an option given below.\n \nThe 'WiFi Networks' options will allow you to discover" +
            " the WiFi Networks around you. The ad-hoc networks will also be discovered if the correct 'wpa_supplicant' file is flashed into this rooted device." +
            " The file can be found at : \n http://forum.xda-developers.com/showthread.php?t=754961 \n" +
            "A flashing tutorial can be found at \n http://www.addictivetips.com/mobile/how-to-install-a-rom-or-app-from-zip-file-to-android-device-from-recovery/ \n" +
            "The 'Bluetooth Network' option will allow you to discover the devices using Bluetooth.\n" +
            "The 'Mobile Networks' option will give you all available information about the mobile networks.";

    //The variables of the activity that will be linked to Layout
    CheckBox selectAll;
    RadioButton selectWifiRB;
    RadioButton selectBluetoothRB;
    RadioButton selectMobileRB;
    TextView explanationTextTV;
    Button selectBTN;


    /**
     * This method will occur when the activity starts, which means at the beginning of the Application. This method will link important layout variables like
     * checkBoxes, radioBoxes, explanation TextView and Select button. A OnclickListener is also set on Select Button. If Bluetooth feature is not supported, the
     * Bluetooth radioBox's visibility is set as GONE and a Toast message is displayed.
     * @param savedInstanceState The last known state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecter_main);

        /*
            Linking Layout (activity_selecter_main) variables to activity variables and setting onClickListener for "Select" Button
         */
        selectAll = (CheckBox)findViewById(R.id.selectAllCB);
        selectWifiRB = (RadioButton)findViewById(R.id.selectWifiRB);
        selectBluetoothRB = (RadioButton)findViewById(R.id.selectBluetoothRB);
        selectMobileRB = (RadioButton)findViewById(R.id.selectMobileRB);
        explanationTextTV = (TextView)findViewById(R.id.explanationTextTV);
        selectBTN = (Button)findViewById(R.id.selectBTN);
        selectBTN.setOnClickListener(this);

        /*
            Setting Explanation Text within the explanationTextTV
         */
        explanationTextTV.setText(explanationText);

        /*
        Verifying if Bluetooth technology is supported by the phone. If not, Bluetooth RadioBox is marked as "gone".
        */
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter==null){
            selectBluetoothRB.setVisibility(View.GONE);
            isBluetoothSupported = false;
            Toast.makeText(Selecter.this, "Bluetooth feature is not supported by the phone", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This method will be launched when the user clicks on
     * @param v The view that has been clicked on.
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * This method will set all boxes checked if the selectAll CheckBox is not already selected. Otherwise, set all boxes unchecked.
     */
    @OnClick(R.id.selectAllCB)
    public void selectAll(){
        if (!allSelected){
            selectAll.setChecked(true);
            allSelected = true;

            selectWifiRB.setChecked(true);
            wifiChecked = true;

            selectBluetoothRB.setChecked(true);
            bluetoothChecked = true;

            selectMobileRB.setChecked(true);
            mobileChecked = true;
        }
        else{
            selectAll.setChecked(false);
            allSelected = false;

            selectWifiRB.setChecked(false);
            wifiChecked = false;

            selectBluetoothRB.setChecked(false);
            bluetoothChecked = false;

            selectMobileRB.setChecked(false);
            mobileChecked = false;
        }
    }

    /**
     * This method will set selectWifiRB to checked if it was not before. Otherwise, the method will uncheck the box.
     */
    @OnClick(R.id.selectWifiRB)
    public void selectWifi(){
    }
}
