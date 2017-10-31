package sandhu.harman.singleproject.parent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import sandhu.harman.singleproject.R;

public class setFee_Institute extends AppCompatActivity {

    RecyclerView recyclerView;
    private ProgressDialog pd;
    private ArrayList<Institute_Model> instituteData;
    private String url;
    private byte[] buffer;
    private String bufferString;
    private JSONObject instituesObj;
    private Institute_Adapter_Parent recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_fee__institute);
        recyclerView = (RecyclerView) findViewById(R.id.set_Institute_Recycler);
        pd = new ProgressDialog(this);
        pd.setTitle("Please wait");
        getData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSetFeeInstitute);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager recycle = new GridLayoutManager(setFee_Institute.this, 3);
        recyclerView.setLayoutManager(recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setSupportActionBar(toolbar);

    }

    private void getData() {
        Intent i = getIntent();
        instituteData = (ArrayList<Institute_Model>) i.getSerializableExtra("institutelist");
        UpdateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        //handle the home button onClick event here.
        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                recyclerAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

//    private void getData() {
//        InputStream is = null;
//        try {
//            is = getAssets().open("school_data2.json");
//
//            int size = is.available();
//            buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            bufferString = new String(buffer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            JSONObject j = new JSONObject(bufferString);
//            instituteData = new ArrayList<Institute_Model>();
//            JSONArray instituteArray = j.getJSONArray("InstituteList");
//            for (int i = 0; i < instituteArray.length(); i++) {
//                instituesObj = instituteArray.getJSONObject(i);
//                Institute_Model newInstitute = new Institute_Model();
//                newInstitute.setInstituteName(instituesObj.getString("name"));
//                newInstitute.setInsttuteImage(instituesObj.getString("brand_image"));
//                newInstitute.setUrl(instituesObj.getString("url"));
//                instituteData.add(newInstitute);
//            }
//            UpdateData();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    public void UpdateData() {
        recyclerAdapter = new Institute_Adapter_Parent(setFee_Institute.this, instituteData);
        recyclerView.setAdapter(recyclerAdapter);
        pd.dismiss();
    }

}
