package sandhu.harman.singleproject.parent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import sandhu.harman.singleproject.MyHeadActivity;
import sandhu.harman.singleproject.R;

public class Navdraw extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private ImageView btnTrackBus;
    private LinearLayout payfee, studyOnline;
    private ImageView viewWebsite;

    private SharedPreferences.Editor mEditor;
    private LinearLayout addMoney, sendMoney, statementOfMoney;
    private FirebaseAuth mAuth;
    private SharedPreferences mparentPrefs;
    private SharedPreferences userCheckLoginPreferences;
    private SharedPreferences.Editor mUserEditor;
    private Navdraw context;
    private LinearLayout schoolShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdraw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewWebsite = (ImageView) findViewById(R.id.btnWeb);
        addMoney = (LinearLayout) findViewById(R.id.btnAddMoney);
        sendMoney = (LinearLayout) findViewById(R.id.btnSendMoney);
        statementOfMoney = (LinearLayout) findViewById(R.id.btnStatement);
        schoolShop = (LinearLayout) findViewById(R.id.schoolShop);

        btnTrackBus = (ImageView) findViewById(R.id.btnTrackBus);
        payfee = (LinearLayout) findViewById(R.id.payfeeLay);
        studyOnline = (LinearLayout) findViewById(R.id.studyOnline);

        viewWebsite.setOnClickListener(this);
        addMoney.setOnClickListener(this);
        sendMoney.setOnClickListener(this);
        statementOfMoney.setOnClickListener(this);
        btnTrackBus.setOnClickListener(this);
        payfee.setOnClickListener(this);
        schoolShop.setOnClickListener(this);

        studyOnline.setOnClickListener(this);

        mparentPrefs = getSharedPreferences(getString(R.string.parentPref), MODE_PRIVATE);
        userCheckLoginPreferences = getSharedPreferences(getString(R.string.userLoggedPrefs), MODE_PRIVATE);
        mUserEditor = userCheckLoginPreferences.edit();

        mEditor = mparentPrefs.edit();
        setSupportActionBar(toolbar);

        context = Navdraw.this;
        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.navdraw, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        CharSequence log = item.getTitle();

        if (id == R.id.nav_trackbus) {

            startActivity(new Intent(Navdraw.this, DriverList.class));
        }
        if (id == R.id.nav_change_college) {

            mEditor.putString(getString(R.string.college_Id), "");
            mEditor.apply();
            startActivity(new Intent(Navdraw.this, College_Id.class));
            finish();
        }
        if (id == R.id.nav_logout) {
            if (mAuth.getCurrentUser() != null) {
                mAuth.signOut();
            }
            mEditor.putString(getString(R.string.college_Id), "");
            mEditor.apply();
            mUserEditor.putString(getString(R.string.userLogged), "");
            mUserEditor.putString(getString(R.string.userType), "");
            mUserEditor.apply();
            startActivity(new Intent(Navdraw.this, MyHeadActivity.class));
            finish();
        }
        if (id == R.id.nav_pay_fee) {
            startActivity(new Intent(Navdraw.this, PayFees.class));

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddMoney:
                Toast.makeText(context, "Add Money Feature Will be Available Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnSendMoney:
                Toast.makeText(context, "Send Money Feature Will be Available Soon", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnStatement:
                Toast.makeText(context, "Statement Feature Will be Available Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.payfeeLay:
                startActivity(new Intent(Navdraw.this, PayFees.class));
                break;
            case R.id.studyOnline:
                Toast.makeText(context, "This Feature Will be Available Soon", Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(Navdraw.this, StudyOnline.class));
                break;
            case R.id.btnTrackBus:
                startActivity(new Intent(Navdraw.this, DriverList.class));
                break;

            case R.id.btnWeb:
                startActivity(new Intent(Navdraw.this, viewCollegeWebsite.class));
                break;
            case R.id.schoolShop:

                // TODO: 06-11-2017 Shop Online

//                Toast.makeText(context, "This Feature Will be Available Soon", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Navdraw.this, fee_ShopActivity.class));
                break;

        }
    }
}
