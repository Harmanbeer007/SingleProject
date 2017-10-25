package sandhu.harman.singleproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.HashMap;

public class MyHeadActivity extends AppCompatActivity implements View.OnClickListener, myInterface {

    CardView cardTeacher, cardDriver, cardParents;

    View inputUserContainer;
    private View inputPasswordContainer;
    private TextView login;
    private EditText user;
    private EditText password;
    private SharedPreferences userCheckLoginPreferences;
    SharedPreferences.Editor editor;
    private FragmentTransaction transection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myx);
        cardTeacher = (CardView) findViewById(R.id.cardViewTeacher);
        cardParents = (CardView) findViewById(R.id.cardViewParent);
        cardDriver = (CardView) findViewById(R.id.cardViewDriver);
        userCheckLoginPreferences = getSharedPreferences(getString(R.string.userLoggedPrefs), MODE_PRIVATE);


        onComplete();

        cardTeacher.setOnClickListener(this);
        cardParents.setOnClickListener(this);
        cardDriver.setOnClickListener(this);


    }

    private void rollOutCardExcept(String user) {
        switch (user) {
            case "driver": {
                setElevation(cardDriver, cardParents, cardTeacher);
                setenable("driver");
                YoYo.with(Techniques.RollOut).duration(700).delay(1000).playOn(cardParents);
                YoYo.with(Techniques.RollOut).duration(700).delay(1000).playOn(cardTeacher);
                break;
            }
            case "parents": {
                setenable("parents");
                setElevation(cardParents, cardDriver, cardTeacher);
                YoYo.with(Techniques.RollOut).duration(700).playOn(cardTeacher);
                YoYo.with(Techniques.RollOut).duration(700).playOn(cardDriver);
                break;

            }
            case "teacher": {
                setenable("teacher");
                setElevation(cardTeacher, cardParents, cardDriver);
                YoYo.with(Techniques.RollOut).duration(700).playOn(cardParents);
                YoYo.with(Techniques.RollOut).duration(700).playOn(cardDriver);

                break;
            }
            default:
                YoYo.with(Techniques.RollIn).duration(700).playOn(cardParents);
                YoYo.with(Techniques.RollIn).duration(700).playOn(cardDriver);
                YoYo.with(Techniques.RollIn).duration(700).playOn(cardTeacher);


        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardViewDriver: {
                setElevation(cardDriver, cardParents, cardTeacher);
                setenable("driver");
                break;
            }
            case R.id.cardViewParent: {
                setenable("parents");
                setElevation(cardParents, cardDriver, cardTeacher);
                break;
            }
            case R.id.cardViewTeacher: {
                setenable("teacher");
                setElevation(cardTeacher, cardParents, cardDriver);
                break;
            }
        }
    }

    private void checkInputs() {
        String usern = user.getText().toString().trim();
        String passn = password.getText().toString().trim();
        View frame;


        if (TextUtils.isEmpty(usern) || TextUtils.isEmpty(passn)) {
            user.setError("Required");
            password.setError("Required");
        } else {
            Toast.makeText(this, "Login With" + usern, Toast.LENGTH_SHORT).show();
        }

    }

    private void setElevation(CardView v, CardView second, CardView third) {
        switch (v.getId()) {
            case R.id.cardViewDriver: {
                cardDriver.setCardElevation(5);
                cardDriver.setCardBackgroundColor(getResources().getColor(R.color.green_300));
                setdisable(second, third);
                animateInputContainer(cardDriver);
                break;
            }
            case R.id.cardViewParent: {
                cardParents.setCardElevation(5);
                setdisable(second, third);
                cardParents.setCardBackgroundColor(getResources().getColor(R.color.green_300));
                animateInputContainer(cardParents);
                break;
            }
            case R.id.cardViewTeacher: {
                cardTeacher.setCardElevation(5);
                setdisable(second, third);
                cardTeacher.setCardBackgroundColor(getResources().getColor(R.color.green_300));
                animateInputContainer(cardTeacher);
                break;
            }

        }

    }

    private void setenable(String i) {
        Fragment selectedFragment = TeacherFrag.newInstance();
        switch (i) {

            case "teacher":
                selectedFragment = TeacherFrag.newInstance();
                setElevation(cardTeacher, cardDriver, cardParents);
                break;
            case "parents":
                selectedFragment = PArentFrag.newInstance();
                setElevation(cardParents, cardDriver, cardTeacher);
                break;
            case "driver":
                selectedFragment = DriverFrag.newInstance();
                setElevation(cardDriver, cardTeacher, cardParents);
                break;

        }
        try {
            transection = getSupportFragmentManager().beginTransaction();
            transection.replace(R.id.myFrame, selectedFragment);
            transection.commit();
        } catch (IllegalStateException e) {

        }
    }


    private void setdisable(CardView second, CardView third) {
        second.setCardElevation(0);
        third.setCardElevation(0);
        second.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        third.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public void animateInputContainer(View view) {
        YoYo.with(Techniques.Shake).duration(700).playOn(view);
    }

    @Override
    public void onComplete() {

        String user = userCheckLoginPreferences.getString(getString(R.string.userLogged), "");
        String type = userCheckLoginPreferences.getString(getString(R.string.userType), "");
        if (!user.equals("") || !type.equals("")) {
            rollOutCardExcept(type);
        } else {
            YoYo.with(Techniques.RollIn).duration(1000).playOn(cardParents);
            YoYo.with(Techniques.RollIn).duration(1000).playOn(cardDriver);
            YoYo.with(Techniques.RollIn).duration(1000).playOn(cardTeacher);
            transection = getSupportFragmentManager().beginTransaction();
            transection = MyHeadActivity.this.getSupportFragmentManager().beginTransaction();
            transection.replace(R.id.myFrame, TeacherFrag.newInstance());
            transection.addToBackStack(null);
            transection.commit();
            setElevation(cardTeacher, cardDriver, cardParents);
        }
    }


}
