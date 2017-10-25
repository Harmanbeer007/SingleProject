package sandhu.harman.singleproject.driver;//package org.tracks;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private ListView lv;
//    private Object sa;
//    private FirebaseDatabase mFirebaseInstance;
//    private DatabaseReference mCollageInstance;
//    private SharedPreferences mPrefs;
//    private String college;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        lv = (ListView) findViewById(R.id.listV);
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap)
//    {
//        mMap = googleMap;
//        mPrefs = getSharedPreferences(getString(R.string.prefs), MODE_PRIVATE);
//        college=mPrefs.getString(getString(R.string.college_Id),"");
//        mFirebaseInstance = FirebaseDatabase.getInstance();
//        mCollageInstance = mFirebaseInstance.getReference("ADMIN/" + college+"/busLocation").child("PB-0055");
//
//        mCollageInstance.limitToFirst(1).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapm: dataSnapshot.getChildren()) {
//
//                    Double lati = snapm.child("lat").getValue(Double.class);
//                    Double longitude = snapm.child("lng").getValue(Double.class);
//                    Double name = snapm.child("power").getValue(Double.class);
//                    Long time=snapm.child("time").getValue(Long.class);
//                    LatLng newLocation = new LatLng(lati, longitude);
//                    mMap.clear();
//
//                    final CameraPosition cameraPosition = new CameraPosition.Builder()
//                            .target(newLocation)
//                            .zoom(17)
//                            .tilt(0)
//                            .bearing(90)
//                            .build();
//                    mMap.addMarker(new MarkerOptions().position(newLocation).title(dataSnapshot.getKey()).icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker_teal)));
//                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                    mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//                        @Override
//                        public boolean onMyLocationButtonClick() {
//                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                            return true;
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
////        mCollageInstance.limitToFirst(1).addChildEventListener(new ChildEventListener() {
////            @Override
////            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
////
////                Toast.makeText(MapsActivity.this,"Exp_Child_Location Added"+ s, Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
////                for (DataSnapshot snapm: dataSnapshot.getChildren()) {
////
////                    Double lati = snapm.child("lat").getValue(Double.class);
////                    Double longitude = snapm.child("lng").getValue(Double.class);
////                    Double name = snapm.child("power").getValue(Double.class);
////                    Long time=snapm.child("time").getValue(Long.class);
////                    LatLng newLocation = new LatLng(lati, longitude);
////                    mMap.clear();
////
////                    final CameraPosition cameraPosition = new CameraPosition.Builder()
////                            .target(newLocation)
////                            .zoom(17)
////                            .tilt(0)
////                            .bearing(90)
////                            .build();
////                    mMap.addMarker(new MarkerOptions().position(newLocation).title(dataSnapshot.getKey()).icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker_teal)));
////                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////                    mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
////                        @Override
////                        public boolean onMyLocationButtonClick() {
////                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////                            return true;
////                        }
////                    });
////            }
////            }
////
////            @Override
////            public void onChildRemoved(DataSnapshot dataSnapshot) {
////
////            }
////
////            @Override
////            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
////
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
////        });
//           }
//
//
//    }
//
