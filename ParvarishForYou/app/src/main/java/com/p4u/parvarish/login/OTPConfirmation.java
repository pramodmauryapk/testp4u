package com.p4u.parvarish.login;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class OTPConfirmation extends AppCompatActivity implements View.OnClickListener {

    private static final String KEY_VERIFY_IN_PROGRESS="key_verify_in_progress";

    EditText otp1, otp2,otp3,otp4,otp5,otp6;
    Button verify;
    ImageButton edit;
    TextView mobile;
    TextView invalidotp;

    String sMob;
    CardView verifyCard;
    TextView resendOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpconfirmation);
        (requireNonNull(getSupportActionBar())).hide();
        if (getIntent().getExtras()!=null){
            sMob = getIntent().getExtras().getString("mobile");
        }

        mobile = findViewById(R.id.otp_mobile);
        verify = findViewById(R.id.verify_otp);
        resendOTP = findViewById(R.id.resend_otp);

        otp1 = findViewById(R.id.otp_pin_1);
        otp2 = findViewById(R.id.otp_pin_2);
        otp3 = findViewById(R.id.otp_pin_3);
        otp4 = findViewById(R.id.otp_pin_4);
        otp5 = findViewById(R.id.otp_pin_5);
        otp6 = findViewById(R.id.otp_pin_6);

        edit = findViewById(R.id.edit_mobile);
        invalidotp = findViewById(R.id.invalid_otp);

        verifyCard = findViewById(R.id.verify_card);

        mobile.setText(sMob);


        otp1.addTextChangedListener(new EditTextWatcher(otp1));
        otp2.addTextChangedListener(new EditTextWatcher(otp2));
        otp3.addTextChangedListener(new EditTextWatcher(otp3));
        otp4.addTextChangedListener(new EditTextWatcher(otp4));
        otp5.addTextChangedListener(new EditTextWatcher(otp5));
        otp6.addTextChangedListener(new EditTextWatcher(otp6));

        verify.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.edit_mobile:
                finish();
                break;
            case R.id.verify_otp:
                Toast.makeText(this,"Verify Clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.resend_otp:
                Toast.makeText(this,"Resend Code Clicked",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public class EditTextWatcher implements TextWatcher {

        View view;
        EditTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            invalidotp.setVisibility(View.GONE);

            String text = editable.toString();
            switch (view.getId()){
                case R.id.otp_pin_1:
                    if (text.length()==1){
                        otp2.requestFocus();
                    }
                    break;
                case R.id.otp_pin_2:
                    if (text.length()==1){
                        otp3.requestFocus();
                    }else{
                        otp1.requestFocus();
                    }
                    break;
                case R.id.otp_pin_3:
                    if (text.length()==1){
                        otp4.requestFocus();
                    }else{
                        otp2.requestFocus();
                    }
                    break;
                case R.id.otp_pin_4:
                    if (text.length()==1){
                        otp5.requestFocus();
                    }else{
                        otp3.requestFocus();
                    }
                    break;
                case R.id.otp_pin_5:
                    if (text.length()==1){
                        otp6.requestFocus();
                    }else{
                        otp4.requestFocus();
                    }
                    break;
                case R.id.otp_pin_6:
                    if (text.length()!=1){
                        otp5.requestFocus();
                    }
                    break;
                default:break;
            }

            if (getOTP()!=null && getOTP().length()==6){
                verify.setEnabled(true);
                verifyCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                verify.performClick();
            }else{
                verify.setEnabled(false);
                verifyCard.setCardBackgroundColor(getResources().getColor(R.color.grey1));
            }

        }
    }
    public String getOTP(){
        String otp;
        otp = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
        return otp;
    }
}
