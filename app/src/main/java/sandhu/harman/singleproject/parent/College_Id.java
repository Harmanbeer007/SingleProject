package sandhu.harman.singleproject.parent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sandhu.harman.singleproject.R;


public class College_Id extends AppCompatActivity {

    private SharedPreferences mParentPrefs;
    public List<BusInfoparent> busData;
    private Button mStartButton;
    private EditText mCollegeIdEditText;
    List<String> collegeBusList;


    private static final String TAG = College_Id.class.getSimpleName();
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mCollageInstance;
    private String college;
    private RecyclerView recycle;
    private SharedPreferences.Editor parentPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_id);


        mStartButton = (Button) findViewById(R.id.button_start_track);
        recycle = (RecyclerView) findViewById(R.id.MyRecyler);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkInputFields();
            }
        });

        mCollegeIdEditText = (EditText) findViewById(R.id.college_Id);


        mParentPrefs = getSharedPreferences(getString(R.string.parentPref), MODE_PRIVATE);
        String collegeID = mParentPrefs.getString(getString(R.string.college_Id), "");


        mCollegeIdEditText.setText(collegeID);


        if (collegeID.length() > 0) {
            authenticate(collegeID);
        } else {
            mCollegeIdEditText.setText(getString(R.string.build_transport_id));

        }

    }

    private void authenticate(final String college) {
        collegeBusList = new ArrayList<String>();

        startActivity(new Intent(College_Id.this, Navdraw.class));
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mCollageInstance = mFirebaseInstance.getReference("ADMIN/");

        mCollageInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                busData = new ArrayList<>();
                // Result will be holded Here
                if (dataSnapshot != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        finish();
    }

    private void checkInputFields() {
        if (mCollegeIdEditText.length() == 0) {
            Toast.makeText(College_Id.this, R.string.missing_inputs, Toast.LENGTH_SHORT).show();
        } else {
            parentPrefEditor = mParentPrefs.edit();
            parentPrefEditor.putString(getString(R.string.college_Id), mCollegeIdEditText.getText().toString().trim());
            parentPrefEditor.apply();
            authenticate(mCollegeIdEditText.getText().toString().trim());

        }
    }


}