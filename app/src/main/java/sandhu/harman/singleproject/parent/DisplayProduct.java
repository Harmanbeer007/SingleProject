package sandhu.harman.singleproject.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import sandhu.harman.singleproject.R;

/**
 * Created by harman on 07-11-2017.
 */

public class DisplayProduct extends AppCompatActivity {
    private ArrayList<ProductListGridModel> productItems;
    private RecyclerView productsRec;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view_layout);
        productsRec = (RecyclerView) findViewById(R.id.productsRecyler);
        productItems = new ArrayList<>();
        Intent i = getIntent();
        productItems = i.getExtras().getParcelableArrayList("dataProducts");

        AdapterHandleProducts adapter = new AdapterHandleProducts(DisplayProduct.this, productItems);
        productsRec.setLayoutManager(new GridLayoutManager(DisplayProduct.this, 2));
        productsRec.setAdapter(adapter);

    }
}
