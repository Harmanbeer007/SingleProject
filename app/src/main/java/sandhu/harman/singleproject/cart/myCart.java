package sandhu.harman.singleproject.cart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sandhu.harman.singleproject.R;
import sandhu.harman.singleproject.parent.listner.BillListner;

public class myCart extends AppCompatActivity implements BillListner {

    private RecyclerView recyclerView;
    private List<ModelProducts> myproducts = new ArrayList<ModelProducts>();
    private Double totalPrice;
    private Button pay_Bill;
    private FrameLayout emptycart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        recyclerView = (RecyclerView) findViewById(R.id.myCartRecyler);
        pay_Bill = (Button) findViewById(R.id.paymentButton);
        emptycart = (FrameLayout) findViewById(R.id.nothingIncartFrame);

        CartDb cartDb = new CartDb(this);
        myproducts = cartDb.fetchingData();

        CartAdapter cartAdapter = new CartAdapter(this, myproducts);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
        if (myproducts.size() == 0) {
            pay_Bill.setVisibility(View.GONE);
            emptycart.setVisibility(View.VISIBLE);

        }
        pay_Bill.setText("Pay For ₹" + String.valueOf(updateBill()));


    }

    public void goBack(View v) {
        finish();
    }

    @Override
    public Double updateBill() {
        Double price = 0.0d;
        int qnty = 0;
        Double total = 0.0d;
        try {
            CartDb cartDb = new CartDb(this);


            for (int i = 0; i < myproducts.size(); i++) {
                price = Double.valueOf(myproducts.get(i).getProductPrice());
                qnty = cartDb.checkCart(myproducts.get(i).getProductName());
                total += price * qnty;

            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        pay_Bill.setText("Pay For ₹" + String.valueOf(total));
        if (myproducts.size() == 0) {
            pay_Bill.setVisibility(View.GONE);
            emptycart.setVisibility(View.VISIBLE);
        }
        return total;

    }


}
