package sandhu.harman.singleproject;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sandhu.harman.singleproject.driver.TrackerService;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.POWER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DriverFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DriverFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverFrag extends Fragment {

    private static DriverFrag fragment;
    private EditText mCollegeIdEdit;
    private EditText mDriverIdEdit;
    private SharedPreferences mDriverPrefs;
    private TextView mStartButton;
    private SwitchCompat mSwitch;
    private View view;
    myInterface mListner;
    private Snackbar mSnackbarPermissions;
    private Snackbar mSnackbarGps;
    private static final int PERMISSIONS_REQUEST = 1;
    private static String[] PERMISSIONS_REQUIRED = new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ViewGroup viewgrp;
    private static Snackbar snackbar;

    public DriverFrag() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DriverFrag newInstance() {

        if (fragment == null) {
            fragment = new DriverFrag();
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
        if (isAdded()) {
            view = inflater.inflate(R.layout.fragment_driver, container, false);
            viewgrp = container;

            mCollegeIdEdit = (EditText) view.findViewById(R.id.inputCollegeIDDriverFrag);
            mDriverIdEdit = (EditText) view.findViewById(R.id.inputDriverIdFrag);
            mStartButton = (TextView) view.findViewById(R.id.btnLoginDriverFrag);
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkInputFields();
                }
            });


            mDriverPrefs = getActivity().getSharedPreferences(getString(R.string.driverPref), MODE_PRIVATE);
            String collegeID = mDriverPrefs.getString(getString(R.string.college_Id), "");
            String driverid = mDriverPrefs.getString(getString(R.string.driverId), "");

            snackbar = Snackbar
                    .make(viewgrp, getString(R.string
                            .gps_required), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.enable, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
            mCollegeIdEdit.setText(collegeID);
            mDriverIdEdit.setText(driverid);


            if (isServiceRunning(TrackerService.class)) {
                // If service already running, simply update UI.
                setTrackingStatus(R.string.tracking);

            } else if (collegeID.length() > 0 && driverid.length() > 0) {
                // Inputs have previously been stored, start validation.
                checkLocationPermission();
            } else {
                // First time running - check for inputs pre-populated from build.
//            mCollegeIdEdit.setHint(getString(R.string.college_id));
//            mDriverIdEdit.setHint(getString(R.string.driverId));
            }

        }
        return view;

    }


    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setTrackingStatus(int status) {
        boolean tracking = status == R.string.tracking;
        mCollegeIdEdit.setEnabled(!tracking);
        mDriverIdEdit.setEnabled(!tracking);
        mStartButton.setVisibility(tracking ? View.INVISIBLE : View.VISIBLE);

//        if (mSwitch != null) {
//            // Initial broadcast may come before menu has been initialized.
//            mSwitch.setChecked(tracking);
//        }
        ((TextView) view.findViewById(R.id.textTrack)).setText(getString(status));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTrackingStatus(intent.getIntExtra(getString(R.string.status), 0));
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListner = (myInterface) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(TrackerService.STATUS_INTENT));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        if (snackbar.isShown()) {
            snackbar.dismiss();
        }
        super.onPause();
    }

    private void checkInputFields() {
        if (mCollegeIdEdit.length() == 0 || mDriverIdEdit.length() == 0) {
            Toast.makeText(getContext(), R.string.missing_inputs, Toast.LENGTH_SHORT).show();
        } else {
            // Store values.
            SharedPreferences.Editor editor = mDriverPrefs.edit();
            editor.putString(getString(R.string.college_Id), mCollegeIdEdit.getText().toString());
            editor.putString(getString(R.string.driverId), mDriverIdEdit.getText().toString());
            editor.apply();
            // Validate permissions.
            checkLocationPermission();
//            mSwitch.setEnabled(true);
        }
    }

    private void checkLocationPermission() {
        int locationPermission = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (locationPermission != PackageManager.PERMISSION_GRANTED
                || storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST);
        } else {
            checkGpsEnabled();
        }
    }

    private void checkGpsEnabled() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            reportGpsError();
        } else {
            resolveGpsError();
            startLocationService();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            // We request storage perms as well as location perms, but don't care
            // about the storage perms - it's just for debugging.
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        reportPermissionsError();
                    } else {
                        resolvePermissionsError();
                        checkGpsEnabled();
                    }
                }
            }
        }
    }

    private void startLocationService() {
        // Before we start the service, confirm that we have extra power usage privileges.
        PowerManager pm = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!pm.isIgnoringBatteryOptimizations(getActivity().getPackageName())) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                startActivity(intent);
            }
        }
        getActivity().startService(new Intent(getContext(), TrackerService.class));
    }

    private void stopLocationService() {
        getActivity().stopService(new Intent(getContext(), TrackerService.class));
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//       inflater.inflate(R.menu.main_activity, menu);
//
//        // Get the action view used in your toggleservice item
//        final MenuItem toggle = menu.findItem(R.id.menu_switch);
//        mSwitch = (SwitchCompat) toggle.getActionView().findViewById(R.id.switchInActionBar);
//        mSwitch.setEnabled(mCollegeIdEdit.length() > 0 && mDriverIdEdit.length() > 0);
//        mSwitch.setChecked(mStartButton.getVisibility() != View.VISIBLE);
//        mSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((SwitchCompat) v).isChecked()) {
//                    checkInputFields();
//                } else {
//                    confirmStop();
//                }
//            }
//        });
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        final MenuItem toggle = menu.findItem(R.id.menu_switch);
//        mSwitch = (SwitchCompat) toggle.getActionView().findViewById(R.id.switchInActionBar);
//        mSwitch.setEnabled(mCollegeIdEdit.length() > 0 && mDriverIdEdit.length() > 0);
//        mSwitch.setChecked(mStartButton.getVisibility() != View.VISIBLE);
//        mSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((SwitchCompat) v).isChecked()) {
//                    checkInputFields();
//                } else {
//                    confirmStop();
//                }
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }

    private void confirmStop() {
//        mSwitch.setChecked(true);
        new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.confirm_stop))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSwitch.setChecked(false);
                        mCollegeIdEdit.setEnabled(true);
                        mDriverIdEdit.setEnabled(true);
                        mStartButton.setVisibility(View.VISIBLE);
                        stopLocationService();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void reportPermissionsError() {
//        if (mSwitch != null) {
//            mSwitch.setChecked(false);
//        }
        Snackbar snackbar = Snackbar
                .make(
                        viewgrp,
                        getString(R.string.location_permission_required),
                        Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.enable, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings
                                .ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                        startActivity(intent);
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(
                android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void resolvePermissionsError() {
        if (mSnackbarPermissions != null) {
            mSnackbarPermissions.dismiss();
            mSnackbarPermissions = null;
        }
    }

    private void reportGpsError() {
//        if (mSwitch != null) {
//            mSwitch.setChecked(false);
//        }


        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id
                .snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }

    private void resolveGpsError() {
        if (mSnackbarGps != null) {
            mSnackbarGps.dismiss();
            mSnackbarGps = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().startService(new Intent(getContext(), TrackerService.class)); // add this line
    }

}
