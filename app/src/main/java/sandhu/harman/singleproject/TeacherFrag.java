package sandhu.harman.singleproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Map;

import sandhu.harman.singleproject.admin.AccessInfo;
import sandhu.harman.singleproject.admin.mainscreen;

import static android.content.Context.MODE_PRIVATE;
import static sandhu.harman.singleproject.admin.Loading.dismiss;


public class TeacherFrag extends Fragment {

    private static EditText user;
    SharedPreferences mPrefsUserCheck;
    SharedPreferences mPrefsAdmin;
    SharedPreferences.Editor userPrefEditor;
    SharedPreferences.Editor adminPrefEditor;

    private Button mLoginButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private SwitchCompat mSwitch;
    private Snackbar mSnackbarPermissions;
    private LinkedList<Map<String, Object>> mTransportStatuses = new LinkedList<>();
    private EditText mCollegeIdEditText;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mAccessInstance;
    public static final String TAG = "Admin Login";
    private String scollegeId;
    private String semail;
    private String spassword;
    private myInterface mListener;
    private View inputUserContainer;
    private View inputPasswordContainer;
    private View inputCollegeIdContainer;
    private static TeacherFrag fragment;
    private TextView login;

    private static EditText password;
    public FirebaseAuth mAuth;
    private GoogleApiClient mGoogleClient;
    private ProgressDialog progressDialog;


    public TeacherFrag() {


    }

    public static TeacherFrag newInstance() {
        if (fragment == null) {
            fragment = new TeacherFrag();
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (myInterface) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefsUserCheck = getActivity().getSharedPreferences(getString(R.string.userLoggedPrefs), MODE_PRIVATE);
        mPrefsAdmin = getActivity().getSharedPreferences(getString(R.string.adminPref), MODE_PRIVATE);
        userPrefEditor = mPrefsUserCheck.edit();
        adminPrefEditor = mPrefsAdmin.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);

        inputUserContainer = view.findViewById(R.id.inputUserContainerFrag);
        inputPasswordContainer = view.findViewById(R.id.inputPasswordContainerFrag);
        inputCollegeIdContainer = view.findViewById(R.id.inputCollegeIdContainerFrag);
        login = (TextView) view.findViewById(R.id.btnLoginFrag);

        mEmailEditText = (EditText) view.findViewById(R.id.inputUserFrag);
        mPasswordEditText = (EditText) view.findViewById(R.id.inputPasswordFrag);
        mCollegeIdEditText = (EditText) view.findViewById(R.id.inputCollegeIDFrag);

        scollegeId = mPrefsAdmin.getString(getString(R.string.college_id), "");
        semail = mPrefsAdmin.getString(getString(R.string.email), "");
        spassword = mPrefsAdmin.getString(getString(R.string.password), "");

        mEmailEditText.setText(semail);
        mPasswordEditText.setText(spassword);
        mCollegeIdEditText.setText(scollegeId);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputFields();

            }


        });


        shakeViews();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void shakeViews() {
        YoYo.with(Techniques.ZoomInLeft).duration(1000).playOn(inputUserContainer);
        YoYo.with(Techniques.RollIn).duration(1000).playOn(inputPasswordContainer);
        YoYo.with(Techniques.RollIn).duration(1000).playOn(inputCollegeIdContainer);
        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(login);

    }


    private void checkInputFields() {

        if (mEmailEditText.length() == 0) {
            mEmailEditText.setError("Required");
        } else if (mPasswordEditText.length() == 0) {
            mPasswordEditText.setError("Required");
        } else if (mCollegeIdEditText.length() == 0) {
            mCollegeIdEditText.setError("Required");
        } else {
            // Store values.
            adminPrefEditor.putString(getString(R.string.college_id), mCollegeIdEditText.getText().toString());
            adminPrefEditor.putString(getString(R.string.email), mEmailEditText.getText().toString());
            adminPrefEditor.putString(getString(R.string.password), mPasswordEditText.getText().toString());
            adminPrefEditor.apply();
            authenticate(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString(), mCollegeIdEditText.getText().toString().trim());
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Validating Login Please wait");
            progressDialog.show();
        }
    }


    private void authenticate(final String email, String password, final String college) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        Log.i(TAG, "authenticate: " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            progressDialog.setMessage("Checking Account Permissions");
                            reauthenticate(email, college);
                        } else {
                            Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(getActivity(), "Please Check Your Login Details", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            progressDialog.dismiss();
                        }
                    }
                });
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
                        Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getActivity(), mainscreen.class));

                        userPrefEditor.putString(getString(R.string.userLogged), em);
                        userPrefEditor.putString(getString(R.string.userType), "teacher");
                        userPrefEditor.apply();
                        mListener.onComplete();

                    } else {
                        Toast.makeText(getActivity(), "You are Not authorized to access this college/school data,Please check your Credentials", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                } else {
                    Toast.makeText(getContext(), "Your College Does Not Exist In Our Database ,Please Contact Us If You Want To Be a Member", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

}
