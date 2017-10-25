package sandhu.harman.singleproject.parent;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import sandhu.harman.singleproject.R;

public class PayFees extends AppCompatActivity implements View.OnClickListener {
    private static final int FEE_LOCATION_CODE = 562;
    EditText fee_location, fee_institute, fee_area, fee_paymentmode, fee_enrollment_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fees);
        fee_location = (EditText) findViewById(R.id.pay_fee_location);
        fee_institute = (EditText) findViewById(R.id.pay_fee_institute);
        fee_area = (EditText) findViewById(R.id.pay_fee_Area);
        fee_paymentmode = (EditText) findViewById(R.id.pay_fee_Payment);
        fee_enrollment_number = (EditText) findViewById(R.id.pay_fee_Enrollment_Number);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Rupee_Foradian.ttf");
        fee_location.setTypeface(typeface);
        fee_institute.setTypeface(typeface);
        fee_area.setTypeface(typeface);
        fee_paymentmode.setTypeface(typeface);
        fee_enrollment_number.setTypeface(typeface);

        fee_location.setOnClickListener(this);
        fee_institute.setOnClickListener(this);
        fee_area.setOnClickListener(this);
        fee_paymentmode.setOnClickListener(this);
        fee_enrollment_number.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_fee_location: {
                startActivityForResult(new Intent(PayFees.this, setFeeLocation.class), FEE_LOCATION_CODE);
                break;
            }
            case R.id.pay_fee_institute: {
                break;
            }
            case R.id.pay_fee_Area: {
                break;
            }
            case R.id.pay_fee_Payment: {
                break;
            }
            case R.id.pay_fee_Enrollment_Number: {
                break;
            }
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
            }
        }
    }
}
