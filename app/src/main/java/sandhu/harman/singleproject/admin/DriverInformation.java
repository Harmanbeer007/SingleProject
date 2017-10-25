package sandhu.harman.singleproject.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sandhu.harman.singleproject.R;

public class DriverInformation extends AppCompatActivity {

    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mCollageInstance;
    String myBus;
    private EditText DriverNumber;
    private EditText AssignedBus;
    private EditText AssignedRoute;
    private EditText DriverName;
    private TextView CollegeName;
    private EditText DriverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_information);


        SharedPreferences settings = getSharedPreferences(getString(R.string.adminPref), MODE_PRIVATE);
        String college = settings.getString(getString(R.string.college_id), " ");

        Intent i = getIntent();
        BusInfo busInfo = i.getParcelableExtra("DATA");

        DriverId = (EditText) findViewById(R.id.txtProfileDriverId);
//        CollegeName = (TextView) findViewById(R.id.txtProfileDriverCollege);
        DriverNumber = (EditText) findViewById(R.id.txtProfileDriverNumber);
        AssignedBus = (EditText) findViewById(R.id.txtProfileBusAssigned);
        AssignedRoute = (EditText) findViewById(R.id.txtProfileBusRoute);
        DriverName = (EditText) findViewById(R.id.txtProfileDriverName);
        final Button updateBusData = (Button) findViewById(R.id.btnProfileSave);

        DriverName.setText(busInfo.getDrivername());
//        CollegeName.setText("Collage Name: " + college);
        DriverNumber.setText(busInfo.getDriverNumber());
        AssignedBus.setText(busInfo.getBusNumber());
        DriverId.setText(busInfo.getDriverId());
        AssignedRoute.setText(busInfo.getBusRoute());

        myBus = AssignedBus.getText().toString().trim();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mCollageInstance = mFirebaseInstance.getReference("ADMIN/" + college + "/BusAssigned");


        updateBusData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DriverIdProfile = DriverId.getText().toString().trim();
                String DriverNameProfile = DriverName.getText().toString().trim();
                String DriverNumberProfile = DriverNumber.getText().toString().trim();
                String AssignedBusProfile = AssignedBus.getText().toString().trim();
                String AssignedRouteProfile = AssignedRoute.getText().toString().trim();

                updateUserBus(DriverIdProfile, DriverNameProfile, DriverNumberProfile, AssignedBusProfile, AssignedRouteProfile);

            }
        });


    }

    private void updateUserBus(String driverId, String name, String driverNumber, String busNumber, String busRoute) {
        // updating the user via child nodes


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(busNumber) && !TextUtils.isEmpty(busRoute) && !TextUtils.isEmpty(driverId) && !TextUtils.isEmpty(driverNumber))
            mCollageInstance.child(myBus).child("drivername").setValue(name);
        mCollageInstance.child(myBus).child("busNumber").setValue(busNumber);
        mCollageInstance.child(myBus).child("busRoute").setValue(busRoute);
        mCollageInstance.child(myBus).child("driverId").setValue(driverId);
        mCollageInstance.child(myBus).child("driverNumber").setValue(driverNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DriverInformation.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
