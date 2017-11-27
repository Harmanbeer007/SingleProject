package sandhu.harman.singleproject.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import sandhu.harman.singleproject.R;

/**
 * Created by harman on 23-11-2017.
 */

public class setAddress extends AppCompatActivity {
    private boolean fieldsNotEmpty;
    private LinearLayout lay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_address);
        lay = (LinearLayout) findViewById(R.id.adress_Lay);
        getDynamicViewData(lay);

    }

    private void getDynamicViewData(ViewGroup parent) {

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
                }
            }

            if (child instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) child;
                getDynamicViewData(group);
            }
        }
    }
}