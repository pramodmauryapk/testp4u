package com.p4u.parvarish.user_pannel;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.p4u.parvarish.R;
import com.p4u.parvarish.main_menu.HomeFragment;

import static java.util.Objects.requireNonNull;

public class ResetPasswordFragment extends Fragment {

    private static final String TAG = "Reset Activity";
    private EditText inputEmail;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnReset,btnBack;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_reset_password,container,false);
        initViews();



        auth = FirebaseAuth.getInstance();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment fragment = new HomeFragment();
                ResetPasswordFragment.this.switchFragment(fragment);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().toLowerCase().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ResetPasswordFragment.this.getContext(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "start progress bar");
                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordFragment.this.getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    // startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                    //  finish();
                                } else {
                                    Toast.makeText(ResetPasswordFragment.this.getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
                Log.d(TAG, "Progress bar visible");
            }
        });


        return v;
    }
    private void initViews(){
        inputEmail = v.findViewById(R.id.et_email);
        btnReset = v. findViewById(R.id.btn_resetpassword);
        btnBack = v.findViewById(R.id.btn_back);
        progressBar = v.findViewById(R.id.progressBar_reset);
    }
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("my_frame").commit();

    }

}
