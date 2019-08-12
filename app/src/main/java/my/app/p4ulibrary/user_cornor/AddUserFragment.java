package my.app.p4ulibrary.user_cornor;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import my.app.p4ulibrary.main_menu.HomeFragment;
import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.User;


public class AddUserFragment extends HomeFragment {
	private EditText etFullname,etPassword,etaPassword,etEmail,etMobile,etAddress,etIdentity;
	private Spinner spRole;
	private Button btnCreatenewuser;
	private ProgressBar progressBar;
	private View v;
	private FirebaseAuth auth;
	private String email,password,aPassword,id,name,role,mobilenumber,address,identity,status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//MainActivity.lp = 1;
	}

	@Override
	public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_add_user, container, false);
		//MainActivity.actionBar.show();
		Context context = container.getContext();
		initViews();
		//get Firebase auth instance
		auth = FirebaseAuth.getInstance();
		btnCreatenewuser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				registerUser();
			}
		});
		//DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
		return v;

	}
	private void get_values(){
		email = etEmail.getText().toString().toLowerCase().trim();
		password = etPassword.getText().toString().trim();
		aPassword=etaPassword.getText().toString().trim();
		id = null;
		name = etFullname.getText().toString().toUpperCase();
		role = spRole.getSelectedItem().toString();
		mobilenumber = etMobile.getText().toString();
		address=etAddress.getText().toString().toUpperCase();
		identity=etIdentity.getText().toString().toUpperCase();
		status="1";
	}
	private Boolean validate(){
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(getContext(), "Enter full name!", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(role)) {
			Toast.makeText(getContext(), "Enter role!", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(mobilenumber)&&mobilenumber.length()==10) {
			Toast.makeText(getContext(), "Enter Mobile Number!", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(email)) {
			Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(password.equals(aPassword)) {
			if (TextUtils.isEmpty(password)) {
				Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if (password.length() < 6) {
			Toast.makeText(getContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(address)) {
			Toast.makeText(getContext(), "Enter Address!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(identity)) {
			Toast.makeText(getContext(), "Enter aadhar or Voter id !", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	private void registerUser() {
		get_values();
		Boolean ans=validate();
		if(ans) {
			progressBar.setVisibility (View.VISIBLE);
			auth.createUserWithEmailAndPassword (email, password)
					.addOnCompleteListener (new OnCompleteListener<AuthResult> () {
						@RequiresApi(api = Build.VERSION_CODES.KITKAT)
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {

							if (task.isSuccessful ()) {

								User user = new User (
										id,
										name,
										email,
										role,
										mobilenumber,
										address,
										identity,
										status

								);

								FirebaseDatabase.getInstance ().getReference ("Users")
										.child ((Objects.requireNonNull (FirebaseAuth.getInstance ().getCurrentUser ())).getUid ())
										.setValue (user).addOnCompleteListener (new OnCompleteListener<Void> () {
									@Override
									public void onComplete(@NonNull Task<Void> task) {
										progressBar.setVisibility (View.GONE);
										if (task.isSuccessful ()) {
											Toast.makeText (getContext (), "Registration Success", Toast.LENGTH_LONG).show ();
										}  //display a failure message

									}
								});

							} else {
								Toast.makeText (getContext (), (Objects.requireNonNull (task.getException ())).getMessage (), Toast.LENGTH_LONG).show ();
							}
						}
					});

		}

	}
	private void initViews(){
		etFullname = (EditText)v. findViewById(R.id.et_fullname);
		etPassword = (EditText)v. findViewById(R.id.et_createpassword);
		etaPassword= (EditText)v. findViewById(R.id.et_confirmpassword);
		spRole = (Spinner) v.findViewById(R.id.sp_role);
		etEmail = (EditText) v.findViewById(R.id.et_createemail);
		etMobile = (EditText)v. findViewById(R.id.et_createmobile);
		etAddress=(EditText)v.findViewById(R.id.et_address);
		etIdentity=(EditText)v.findViewById(R.id.et_identity);
		btnCreatenewuser = (Button)v.findViewById(R.id.btn_createnewuser);
		progressBar = (ProgressBar)v.findViewById(R.id.progressbar_createuser);

	}

	@Override
	public void onResume() {
	//	MainActivity.lp = 1;
		super.onResume();
	}

	// switching fragment
	@SuppressWarnings("unused")
	private void switchFragment(Fragment fragment) {
		Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment)
				.addToBackStack("my_fragment").commit();
	}
}