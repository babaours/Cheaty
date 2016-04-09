package hugues.marchal.cheaty.Activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;
import hugues.marchal.cheaty.R;

/**
 * This class will make the user choose a network to scan and start the relative activity, knowing that if Bluetooth feature is not supported by the phone,
 * the checkbox relative to Bluetooth will be set GONE and unclickable. The possible networks are (by order of appearance) : WiFi Networks, Bluetooth and
 * Mobile Networks. This activity is also the starter activity.
 */
public class SelecterActivity extends AppCompatActivity implements View.OnClickListener{

    //The Booleans to know if the checkbox is selected
    private Boolean isBluetoothSupported = true;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    //The explanatory text
    private final String explanationText = "Please select an option given below.\n \nThe 'WiFi Networks' options will allow you to discover" +
            " the WiFi Networks around you. The ad-hoc networks will also be discovered if the correct 'wpa_supplicant' file is flashed into this rooted device." +
            " The file can be found at : \n http://forum.xda-developers.com/showthread.php?t=754961 \n" +
            "A flashing tutorial can be found at \n http://www.addictivetips.com/mobile/how-to-install-a-rom-or-app-from-zip-file-to-android-device-from-recovery/ \n" +
            "The 'Bluetooth Network' option will allow you to discover the devices using Bluetooth.\n" +
            "The 'Mobile Networks' option will give you all available information about the mobile networks.";

    //The variables of the activity that will be linked to Layout
    CheckBox selectAll;
    CheckBox wifiCB;
    CheckBox bluetoothCB;
    CheckBox mobileCB;
    TextView explanationTextTV;
    Button selectBT;


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
        wifiCB = (CheckBox)findViewById(R.id.selectWifiCB);
        bluetoothCB = (CheckBox)findViewById(R.id.selectBluetoothCB);
        mobileCB = (CheckBox)findViewById(R.id.selectMobileCB);
        explanationTextTV = (TextView)findViewById(R.id.explanationTextTV);
        selectBT = (Button)findViewById(R.id.selectBTN);
        selectBT.setOnClickListener(this);
        selectBT.setVisibility(View.GONE);


        /*
            Setting Explanation Text within the explanationTextTV
         */
        explanationTextTV.setText(explanationText);

