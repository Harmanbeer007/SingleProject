package sandhu.harman.singleproject.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import sandhu.harman.singleproject.R;

public class PayFees extends AppCompatActivity implements View.OnClickListener {
    private static final int FEE_LOCATION_CODE = 562;
    private static final int FEE_INSTITUTE_CODE = 563;
    private static final int FEE_AREA_CODE = 564;
    private static final int FEE_COURSE_CODE = 565;
    private EditText fee_location, fee_institute, fee_area, pay_fee_Course;
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
    private ProgressBar pd;
    private RadioButton newRegitration;
    private EditText editTextFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fees);
        fee_location = (EditText) findViewById(R.id.pay_fee_location);
        fee_institute = (EditText) findViewById(R.id.pay_fee_institute);
        pay_fee_Course = (EditText) findViewById(R.id.pay_fee_Course);

        linearLayout = (LinearLayout) findViewById(R.id.pay_fee_institute_Layout);
        textInput_fee_Area = (TextInputLayout) findViewById(R.id.textInput_fee_Area);
        textInput_fee_Course = (TextInputLayout) findViewById(R.id.textInput_fee_Course);
        textInput_fee_Amount = (TextInputLayout) findViewById(R.id.textInput_fee_Amount);
        final RadioButton feePayment = (RadioButton) findViewById(R.id.FeePaymentRadio);


        feeType = (RadioGroup) findViewById(R.id.feeTypeGroup);
        newRegitration = (RadioButton) findViewById(R.id.newRegistrationPaymentRadio);
        getData(1);
        feeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selected = radioGroup.getCheckedRadioButtonId();
                clearData();
                if (selected == newRegitration.getId()) {
                    getData(1);
                } else if (selected == feePayment.getId()) {
                    getData(2);
                }
            }
        });
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
//                startActivity(new Intent(PayFees.this, DebitCard.class));
                final int childCount = linearLayout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    Toast.makeText(this, editTextFields.getText(), Toast.LENGTH_SHORT).show();

                }
                break;
            }
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
                    pd = new ProgressBar(PayFees.this);
                    pd.setVisibility(View.VISIBLE);
                }
                if (requestCode == FEE_AREA_CODE) {
                    String area = data.getStringExtra("selectedItemText");
                    int id = data.getIntExtra("selectedItem", 0);
                    fee_area.setText(area);
                    getcourses(id);
                }
                if (requestCode == FEE_COURSE_CODE) {
                    String area = data.getStringExtra("selectedItemText");
                    int id = data.getIntExtra("selectedItem", 0);
                    pay_fee_Course.setText(area);
//                    getcourses(id);
                }

            }
        }
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
                for (int l = 0; l < inputFields.length(); l++) {
                    inputFieldsObj = inputFields.getJSONObject(l);
                    variantsCourseFieldsList.add(inputFieldsObj.getString("title"));
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
                        InstituteDataModel instituteDataModel = new InstituteDataModel();
                        instituteDataModel.setLocation(areaObject.getString("location"));
                        areaList.add(areaObject.getString("location"));
                        variantsCourse = areaObject.getJSONArray("variants");
                    }

                    pd.setVisibility(View.GONE);

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

    private void getData(int paymode) {
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

    private void loopQuestions(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);


            if (child instanceof EditText) {
                //Support for EditText
                EditText et = (EditText) child;
                Log.w("ANDROID DYNAMIC VIEWS:", "EdiText: " + et.getText());
            }

            if (child instanceof ViewGroup) {
                //Nested Q&A
                ViewGroup group = (ViewGroup) child;
                loopQuestions(group);
            }
        }
    }



}
