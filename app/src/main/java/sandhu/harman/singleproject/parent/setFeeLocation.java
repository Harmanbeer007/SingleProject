package sandhu.harman.singleproject.parent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import sandhu.harman.singleproject.R;

public class setFeeLocation extends AppCompatActivity {

    private ArrayAdapter<String> portAdapter;
    private AutoCompleteTextView byport;
    private Button choosed;
    HashMap<String, List<String>> data_Child = new HashMap<>();
    ArrayList<String> parent = new ArrayList<>();
    ExpandableListView expandableListView;
    private Exp_Adapter_Location myadapter;
    private ExpandableListView ExpandList;
    private byte[] buffer;
    private String bufferString;
    private JSONArray cityArray;
    private JSONObject stateFilterObj;
    private JSONArray cityFilterArray;
    private JSONObject cityFilterObj;
    LinkedHashMap<String, ArrayList<String>> hashMap;
    ArrayList<Exp_Child_Location> city;
    private ArrayList<Exp_Group_Location> state;
    private ArrayList<Exp_Group_Location> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_fee_location);

        byport = (AutoCompleteTextView) findViewById(R.id.idChooseLocation);
        choosed = (Button) findViewById(R.id.Choosed);
        ExpandList = (ExpandableListView) findViewById(R.id.locationExpandableforFee);

//        final String[] PORTS = new String[]{
//                "MUMBAI", "HONG KONG", "OKHA", "PORBANDAR", "DAR ES SALAAM"
//        };
//        portAdapter = new ArrayAdapter<String>(setFeeLocation.this, android.R.layout.simple_dropdown_item_1line, PORTS);
//        byport.setAdapter(portAdapter);
//
//        choosed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String result = byport.getText().toString();
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("location", result);
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
//            }
//        });

        makejsonobjreq();
    }

    private void makejsonobjreq() {

        InputStream is = null;
        try {
            is = getAssets().open("cities.json");

            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            bufferString = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject j = new JSONObject(bufferString);
            list = new ArrayList<Exp_Group_Location>();
            ArrayList<Exp_Child_Location> ch_list;
            cityArray = j.getJSONArray("city_list");

            for (int i = 0; i < cityArray.length(); i++) {
                Exp_Group_Location gru = new Exp_Group_Location();
                stateFilterObj = cityArray.getJSONObject(i);
                gru.setName(stateFilterObj.getString("filter_name"));
                ch_list = new ArrayList<Exp_Child_Location>();
                cityFilterArray = stateFilterObj.getJSONArray("variants");
                for (int cities = 0; cities < cityFilterArray.length(); cities++) {
                    cityFilterObj = cityFilterArray.getJSONObject(cities);
                    Exp_Child_Location ch = new Exp_Child_Location();
                    ch.setName(cityFilterObj.getString("filter_name"));
                    ch_list.add(ch);
                }
                gru.setItems(ch_list);
                list.add(gru);
                myadapter = new Exp_Adapter_Location(setFeeLocation.this, list);
                ExpandList.setAdapter(myadapter);
                ExpandList.setChildDivider(getResources().getDrawable(R.color.white));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ExpandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String result = String.valueOf(list.get(i).getItems().get(i1).getName());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("location", result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return false;
            }
        });


    }

}