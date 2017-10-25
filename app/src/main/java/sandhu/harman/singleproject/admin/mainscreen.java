package sandhu.harman.singleproject.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sandhu.harman.singleproject.MyHeadActivity;
import sandhu.harman.singleproject.R;

public class mainscreen extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton addBus, listBus, trackBus;
    MyHeadActivity headActivity;
    SharedPreferences mPrefsUserCheck;
    SharedPreferences mPrefsAdmin;
    SharedPreferences.Editor userPrefEditor;
    SharedPreferences.Editor adminPrefEditor;
    private TextView user;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        findViewById(R.id.btnAddBus).setOnClickListener(this);
        findViewById(R.id.btnBusList).setOnClickListener(this);
        findViewById(R.id.btnTrackBus).setOnClickListener(this);
        findViewById(R.id.btnWeb).setOnClickListener(this);
        headActivity = new MyHeadActivity();

        user = (TextView) findViewById(R.id.txtUser);

//        SharedPreferences
        mPrefsUserCheck = getSharedPreferences(getString(R.string.userLoggedPrefs), MODE_PRIVATE);
        mPrefsAdmin = getSharedPreferences(getString(R.string.adminPref), MODE_PRIVATE);
        userPrefEditor = mPrefsUserCheck.edit();
        adminPrefEditor = mPrefsAdmin.edit();
        mAuth = FirebaseAuth.getInstance();

        String username = mPrefsAdmin.getString(getString(R.string.college_id), " ");
        user.setText("Logout: " + username);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPrefEditor.putString(getString(R.string.userType), "");
                userPrefEditor.putString(getString(R.string.userLogged), "");
                userPrefEditor.apply();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    mAuth.signOut();
                }
                startActivity(new Intent(mainscreen.this, MyHeadActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())

        {
            case R.id.btnAddBus: {
                startActivity(new Intent(mainscreen.this, AddBus.class));
                break;
            }
            case R.id.btnBusList: {
                startActivity(new Intent(mainscreen.this, ListOfBus.class));
                break;
            }
            case R.id.btnTrackBus: {
                startActivity(new Intent(mainscreen.this, AdminBusTrack.class));
                break;
            }
            case R.id.btnWeb: {
                startActivity(new Intent(mainscreen.this, WebsiteAdd.class));

                break;
            }
//            case R.id.btnAPIInterface:
//            {
//                break;
//            }
        }

    }
}
