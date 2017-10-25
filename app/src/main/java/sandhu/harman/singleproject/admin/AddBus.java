package sandhu.harman.singleproject.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sandhu.harman.singleproject.R;

import static sandhu.harman.singleproject.admin.isEmptyUtil.isEmpty;

public class AddBus extends AppCompatActivity {

    private static final int SELECT_PHOTO = 1222;
    private Button btnSave;
    private DatabaseReference mCollageInstance;
    private FirebaseDatabase mFirebaseInstance;

    private EditText DriverName;
    private EditText DriverNumber;
    private EditText DriverId;
    private EditText BusNumber;
    private EditText BusRoute;
    private Button btnGo;
    private ProgressDialog m_Dialog;
    private String college;
    private DatabaseReference mbusListInstance;
    private DatabaseReference mDriverListInstance;
    private Button browse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainaddbus);


        DriverName = (EditText) findViewById(R.id.drivername);
        DriverNumber = (EditText) findViewById(R.id.DriverNumber);
        DriverId = (EditText) findViewById(R.id.DriverId);
        BusNumber = (EditText) findViewById(R.id.bus);
        BusRoute = (EditText) findViewById(R.id.busRoute);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnGo = (Button) findViewById(R.id.btn_go);
//        browse=(Button) findViewById(R.id.browseImage);
//        browse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectImage();
//            }
//        });
        SharedPreferences settings = getSharedPreferences(getString(R.string.adminPref), MODE_PRIVATE);
        college = settings.getString(getString(R.string.college_id), " ");

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mCollageInstance = mFirebaseInstance.getReference("ADMIN/" + college + "/BusAssigned");

        //creates a storage reference

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBus.this, ListOfBus.class));
            }
        });

        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Drvname = DriverName.getText().toString().trim();
                String DrvNumber = DriverNumber.getText().toString().trim();
                String BsNumber = BusNumber.getText().toString().trim();
                String BsRoute = BusRoute.getText().toString().trim();
                String DrvId = DriverId.getText().toString().trim();

                // Check for already existed busId
//                if (TextUtils.isEmptyUtil(busId)) {
                createNewBus(Drvname, DrvNumber, BsNumber, BsRoute, DrvId);
//                }
            }
        });

    }


    private void createNewBus(String drivername, String driverNumber, String busNumber, String busRoute, String driverId) {
        if (busNumber.length() == 0) {
            BusNumber.setError("Required");
        } else if (busRoute.length() == 0) {
            BusRoute.setError("Required");
        } else if (drivername.length() == 0) {
            DriverName.setError("Required");
        } else if (driverNumber.length() < 10 || driverNumber.length() > 10) {
            DriverNumber.setError("Please Input Valid Number");
        } else if (driverId.length() == 0) {
            DriverId.setError("Required");
        } else
//        else if(isEmpty(drivername) && isEmpty(driverNumber) && isEmpty(busNumber) && isEmpty(busRoute) && isEmpty(driverId))
//        {
        {
            BusInfo bus = new BusInfo(drivername, driverNumber, busNumber, busRoute, driverId);
            m_Dialog = new ProgressDialog(AddBus.this);
            m_Dialog.setMessage("Adding Bus");
            m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_Dialog.setCancelable(false);
            m_Dialog.show();

            mbusListInstance = mFirebaseInstance.getReference("ADMIN/" + college + "/busList");
            mDriverListInstance = mFirebaseInstance.getReference("ADMIN/" + college + "/driverList");
            BusInfo driver = new BusInfo(drivername, driverNumber, driverId);
            mDriverListInstance.child(drivername).setValue(driver);


            mbusListInstance.child(busNumber).child("Bus Name").setValue(busNumber);
            mCollageInstance.child(busNumber).setValue(bus).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AddBus.this, "Bus Added", Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
//        addBusChangeListener();
        }
//        else {
//            Toast.makeText(this, "fields cant be empty", Toast.LENGTH_SHORT).show();
//        }
    }


}
