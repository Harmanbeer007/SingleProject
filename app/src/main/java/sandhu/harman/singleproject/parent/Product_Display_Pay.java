package sandhu.harman.singleproject.parent;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import sandhu.harman.singleproject.R;
import sandhu.harman.singleproject.cart.CartController;
import sandhu.harman.singleproject.cart.CartDb;
import sandhu.harman.singleproject.cart.CartHead;
import sandhu.harman.singleproject.cart.myCart;

public class Product_Display_Pay extends CartHead implements PaymentResultListener {

    public JSONObject resultObj;
    private ProgressBar progressBar;
    private List imageArray;
    private TextView txtProductName, txtpoductPrice, txtpoductActualPrice, txtpoductPriceDiscount;
    private Button payProceedBtn;
    private ViewPager mPager;
    private LinearLayout payProductDiscLayout;
    private JSONObject objDisc;
    private JSONObject objDiscAttribute;
    private LinearLayout linearLayout;
    private CartController ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__display__pay);
        progressBar = (ProgressBar) findViewById(R.id.productDisplayLoading);
        progressBar.setVisibility(View.VISIBLE);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtpoductPrice = (TextView) findViewById(R.id.txtpoductPrice);
        payProceedBtn = (Button) findViewById(R.id.btn_PayForProduct);
        setHead();
        setCartIcon();
        // TODO: 14-11-2017 Cart Implementation
        ct = (CartController) getApplicationContext();

        payProductDiscLayout = (LinearLayout) findViewById(R.id.payProductDisc);
        txtpoductActualPrice = (TextView) findViewById(R.id.txtpoductActualPrice);
        txtpoductPriceDiscount = (TextView) findViewById(R.id.txtpoductPriceDiscount);
        imageArray = new ArrayList();
        String url = getIntent().getStringExtra("productUrl");
        getProductDetails(url);
        payProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkOut();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getProductDetails(String url) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(params), new Response.Listener<JSONObject>() {

            public JSONObject otherImageObj;
            public JSONArray otherImageArray;

            @Override
            public void onResponse(JSONObject jsonObject) {
                progressBar.setVisibility(View.GONE);
                try {
                    resultObj = jsonObject;
                    imageArray.add(resultObj.getString("image_url"));
                    otherImageArray = jsonObject.getJSONArray("other_images");
                    for (int i = 0; i < otherImageArray.length(); i++) {
                        imageArray.add(otherImageArray.getString(i));
                    }
                    getProductDetail();
                    getProductDisc();
                    init();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Product_Display_Pay.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void getProductDetail() throws JSONException {
        txtProductName.setText(resultObj.getString("name"));
        txtpoductPrice.setText("₹ " + resultObj.getString("offer_price"));
        txtpoductActualPrice.setText("₹ " + resultObj.getString("actual_price"));
        txtpoductActualPrice.setPaintFlags(txtpoductActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtpoductPriceDiscount.setText(resultObj.getString("discount"));
        payProceedBtn.setText("Pay For ₹" + resultObj.getString("offer_price"));

    }

    private void getProductDisc() throws JSONException {
        JSONArray arrayDisc = resultObj.getJSONArray("long_rich_desc");
        for (int i = 0; i < arrayDisc.length(); i++) {
            objDisc = arrayDisc.getJSONObject(i);
            CardView cardView = new CardView(Product_Display_Pay.this);
            cardView.setCardElevation(5.0f);
            cardView.setPadding(5, 5, 5, 5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 2, 0, 3);
            cardView.setLayoutParams(params);
            linearLayout = new LinearLayout(Product_Display_Pay.this);


            TextView textView = new TextView(Product_Display_Pay.this);
            textView.setText(objDisc.getString("title").replaceAll("<Br>", ""));
            textView.setTextColor(Color.BLACK);
            textView.setPadding(5, 5, 5, 5);
            linearLayout.addView(textView);
            objDiscAttribute = objDisc.getJSONObject("attributes");
            if (objDiscAttribute.has("Warranty Summary")) {
                String des = objDiscAttribute.getString("Warranty Summary").replaceAll("<Br>", "");
                TextView textView1 = new TextView(Product_Display_Pay.this);
                textView1.setText(des);
                textView.setPadding(5, 5, 5, 5);
                linearLayout.addView(textView1);
            }

            if (objDisc.has("description")) {
                String des = objDisc.getString("description").replaceAll("<Br>", "");
                String des2 = des.replaceAll("<br>", "\r\t");

                TextView textView1 = new TextView(Product_Display_Pay.this);
                textView1.setText(des2);
                textView.setPadding(5, 5, 5, 5);
                linearLayout.addView(textView1);
            }

            if (objDiscAttribute.has("Return Policy")) {
                String des = objDiscAttribute.getString("Return Policy").replaceAll("<br>", "");
                TextView textView1 = new TextView(Product_Display_Pay.this);
                textView1.setText(des);
                textView.setPadding(5, 5, 5, 5);
                linearLayout.addView(textView1);
            }
            cardView.addView(linearLayout);
            payProductDiscLayout.addView(cardView);

        }
    }

    private void checkOut() throws JSONException {

        CartDb cartDb = new CartDb(Product_Display_Pay.this);
        if (cartDb.checkCart(resultObj.getString("name")) == 0) {
            cartDb.addData(resultObj.getString("name"), "Description", resultObj.getDouble("offer_price"), resultObj.getString("actual_price"), resultObj.getString("image_url"), resultObj.getString("discount"));
        } else {
            int quantity = cartDb.checkCart(resultObj.getString("name"));
            quantity++;
            cartDb.updateData(resultObj.getString("name"), quantity);
            Toast.makeText(Product_Display_Pay.this, "Product Already In Cart,New Order Quantity Updated to " + String.valueOf(quantity), Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(Product_Display_Pay.this, myCart.class));
        // TODO: 15-11-2017 the commented part is to process cart payment .
//        final Checkout co = new Checkout();

//        try {
//            String description;
//            JSONObject options = new JSONObject();
//            options.put("name", resultObj.getString("name"));
//            description = resultObj.getString("name");
//            options.put("description", description);
//            //You can omit the image option to fetch the image from dashboard
////            options.put("image", "https://firebasestorage.googleapis.com/v0/b/byefirebaseproject.appspot.com/o/logo_100.png?alt=media&token=ed4abfa5-e396-49e8-ae3c-9af24692e4c6");
//            options.put("image", resultObj.getString("thumbnail"));
//            options.put("currency", "INR");
//            options.put("amount", Double.valueOf(resultObj.getString("offer_price")) * 100);
//
//            JSONObject preFill = new JSONObject();
//            fire = FirebaseAuth.getInstance();
//            user = fire.getCurrentUser();
//            if (user != null) {
//                preFill.put("email", user.getEmail());
//                preFill.put("contact", user.getPhoneNumber());
//            } else {
//                preFill.put("email", "");
//                preFill.put("contact", "");
//            }
//            options.put("prefill", preFill);
//            co.open(Product_Display_Pay.this, options);
//
//
//        } catch (Exception e) {
//            Toast.makeText(Product_Display_Pay.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
//                    .show();
//            e.printStackTrace();
//        }
    }


    private void init() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SliderAdapter(Product_Display_Pay.this, imageArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
    }

    @Override
    public void onPaymentSuccess(String s) {
//        try {
        Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
//            String url = "https://bareheaded-signatur.000webhostapp.com/umangcare/institute.php";
//            userdetails.put("Transection_number", s);
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(userdetails), new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    Toast.makeText(PayFees.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    Toast.makeText(PayFees.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            Volley.newRequestQueue(PayFees.this).add(jsonObjectRequest);
//
//        } catch (Exception e) {
//            Log.e(TAG, "Exception in onPaymentSuccess", e);
//        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment failed : " + i + " " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
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
