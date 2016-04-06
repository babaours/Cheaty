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
    private Boolean isBluetoothSupported = true;
    private Boolean wifiRBSelected;
    private Boolean mobileRBSelected;
    private Boolean bluetoothRBSelected;

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
        selectBTN.setVisibility(View.GONE);

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
        wifiRBSelected = selectWifiRB.isChecked();
        mobileRBSelected = selectMobileRB.isChecked();
        bluetoothRBSelected = selectBluetoothRB.isChecked();
        allSelected = selectAll.isChecked();

        if(allSelected){
            //Start All Scan Activity
        }
        else{
            if(wifiRBSelected){
                //Start WiFi Scan Activity
            }
            if(mobileRBSelected){
                //Start Mobile Scan Activity
            }
            if(bluetoothRBSelected){
                //Start Bluetooth Scan Activity
            }
        }
    }

    /**
     * This method will set all boxes checked if the selectAll CheckBox is not already selected. In this case, the select button will be visible, Otherwise,
     * set all boxes unchecked and set select button gone.
     */
    @OnClick(R.id.selectAllCB)
    public void selectAll(){
        if (!allSelected){
            selectAll.setChecked(true);
            allSelected = true;
            selectWifiRB.setChecked(true);
            selectBluetoothRB.setChecked(true);
            selectMobileRB.setChecked(true);
            selectBTN.setVisibility(View.VISIBLE);
        }
        else{
            selectAll.setChecked(false);
            allSelected = false;
            selectWifiRB.setChecked(false);
            selectBluetoothRB.setChecked(false);
            selectMobileRB.setChecked(false);
            selectBTN.setVisibility(View.GONE);
        }
    }

    /**
     * This method will allow to change selectAll CB (the Select All CheckBox) if an option is clicked and set unselected.
     * @precondition none
     * @postcondition if the selectAll CB is checked, it will be set unchecked and select button visibility will pass to gone
     */
    protected void unSelectAllBox (){
        if (selectAll.isChecked()) {
            selectAll.setChecked(false);
            selectBTN.setVisibility(View.GONE);
        }
    }

    /**
     * This method will allow to change selectAll CB (the Select All CheckBox) if the 3 options are clicked and set
     * selected without clicking (selecting) the "Select All" CheckBox
     * @precondition none
     * @postcondition selectAll CB is checked if every option is selected
     */
    protected void selectAllBox(){
        if (selectBluetoothRB.isChecked() && selectMobileRB.isChecked() && selectWifiRB.isChecked()){
            selectAll.setChecked(true);
            selectBTN.setVisibility(View.VISIBLE);
        }

    }

    protected void setSelectBTNGone(){
        wifiRBSelected = selectWifiRB.isChecked();
        mobileRBSelected = selectMobileRB.isChecked();
        bluetoothRBSelected = selectBluetoothRB.isChecked();
        allSelected = selectAll.isChecked();

        if(!allSelected){
            if(!wifiRBSelected && mobileRBSelected && bluetoothRBSelected)
                selectAll.setVisibility(View.GONE);
            if(wifiRBSelected && !mobileRBSelected && bluetoothRBSelected)
                selectAll.setVisibility(View.GONE);
            if(wifiRBSelected && mobileRBSelected && !bluetoothRBSelected)
                selectAll.setVisibility(View.GONE);
        }
    }

    /**
     * This method will set selectWifiRB to checked if it was not before. Otherwise, the method will uncheck the box.
     */
    @OnClick(R.id.selectWifiRB)
    public void selectWifi(){
        if(selectWifiRB.isChecked()){
            selectWifiRB.setChecked(false);
        }
        else{
            selectWifiRB.setChecked(true);
        }
        unSelectAllBox();
        selectAllBox();
        setSelectBTNGone();
    }

    /**
     * This method will check or uncheck the bluetooth RB and modify the display of the buttons
     */
    @OnClick(R.id.selectBluetoothRB)
    public void selectBluetooth(){
        if(selectBluetoothRB.isChecked()){
            selectBluetoothRB.setChecked(false);
        }
        else selectBluetoothRB.setChecked(true);
        unSelectAllBox();
        selectAllBox();
        setSelectBTNGone();
    }


    /**
     * This method will check or uncheck the mobile RB and modify the display of the buttons
     */
    @OnClick(R.id.selectMobileRB)
    public void selectMobile(){
        if(selectMobileRB.isChecked())
            selectMobileRB.setChecked(false);
        else selectMobileRB.setChecked(true);
        unSelectAllBox();
        selectAllBox();
        setSelectBTNGone();
    }

}
