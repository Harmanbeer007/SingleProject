package sandhu.harman.singleproject.parent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sandhu.harman.singleproject.R;

public class Website extends AppCompatActivity {
    List<String> collegeBusList;
    public List<BusInfoparent> busData;
    Context context = Website.this;

    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mCollageInstance;
    private SharedPreferences mPrefs;
    public RecyclerView recycle;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);
        collegeBusList = new ArrayList<String>();

        pd = new ProgressDialog(context);
        pd.setTitle("Loading Data");
        pd.setMessage("Please Wait few seconds,depending on network speed");
        pd.show();
        mPrefs = getSharedPreferences(getString(R.string.parentPref), MODE_PRIVATE);
        String collegeID = mPrefs.getString(getString(R.string.college_Id), "");
        recycle = (RecyclerView) findViewById(R.id.MyRecyler);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mCollageInstance = mFirebaseInstance.getReference("ADMIN/" + collegeID + "/BusAssigned");


        mCollageInstance.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    busData = new ArrayList<>();
                    // Result will be holded Here
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        BusInfoparent value = dataSnapshot1.getValue(BusInfoparent.class);
                        BusInfoparent bus = new BusInfoparent();

                        assert value != null;
                        String name = value.getDrivername();
                        String drivernum = value.getDriverNumber();
                        String busnum = value.getBusNumber();
                        String busRout = value.getBusRoute();
                        String driverId = value.getDriverId();
                        bus.setDrivername(name);
                        bus.setDriverNumber(drivernum);
                        bus.setBusNumber(busnum);
                        bus.setBusRoute(busRout);
                        bus.setDriverId(driverId);
                        busData.add(bus);
                        UpdateData();
                    }

                } else {
                    new AlertDialog.Builder(context).setTitle("No Bus Registered")
                            .setMessage("Your College/School Have Not Registered  Any Bus to  Database yet Please Contact Your College/School .")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                    pd.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                ToastClass.ToastMessage(context, databaseError);
            }
        });

    }

    public void UpdateData() {
        BusTrackAdapter recyclerAdapter = new BusTrackAdapter(busData, context, "parent");
        RecyclerView.LayoutManager recyce = new LinearLayoutManager(context);

        recycle.setLayoutManager(recyce);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(recyclerAdapter);
        pd.dismiss();
    }
}
