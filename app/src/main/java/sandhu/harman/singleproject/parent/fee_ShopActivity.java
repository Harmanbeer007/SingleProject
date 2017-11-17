package sandhu.harman.singleproject.parent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import sandhu.harman.singleproject.R;
import sandhu.harman.singleproject.cart.CartHead;

public class fee_ShopActivity extends CartHead {

    public JSONObject homepage_layoutObj;
    public JSONArray homepage_layout;
    private String url;
    private String layout_type;
    private JSONArray itemsArray;
    private JSONObject itemsArrayObj;

    private String[] sampleNetworkImageURLs;
    private ProgressBar progressBar;
    private ArrayList<CarouselModel> data;

    private JSONObject jsonObjectData;
    private SectionAdapterRecycler data1Section;
    private HashMap<String, JSONObject> sections;
    private SectionedRecyclerViewAdapter sectionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fee_shop_activity);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        sectionAdapter = new SectionedRecyclerViewAdapter();
        sections = new LinkedHashMap<>();
        setHead();
        getData();
        setCartIcon();

    }

    private void getData() {
        url = "https://catalog.paytm.com/v1/h/books-media-stationery/stationery";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    homepage_layout = jsonObject.getJSONArray("homepage_layout");
                    for (int i = 0; i < homepage_layout.length(); i++) {
                        homepage_layoutObj = homepage_layout.getJSONObject(i);
                        String name = homepage_layoutObj.getString("name");
                        sections.put(name, homepage_layoutObj);
//
                    }
                    UpdateView();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(fee_ShopActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(fee_ShopActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                if (volleyError instanceof TimeoutError) {
                    getData();
                } else if (volleyError instanceof NetworkError) {
                    getData();
                }

            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    public void UpdateView() {
        Iterator it = sections.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            data1Section = new SectionAdapterRecycler(fee_ShopActivity.this, pair.getKey().toString(), (JSONObject) pair.getValue());
            sectionAdapter.addSection(data1Section);
            it.remove();
        }

        final RecyclerView recyclerView3 = (RecyclerView) findViewById(R.id.recyler);
        recyclerView3.setLayoutManager(new LinearLayoutManager(fee_ShopActivity.this));
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setAdapter(sectionAdapter);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public Double updateBill() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartIcon();
    }
}

