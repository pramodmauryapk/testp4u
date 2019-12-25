package com.p4u.parvarish.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class MobileVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="mobilverify" ;
    private EditText otp1, otp2,otp3,otp4,otp5,otp6;
    private Button verify;
    private ImageButton edit;
    private TextView invalidotp;
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private String phonenumber,code;
    private String user_name,user_email,user_roll,user_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_verify_phone);
        (requireNonNull (getSupportActionBar ())).hide ();
        mAuth = FirebaseAuth.getInstance();
        init();

            if (getIntent().getExtras()!=null){
                phonenumber = getIntent().getStringExtra("phonenumber");
            }
            sendVerificationCode(phonenumber);

        otp1.addTextChangedListener(new EditTextWatcher(otp1));
        otp2.addTextChangedListener(new EditTextWatcher(otp2));
        otp3.addTextChangedListener(new EditTextWatcher(otp3));
        otp4.addTextChangedListener(new EditTextWatcher(otp4));
        otp5.addTextChangedListener(new EditTextWatcher(otp5));
        otp6.addTextChangedListener(new EditTextWatcher(otp6));
        verify.setEnabled(false);
        verify.setOnClickListener(this);
        edit.setOnClickListener(this);

    }

    private void init() {
        otp1 = findViewById(R.id.otp_pin_1);
        otp2 = findViewById(R.id.otp_pin_2);
        otp3 = findViewById(R.id.otp_pin_3);
        otp4 = findViewById(R.id.otp_pin_4);
        otp5 = findViewById(R.id.otp_pin_5);
        otp6 = findViewById(R.id.otp_pin_6);
        TextView mobile = findViewById(R.id.otp_mobile);
        verify = findViewById(R.id.buttonSignIn);
        TextView resendOTP = findViewById(R.id.resend_otp);
        edit = findViewById(R.id.edit_mobile);
        invalidotp = findViewById(R.id.invalid_otp);
        progressBar = findViewById(R.id.progressbar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_mobile:
                finish();
                break;
            case R.id.buttonSignIn:
                //Toast.makeText(this,"Verify Clicked"+getOTP(),Toast.LENGTH_SHORT).show();
                verifyVerificationCode(getOTP());
                break;
            case R.id.resend_otp:
                sendVerificationCode(phonenumber);
                Toast.makeText(this,"Code sent",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        //signing the user
        signInWithPhoneAuthCredential(credential);
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
                        verify.requestFocus();
                    }
                    break;
                default:break;
            }

            if (getOTP()!=null && getOTP().length()==6){
                verify.setEnabled(true);
                verify.performClick();
            }else{
                verify.setEnabled(false);

            }

        }
    }
    public String getOTP(){
        String otp;
        otp = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
        return otp;
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MobileVerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("USERS");
                            myref.addValueEventListener(new ValueEventListener() {

                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        Log.d(TAG,"starting user registation");
                                        Intent intent = new Intent(MobileVerifyActivity.this, UserRegistrationMobileActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("mobile", phonenumber);
                                        startActivity(intent);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                     Log.d(TAG, "failed to read values", databaseError.toException());
                                }
                            });

                        } else {

                            //verification unsuccessful.. display an error message
                            String message = "Somthing is wrong, we will fix it soon...";
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Welcome Back!";
                            }

                            Toast.makeText(MobileVerifyActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,           // Phone number to verify
                120,            // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this,//TaskExecutors.MAIN_THREAD// Activity (for callback binding)
                mCallback
        );

    }
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            try{
                //Getting the code sent by SMS
                code = phoneAuthCredential.getSmsCode();
                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    //editTextCode.setText(code);
                    filltext(code);

                }
            }catch (Exception e){
                Toast.makeText(MobileVerifyActivity.this, "Code Not received", Toast.LENGTH_LONG).show();
            }finally {
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MobileVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //storing the verification id that is sent to the user
            verificationId = s;
        }
    };
    private void filltext(String code) {

        int number=Integer.parseInt(code);
        int r;
        r=number%10;
        number=number/10;
        otp6.setText(String.valueOf(r));
        r=number%10;
        number=number/10;
        otp5.setText(String.valueOf(r));
        r=number%10;
        number=number/10;
        otp4.setText(String.valueOf(r));
        r=number%10;
        number=number/10;
        otp3.setText(String.valueOf(r));
        r=number%10;
        number=number/10;
        otp2.setText(String.valueOf(r));
        r=number%10;
        otp1.setText(String.valueOf(r));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getting_role(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Teacher uInfo=ds.getValue(Teacher.class);
            if(requireNonNull(uInfo).getUserId().equals(requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                user_name = requireNonNull(uInfo).getUserName();
                user_email = uInfo.getUserEmail();
                user_roll = uInfo.getUserRole();
                user_img = uInfo.getImageURL();


            }

        }

    }
}
