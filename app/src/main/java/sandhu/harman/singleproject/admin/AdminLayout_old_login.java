package sandhu.harman.singleproject.admin;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Map;

import sandhu.harman.singleproject.R;

import static sandhu.harman.singleproject.admin.Loading.dismiss;
import static sandhu.harman.singleproject.admin.Loading.setMessge;


public class AdminLayout_old_login extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;
    private static String[] PERMISSIONS_REQUIRED = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String TAG = "Login";

    private SharedPreferences mPrefs;

    private Button mLoginButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private SwitchCompat mSwitch;
    private Snackbar mSnackbarPermissions;
    private LinkedList<Map<String, Object>> mTransportStatuses = new LinkedList<>();
    private EditText mCollegeIdEditText;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mAccessInstance;
    private String scollegeId;
    private String semail;
    private String spassword;


    /**
     * Configures UI elements, and starts validation if inputs have previously been entered.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_layout);

        mLoginButton = (Button) findViewById(R.id.button_login);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkInputFields();
            }
        });
        mCollegeIdEditText = (EditText) findViewById(R.id.college_id);
        mEmailEditText = (EditText) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);

        mPrefs = getSharedPreferences(getString(R.string.userLoggedPrefs), MODE_PRIVATE);
        scollegeId = mPrefs.getString(getString(R.string.college_id), "");
        semail = mPrefs.getString(getString(R.string.email), "");
        spassword = mPrefs.getString(getString(R.string.password), "");

        mEmailEditText.setText(semail);
        mPasswordEditText.setText(spassword);
        mCollegeIdEditText.setText(scollegeId);


    }


    private void checkInputFields() {
        if (mEmailEditText.length() == 0 || mPasswordEditText.length() == 0) {
            Toast.makeText(AdminLayout_old_login.this, R.string.missing_inputs, Toast.LENGTH_SHORT).show();
        } else {
            // Store values.
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(getString(R.string.college_id), mCollegeIdEditText.getText().toString());
            editor.putString(getString(R.string.email), mEmailEditText.getText().toString());
            editor.putString(getString(R.string.password), mPasswordEditText.getText().toString());
            editor.apply();
            // Validate permissions.
            authenticate(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString(), mCollegeIdEditText.getText().toString().trim());
            Loading.load(AdminLayout_old_login.this, "Validating Login", "Please wait");
//            mSwitch.setEnabled(true);
        }
    }


    private void authenticate(final String email, String password, final String college) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        Log.i(TAG, "authenticate: " + task.isSuccessful());
                        if (task.isSuccessful()) {

                            reauthenticate(email, college);
                            setMessge("Checking Account Permissions");

                        } else {
                            Toast.makeText(AdminLayout_old_login.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);


        return super.onCreateOptionsMenu(menu);
    }


    private void reauthenticate(final String email, final String collegeID) {

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAccessInstance = mFirebaseInstance.getReference
                ("AccessTo/" + mCollegeIdEditText.getText().toString().trim());

        mAccessInstance.addValueEventListener(new ValueEventListener() {
            public AccessInfo value;
            public boolean driverListed = false;
            public String bus;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    value = dataSnapshot.getValue(AccessInfo.class);

                    String em = value.getEmail();
                    String co = value.getCollege();
                    if (em.equals(email) && co.equals(collegeID)) {
                        Toast.makeText(AdminLayout_old_login.this, "Welcome", Toast.LENGTH_LONG).show();
                        dismiss();
                        startActivity(new Intent(AdminLayout_old_login.this, mainscreen.class));

                    } else {
                        Toast.makeText(AdminLayout_old_login.this, "You are Not authorized to access this college/school data,Please check your Credentials", Toast.LENGTH_SHORT).show();

                        dismiss();

                    }
                } else {
                    Toast.makeText(AdminLayout_old_login.this, "Your College Does Not Exist In Our Database ,Please Contact Us If You Want To Be a Member", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminLayout_old_login.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                dismiss();

            }
        });
    }
}
