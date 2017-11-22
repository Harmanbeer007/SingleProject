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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sandhu.harman.singleproject.R;
import sandhu.harman.singleproject.parent.listner.BillListner;

public class myCart extends AppCompatActivity implements BillListner, PaymentResultListener {

    private RecyclerView recyclerView;
    private List<ModelProducts> myproducts = new ArrayList<ModelProducts>();
    private Double totalPrice;
    private Button pay_Bill;
    private FrameLayout emptycart;
    private FirebaseAuth fire;
    private FirebaseUser user;
    private StringBuilder pName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        recyclerView = (RecyclerView) findViewById(R.id.myCartRecyler);
        pay_Bill = (Button) findViewById(R.id.paymentButton);
        emptycart = (FrameLayout) findViewById(R.id.nothingIncartFrame);

        final CartDb cartDb = new CartDb(this);
        myproducts = cartDb.fetchingData();
        pName = new StringBuilder();

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
        pay_Bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Checkout co = new Checkout();

                try {
                    String description;
                    JSONObject options = new JSONObject();
//            LinkedList<String> products=new LinkedList<>();
//           products= cartDb.getAllProductsName();

                    for (int i = 0; i < myproducts.size(); i++) {
                        pName.append(myproducts.get(i).getProductName() + "Rs( " + myproducts.get(i).getProductPrice() + ")" + " , ");
                    }
                    options.put("name", pName);
                    description = pName.toString();
                    options.put("description", "multiple products in cart");
                    options.put("image", "https://firebasestorage.googleapis.com/v0/b/byefirebaseproject.appspot.com/o/logo_100.png?alt=media&token=ed4abfa5-e396-49e8-ae3c-9af24692e4c6");
                    options.put("currency", "INR");
                    options.put("amount", Double.valueOf(String.valueOf(updateBill())) * 100);
                    JSONObject preFill = new JSONObject();
                    fire = FirebaseAuth.getInstance();
                    user = fire.getCurrentUser();
                    if (user != null) {
                        preFill.put("email", user.getEmail());
                        preFill.put("contact", user.getPhoneNumber());
                    } else {
                        preFill.put("email", "");
                        preFill.put("contact", "");
                    }
                    options.put("prefill", preFill);
                    co.open(myCart.this, options);


                } catch (Exception e) {
                    Toast.makeText(myCart.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    e.printStackTrace();
                }
            }
        });


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


    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        CartDb cartDb = new CartDb(myCart.this);
        cartDb.getAllProductsName();
        cartDb.getAllProductsPrices();
        for (int j = 0; j < myproducts.size(); j++) {
            String name = myproducts.get(j).getProductName();
            String price = myproducts.get(j).getProductPrice().toString();
            String qnty = String.valueOf(cartDb.checkCart(name));
            String off = myproducts.get(j).getOff_on_product();
            String actual_price = myproducts.get(j).getProduct_actual_price();
            String img = myproducts.get(j).getProduct_image_url();
            String desc = myproducts.get(j).getProductDesc();
            String txnId = s;
            try {
                sendToServer(txnId, name, price, qnty, desc, actual_price, off, img, String.valueOf(j));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendToServer(String transection_Id, String itemName, String itemPrice, String qnty, String desc, String actual_price, String off, String img_url, String counted) throws JSONException {
        String url = "https://bareheaded-signatur.000webhostapp.com/umangcare/transe.php";

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
//        String json_string = "{\"upload_transections\":[";
        final HashMap<String, String> j = new HashMap<>();
        j.put("transection_number", transection_Id);
        j.put("item_name", itemName);
        j.put("item_price", itemPrice);
        j.put("item_qnty", qnty);
        j.put("item_desc", desc);
        j.put("item_actual_price", actual_price);
        j.put("item_off", off);
        j.put("item_img_url", img_url);
        j.put("user_mobile", user.getPhoneNumber());
        j.put("user_email", user.getEmail());
        j.put("user_address", "not yet added");
        j.put("count", counted);
        j.put("timestamp", ts);
//        json_string = json_string + j.toString() + ",";
////Close JSON string
//        json_string = json_string.substring(0, json_string.length() - 1);
//        json_string += "]}";

        RequestQueue requestQueue = Volley.newRequestQueue(myCart.this);

        Toast.makeText(this, j.toString(), Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Toast.makeText(myCart.this, s, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(myCart.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return j;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s + String.valueOf(i), Toast.LENGTH_SHORT).show();

    }
}
