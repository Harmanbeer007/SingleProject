package sandhu.harman.singleproject.parent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sandhu.harman.singleproject.R;

public class setSelectedItem extends AppCompatActivity {

    private ListView listview;
    private ProgressDialog pd;
    private ArrayList<String> instituteShowListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_area);
        listview = (ListView) findViewById(R.id.setItemsList);

        pd = new ProgressDialog(this);
        pd.setTitle("Please wait");
        getData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSetFeeArea);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setSupportActionBar(toolbar);

    }

    private void getData() {
        Intent i = getIntent();
        String listing = i.getStringExtra("toBeListed");

        if (listing.equalsIgnoreCase("area")) {
            instituteShowListData = (ArrayList<String>) i.getSerializableExtra("areaList");
        } else if (listing.equalsIgnoreCase("course")) {
            instituteShowListData = (ArrayList<String>) i.getSerializableExtra("courseList");
        }

        UpdateData();
    }

    public void UpdateData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, instituteShowListData);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent setSelectedItem = new Intent();
                setSelectedItem.putExtra("selectedItemText", instituteShowListData.get(i).toString());
                setSelectedItem.putExtra("selectedItem", i);
                setResult(RESULT_OK, setSelectedItem);
                finish();
            }
        });
        pd.dismiss();
    }


}