        /*
        Verifying if Bluetooth technology is supported by the phone. If not, Bluetooth CheckBox is marked as "gone".
        */
        if (bluetoothAdapter==null){
            bluetoothCB.setVisibility(View.GONE);
            isBluetoothSupported = false;
            Toast.makeText(SelecterActivity.this, "Bluetooth technology is not supported by the phone", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * This method will allow to select all options if the CheckBox "Select All" is selected/clicked (works at OnClick)
     * @precondition none
     * @postcondition if cbSelectAll.isChecked == true then all <option>.setChecked at true, false otherwise.
     *
     */
    @OnClick(R.id.selectAllCB)
    public void selectAllOptions(View view){
        if (selectAll.isChecked()) {
            bluetoothCB.setChecked(true);
            wifiCB.setChecked(true);
            mobileCB.setChecked(true);
            selectBT.setVisibility(View.VISIBLE);
        }
        else{
            mobileCB.setChecked(false);
            wifiCB.setChecked(false);
            bluetoothCB.setChecked(false);
            selectBT.setVisibility(View.GONE);
        }
    }

    /**
     * This method will allow to change cbSelectAll.setChecked (the Select All CheckBox) if an option is clicked and set unselected.
     * @precondition none
     * @postcondition if cbSelectAll.isChecked == true then cbSelectAll.setChecked = false.
     */
    protected void unSelectAllBox(){
        if (selectAll.isChecked()) {
            selectAll.setChecked(false);
            selectBT.setVisibility(View.GONE);
        }
    }

    /**
     * This method will allow to change cbSelectAll.setChecked (the Select All CheckBox) if the 3 options are clicked and set
     * selected without clicking (selecting) the "Select All" CheckBox
     * @precondition none
     * @postcondition cbSelectAll is checked if every option is selected
     */
    protected void SelectAllBox(){
        if(isBluetoothSupported){
            if (bluetoothCB.isChecked() && mobileCB.isChecked() && wifiCB.isChecked()){
                selectAll.setChecked(true);
                selectBT.setVisibility(View.VISIBLE);
            }
        }else{
            if (mobileCB.isChecked() && wifiCB.isChecked()){
                selectAll.setChecked(true);
                selectBT.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * This method will give the reaction when the user selects/clicks (works at OnClick) the CheckBox option "Mobile Networks".
     * It will call methods to check if the "Select All" CheckBox needs or not to be checked/unchecked.
     * @see #unSelectAllBox()
     * @see #SelectAllBox()
     * @precondition none
     * @postcondition select or unselect the checkBox, which impacts the display of the scanButton and cbSelectAll
     */
    @OnClick(R.id.selectMobileCB)
    public void mobileCBCheck(View view){
        selectBT.setVisibility(View.VISIBLE);
        unSelectAllBox();
        SelectAllBox();
        settingButtonGone();
    }


    /**
     * This method will give the reaction when the user selects/clicks (works at OnClick) the CheckBox option "WiFi Networks".
     * It will call methods to check if the "Select All" CheckBox needs or not to be checked/unchecked.
     * @see #unSelectAllBox()
     * @see #SelectAllBox()
     * @precondition none
     * @postcondition select or unselect the checkBox, which impacts the display of the scanButton and cbSelectAll
     */
    @OnClick(R.id.selectWifiCB)
    public void wifiCBCheck(View view){
        selectBT.setVisibility(View.VISIBLE);
        unSelectAllBox();
        SelectAllBox();
        settingButtonGone();
    }

    /**
     * This method will give the reaction when the user selects/clicks (works at OnClick) the CheckBox option "Bluetooth".
     * It will call methods to check if the "Select All" CheckBox needs or not to be checked/unchecked.
     * @see #unSelectAllBox()
     * @see #SelectAllBox()
     * @precondition none
     * @postcondition select or unselect the checkBox, which impacts the display of the scanButton and cbSelectAll
     */
    @OnClick(R.id.selectBluetoothCB)
    public void bluetoothCBCheck(View view){
        selectBT.setVisibility(View.VISIBLE);
        unSelectAllBox();
        SelectAllBox();
        settingButtonGone();
    }

    /**
     * This method will set the scanButton's visibility to GONE if 2 options are selected
     * @precondition none
     * @postcondition scanButton's Visibility is set as Gone if 2 checkbox are checked.
     *
     */
    private void settingButtonGone (){
        if(isBluetoothSupported) {
            if (bluetoothCB.isChecked() && wifiCB.isChecked() && !mobileCB.isChecked())
                selectBT.setVisibility(View.GONE);
            if (bluetoothCB.isChecked() && !wifiCB.isChecked() && mobileCB.isChecked())
                selectBT.setVisibility(View.GONE);
            if (!bluetoothCB.isChecked() && wifiCB.isChecked() && mobileCB.isChecked())
                selectBT.setVisibility(View.GONE);
            if(!bluetoothCB.isChecked() && !wifiCB.isChecked() && !mobileCB.isChecked())
                selectBT.setVisibility(View.GONE);
        }
    }


    /**
     * This method's goal is to determine which Activity needs to be launched.
     * Each option has its own Activity to do the specific action.
     * This method will work when the user clicks on the TextField (as Button) "Scan" (scanButton)
     * @precondition none
     * @postcondition   displays a Toast if no option has been selected,
     *                  if "Select All" CheckBox is selected, launches allScanActivity (@see #selectAllOptions())
     *                  if "Bluetooth" CheckBox ONLY is selected, launches bluetoothScanActivity
     *                  if "WiFi Networks" ONLY is selected, launches wiFiScanActivity
     *                  if "Mobile Networks" ONLY is selected, launches mobileScanActivity
     *
     */
    @Override
    public void onClick(View v) {
        /*
            Verifies if an option (of the 3) at least has been checked and launches the activity according to it
         */
        if (selectAll.isChecked()&& (bluetoothCB.getVisibility() != View.GONE)) {
            //all scan
        }
        else {
            if (bluetoothCB.isChecked()){
                startActivity(new Intent(SelecterActivity.this, BluetoothScanActivity.class));
            }
            if (mobileCB.isChecked()){
                startActivity(new Intent(SelecterActivity.this, MobileScanActivity.class));

            }
            if (wifiCB.isChecked()){
                startActivity(new Intent(SelecterActivity.this, WifiScanActivity.class));

            }
        }
    }
}
