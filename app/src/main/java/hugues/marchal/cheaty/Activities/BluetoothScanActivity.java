package hugues.marchal.cheaty.Activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import hugues.marchal.cheaty.R;

public class BluetoothScanActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    private ListView listView;
    private Button scanBtn;
    private Button saveBtn;
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

    @Override
    public void onClick(View v) {

    }


}
