package sandhu.harman.singleproject.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sandhu.harman.singleproject.R;


public class ListOfBus extends AppCompatActivity {

    ListView buslist;
    private TextView textv;
    public List<BusInfo> busData;
    private RecycleAdapter recycleAdapter;
    private Button btn;
    RecyclerView recycle;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_bus);
        context = ListOfBus.this;
        Loading.load(context, "loading", "Please wait...");

        recycle = (RecyclerView) findViewById(R.id.MyRecyler);
        database = FirebaseDatabase.getInstance();
        SharedPreferences settings = getSharedPreferences(getString(R.string.adminPref), MODE_PRIVATE);
        String admin = settings.getString(getString(R.string.email), " ");
        String college = settings.getString(getString(R.string.college_id), " ");


        myRef = database.getReference("ADMIN/" + college + "/BusAssigned");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                busData = new ArrayList<>();
                // Result will be holded Here
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    BusInfo value = dataSnapshot1.getValue(BusInfo.class);

                    BusInfo bus = new BusInfo();
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
                }
                RecycleAdapter recyclerAdapter = new RecycleAdapter(busData, ListOfBus.this);
                RecyclerView.LayoutManager recyce = new LinearLayoutManager(ListOfBus.this);

                recycle.setLayoutManager(recyce);
                recycle.setItemAnimator(new DefaultItemAnimator());
                recycle.setAdapter(recyclerAdapter);
                Loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();

            }
        });


//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//
//            }
//        });
    }
}