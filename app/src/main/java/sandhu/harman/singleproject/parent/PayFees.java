package sandhu.harman.singleproject.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import sandhu.harman.singleproject.R;

public class PayFees extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {
    private static final int FEE_LOCATION_CODE = 562;
    private static final int FEE_INSTITUTE_CODE = 563;
    private static final int FEE_AREA_CODE = 564;
    private static final int FEE_COURSE_CODE = 565;
    private static final String TAG = "PAyment Testing";
    private EditText fee_location, fee_institute, fee_area, pay_fee_Course, pay_fee_Amount;
    private TextInputLayout textInput_fee_Area, textInput_fee_Course, textInput_fee_Amount;
    private Button btn_PrceedFeePay;
    private byte[] buffer;
    private String bufferString;
    private ArrayList<Institute_Model> instituteData;
    private JSONObject instituesObj;
    private JsonObjectRequest instDetails;
    private JSONArray inputFields;
    private JSONObject inputFieldsObj;
    private LinearLayout linearLayout;
    private JSONObject areaObject;
    private JSONObject variantsCourseObj;
    private JSONArray variantsCourseInformation;
    private JSONObject variantsCourseInformationObj;
    private RadioGroup feeType;
    private ArrayList<String> areaList;
    private ArrayList<String> variantsCourseList;
    private ArrayList<String> variantsCourseFieldsList;
    private JSONArray variantsCourse;
    private ProgressBar progressBar;
    private RadioButton newRegistration;
    private EditText editTextFields;
    private HashMap<String, String> userdetails;
    private boolean fieldsNotEmpty = true;
    private HashMap<String, String> regex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fees);
        fee_location = (EditText) findViewById(R.id.pay_fee_location);
        fee_institute = (EditText) findViewById(R.id.pay_fee_institute);
        pay_fee_Course = (EditText) findViewById(R.id.pay_fee_Course);
        pay_fee_Amount = (EditText) findViewById(R.id.pay_fee_Amount);

        linearLayout = (LinearLayout) findViewById(R.id.pay_fee_institute_Layout);
        textInput_fee_Area = (TextInputLayout) findViewById(R.id.textInput_fee_Area);
        textInput_fee_Course = (TextInputLayout) findViewById(R.id.textInput_fee_Course);
        textInput_fee_Amount = (TextInputLayout) findViewById(R.id.textInput_fee_Amount);
        final RadioButton feePayment = (RadioButton) findViewById(R.id.FeePaymentRadio);
        regex = new HashMap<>();

        feeType = (RadioGroup) findViewById(R.id.feeTypeGroup);
        newRegistration = (RadioButton) findViewById(R.id.newRegistrationPaymentRadio);
        bindFeeInstitutes(1);
        feeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selected = radioGroup.getCheckedRadioButtonId();
                clearData();
                if (selected == newRegistration.getId()) {
                    bindFeeInstitutes(1);
                } else if (selected == feePayment.getId()) {
                    bindFeeInstitutes(2);
                }
            }
        });
        userdetails = new HashMap<>();
        areaList = new ArrayList<String>();
        variantsCourseList = new ArrayList<String>();
        variantsCourseFieldsList = new ArrayList<String>();
        fee_area = (EditText) findViewById(R.id.pay_fee_Area);
        btn_PrceedFeePay = (Button) findViewById(R.id.proceed_Payment);

        fee_institute.setOnClickListener(this);
        fee_area.setOnClickListener(this);
        pay_fee_Course.setOnClickListener(this);
        btn_PrceedFeePay.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.pay_fee_location: {
                startActivityForResult(new Intent(PayFees.this, setFeeLocation.class), FEE_LOCATION_CODE);
                break;
            }
            case R.id.pay_fee_institute: {
                clearData();
                startActivityForResult(new Intent(PayFees.this, setFee_Institute.class).putExtra("institutelist", instituteData), FEE_INSTITUTE_CODE);
                break;
            }
            case R.id.pay_fee_Area: {
                startActivityForResult(new Intent(PayFees.this, setSelectedItem.class).putExtra("toBeListed", "area").putExtra("areaList", areaList), FEE_AREA_CODE);
                break;
            }
            case R.id.pay_fee_Course: {
                startActivityForResult(new Intent(PayFees.this, setSelectedItem.class).putExtra("toBeListed", "course").putExtra("courseList", variantsCourseList), FEE_COURSE_CODE);
                break;
            }
            case R.id.proceed_Payment: {
                {
                    if (fee_institute.getText().length() == 0) {
                        Toast.makeText(this, "Please Select a institute", Toast.LENGTH_SHORT).show();
                    } else if (fee_area.getText().length() == 0) {
                        Toast.makeText(this, "Please Select institute area", Toast.LENGTH_SHORT).show();

                    } else {
                        userdetails.clear();
                        fieldsNotEmpty = true;
                        getDynamicViewData(linearLayout);
                        if (fieldsNotEmpty) {
                            if (pay_fee_Amount.getText().length() == 0) {
                                pay_fee_Amount.setError("Fee Amount Can't Be Empty");
                            } else {
                                userdetails.put("Fee_Institute", fee_institute.getText().toString());
                                userdetails.put("Fee_area", fee_area.getText().toString());
                                userdetails.put("Fee_amount", pay_fee_Amount.getText().toString());
                                userdetails.put("Fee_course", pay_fee_Course.getText().toString());
                                checkOut();
                                Toast.makeText(this, "Going To Payment", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    break;
                }
            }
        }
    }

    private void checkOut() {
        final Checkout co = new Checkout();

        try {
            String description;
            JSONObject options = new JSONObject();
            options.put("name", fee_institute.getText().toString());
            if (textInput_fee_Course.getVisibility() == View.VISIBLE) {
                description = pay_fee_Course.getText().toString() + " of Institute " + fee_institute.getText().toString();
            } else {
                description = "Registration Fee For :" + userdetails.get("Registration Number") + " of Institute " + fee_institute.getText().toString();
            }
            options.put("description", description);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://firebasestorage.googleapis.com/v0/b/byefirebaseproject.appspot.com/o/logo_100.png?alt=media&token=ed4abfa5-e396-49e8-ae3c-9af24692e4c6");
            options.put("currency", "INR");
            options.put("amount", Double.valueOf(pay_fee_Amount.getText().toString()) * 100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", userdetails.get("Email Id"));
            preFill.put("contact", userdetails.get("Mobile number"));
            options.put("prefill", preFill);
            co.open(PayFees.this, options);


        } catch (Exception e) {
            Toast.makeText(PayFees.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    private void clearData() {
        fee_area.setText("");
        textInput_fee_Area.setVisibility(View.GONE);
        textInput_fee_Course.setVisibility(View.GONE);
        textInput_fee_Amount.setVisibility(View.GONE);
        linearLayout.removeAllViews();
        fee_institute.setText("");
    }


    private void getcourses(int id) {
        try {
            variantsCourseList.clear();
            for (int i = 0; i < variantsCourse.length(); i++) {
                variantsCourseObj = variantsCourse.getJSONObject(i);
                variantsCourseList.add(variantsCourseObj.getString("course"));

            }
            pay_fee_Course.setText("");
            if (variantsCourseObj.getString("course").contains("N/A")) {
                textInput_fee_Course.setVisibility(View.GONE);
                getInputFields();
            } else if (variantsCourseList.size() != 0) {
                textInput_fee_Course.setVisibility(View.VISIBLE);
                getInputFields();
            } else {
                getInputFields();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getInputFields() {
        try {
            variantsCourseInformation = variantsCourseObj.getJSONArray("products");

            for (int k = 0; k < variantsCourseInformation.length(); k++) {
//
                variantsCourseInformationObj = variantsCourseInformation.getJSONObject(k);
                inputFields = variantsCourseInformationObj.getJSONArray("input_fields");

                // TODO: 30-10-2017 get input fields
                linearLayout.removeAllViews();
                for (int l = 0; l < inputFields.length(); l++) {

                    inputFieldsObj = inputFields.getJSONObject(l);
                    variantsCourseFieldsList.add(inputFieldsObj.getString("title"));
                    if (inputFieldsObj.has("regex")) {
                        regex.put(inputFieldsObj.getString("title"), inputFieldsObj.getString("regex"));
                    }
                    TextInputLayout txt = new TextInputLayout(PayFees.this);
                    txt.setHint(inputFieldsObj.getString("title"));
                    editTextFields = new EditText(PayFees.this);
                    editTextFields.setId(l);
                    editTextFields.setTag(inputFieldsObj.getString("title"));
                    txt.addView(editTextFields);
                    linearLayout.addView(txt);
                    textInput_fee_Amount.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getdetails(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(PayFees.this);
        instDetails = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    // TODO: 30-10-2017 get area
                    areaList.clear();
                    textInput_fee_Area.setVisibility(View.GONE);

                    JSONArray variantsArea = jsonObject.getJSONArray("variants");
                    textInput_fee_Area.setVisibility(View.VISIBLE);
                    for (int i = 0; i < variantsArea.length(); i++) {
                        areaObject = variantsArea.getJSONObject(i);
//                        InstituteDataModel instituteDataModel = new InstituteDataModel();
//                        instituteDataModel.setLocation(areaObject.getString("location"));
                        areaList.add(areaObject.getString("location"));
                        variantsCourse = areaObject.getJSONArray("variants");
                    }

                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(instDetails);
    }

    private void bindFeeInstitutes(int paymode) {
        InputStream is = null;
        try {
            if (paymode == 1) {
                is = getAssets().open("school_data2.json");

            } else if (paymode == 2) {
                is = getAssets().open("school_data.json");
            }
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
            instituteData = new ArrayList<Institute_Model>();
            JSONArray instituteArray = j.getJSONArray("InstituteList");
            for (int i = 0; i < instituteArray.length(); i++) {
                instituesObj = instituteArray.getJSONObject(i);
                Institute_Model newInstitute = new Institute_Model();
                newInstitute.setInstituteName(instituesObj.getString("name"));
                newInstitute.setInsttuteImage(instituesObj.getString("brand_image"));
                newInstitute.setUrl(instituesObj.getString("url"));
                instituteData.add(newInstitute);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDynamicViewData(ViewGroup parent) {

        if (textInput_fee_Course.getVisibility() == View.VISIBLE && pay_fee_Course.getText().length() == 0) {
            Toast.makeText(this, "Please Select A Course", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);


            if (child instanceof EditText) {
                EditText et = (EditText) child;

                if (et.getText().length() == 0) {
                    et.setError("Please Fill the Details Correctly");
                    fieldsNotEmpty = false;
                } else if ((et.getTag().toString().contains("Mobile") && !android.util.Patterns.PHONE.matcher(et.getText().toString()).matches())) {
                    et.setError("Please Enter Phone Number Correctly");
                    fieldsNotEmpty = false;
                } else if (et.getTag().toString().contains("Email") && !android.util.Patterns.EMAIL_ADDRESS.matcher(et.getText().toString()).matches()) {
                    et.setError("Please Fill the Email Address Correctly");
                    fieldsNotEmpty = false;
                } else {
                    String tags = et.getTag().toString().replaceAll(" ", "_");
                    userdetails.put(tags, et.getText().toString());
                }
            }

            if (child instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) child;
                getDynamicViewData(group);
            }
        }
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
            Toast.makeText(this, "Payment failed: " + i + " " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == FEE_LOCATION_CODE) {
                    String Location = data.getStringExtra("location");
                    fee_location.setText(Location);
                }
                if (requestCode == FEE_INSTITUTE_CODE) {
                    String Location = data.getStringExtra("institute");
                    String url = data.getStringExtra("instituteDetails");
                    fee_institute.setText(Location);
                    getdetails(url);
                    progressBar = (ProgressBar) findViewById(R.id.loadingData);
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (requestCode == FEE_AREA_CODE) {
                    String area = data.getStringExtra("selectedItemText");
                    int id = data.getIntExtra("selectedItem", 0);
                    fee_area.setText(area);
                    getcourses(id);
                }
                if (requestCode == FEE_COURSE_CODE) {
                    String area = data.getStringExtra("selectedItemText");
                    pay_fee_Course.setText(area);
                }

            }
        }
    }
}
