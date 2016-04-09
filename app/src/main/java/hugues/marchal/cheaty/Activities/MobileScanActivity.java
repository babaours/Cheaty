package hugues.marchal.cheaty.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hugues.marchal.cheaty.R;

public class MobileScanActivity extends AppCompatActivity implements View.OnClickListener{



    private Button scanButton;
    private TextView typeTV;
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList <String> netDetails = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_scan);
        scanButton = (Button)findViewById(R.id.mobileScanBtn);
        scanButton.setOnClickListener(this);
        typeTV = (TextView)findViewById(R.id.mobileTypeTV);
        listView = (ListView)findViewById(R.id.mobileListView);
        adapter = new ArrayAdapter<>(this, R.layout.mobile_list_item, R.id.mobileListTV, netDetails);
        listView.setAdapter(adapter);


        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    @Override
    public void onClick(View v) {


    }
}