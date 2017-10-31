package sandhu.harman.singleproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import sandhu.harman.singleproject.parent.College_Id;

import static android.content.Context.MODE_PRIVATE;


public class PArentFrag extends Fragment implements View.OnClickListener {


    private static final String TAG = "GoogleLoginActivity";
    private static final int RC_SIGN_IN = 4277;
    private static PArentFrag fragment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private SharedPreferences mParentPrefs;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private View view;
    myInterface mListener;
    private SharedPreferences.Editor parentPrefEditor;

    public PArentFrag() {
        // Required empty public constructor
    }


    public static PArentFrag newInstance() {
        if (fragment == null) {
            fragment = new PArentFrag();
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent, container, false);

        mStatusTextView = (TextView) view.findViewById(R.id.status);
        mDetailTextView = (TextView) view.findViewById(R.id.detail);

        view.findViewById(R.id.sign_in_button).setOnClickListener(this);
        view.findViewById(R.id.sign_out_button).setOnClickListener(this);
        view.findViewById(R.id.getBusList_button).setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.userLoggedPrefs), MODE_PRIVATE);
        mParentPrefs = getActivity().getSharedPreferences(getString(R.string.parentPref), MODE_PRIVATE);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getContext(), "Google Play Services error.", Toast.LENGTH_SHORT).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mAuth = FirebaseAuth.getInstance();

        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event

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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.getBusList_button) {
            startActivity(new Intent(getContext(), College_Id.class));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();

        }
        parentPrefEditor = mParentPrefs.edit();
        parentPrefEditor.putString(getString(R.string.college_Id), "");
        parentPrefEditor.apply();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                        editor.putString(getString(R.string.userLogged), "");
                        editor.putString(getString(R.string.userType), "");
                        editor.apply();
                        mListener.onComplete();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
        editor = sharedPreferences.edit();
        if (user != null) {
            editor.putString(getString(R.string.userLogged), user.getEmail().toString().trim());
            editor.putString(getString(R.string.userType), "parents");
            editor.apply();
            mStatusTextView.setText(getString(R.string.parent_status) + user.getEmail());
            view.findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            mListener.onComplete();

        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);
            view.findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            view.findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Log.d(TAG, mAuth.getCurrentUser().toString());
                            Toast.makeText(getContext(), mAuth.getCurrentUser().getDisplayName().toString(), Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getContext(), College_Id.class));
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(getContext(), "Failed to login", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), result.getStatus().getStatusMessage().toString(), Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();

        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

}
