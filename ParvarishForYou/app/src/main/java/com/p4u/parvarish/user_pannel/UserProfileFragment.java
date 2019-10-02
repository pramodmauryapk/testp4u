package com.p4u.parvarish.user_pannel;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class UserProfileFragment extends Fragment {
    private static final String TAG = "UserProfileFragment";
    private String userID,userName,userEmail,userMobile,userIdentity,userPassword,userAgainPassword,userRole,userAddress,userStatus,userImg;
  private View v;
  private EditText username,email,mobile,identity,password,againpassword;
  private TextView location,role;
    private List<Teacher> users;
private Button save,change;
private LinearLayout l9;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate (R.layout.show_user, container, false);
        initViews ();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        //Declare database reference
        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Users");
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = (requireNonNull(user)).getUid();
        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Accessing database");
                show(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "failed to read values", databaseError.toException());
            }
        });
        save.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfileFragment.this.get_values();
                UserProfileFragment.this.updatedetails(userID,
                        userName,
                        userEmail,
                        userPassword,
                        userRole,
                        userMobile,
                        userAddress,
                        userIdentity,
                        userStatus,
                        userImg);
            }
        });

       change.setOnClickListener (new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               UserProfileFragment.this.switchFragment(new UpdatePasswordFragment());
           }
       });
        return v;
    }
    private void switchFragment(Fragment fragment) {

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }
    private void initViews(){
        username=v.findViewById(R.id.etUserName);
        email=v.findViewById(R.id.etEmailID);
        mobile=v.findViewById(R.id.etMobile);
        identity=v.findViewById(R.id.etIdentity);
        location=v.findViewById(R.id.tvCenterName);
        role=v.findViewById(R.id.tvRole);
        save=v.findViewById (R.id.dbuttonSave);
        change=v.findViewById (R.id.dbuttonchangepassword);

    }
    private void get_values(){
        userName=username.getText ().toString ().trim ().toUpperCase ();
        userEmail=email.getText ().toString ().trim ().toUpperCase ();
        userMobile=mobile.getText ().toString ().trim ();
        userIdentity=identity.getText ().toString ().trim ();
    }
    private void show(DataSnapshot dataSnapshot){
        try {
            for (DataSnapshot ds : dataSnapshot.getChildren ()) {


                   // Teacher uInfo = new Teacher ();
                    //uInfo.setUserRole (((requireNonNull (ds.child (userID).getValue (Teacher.class)))).getUserRole ()); //set the role
                    //uInfo.setUserName (((requireNonNull (ds.child (userID).getValue (Teacher.class)))).getUserName ()); //set the role
                   // uInfo.setUserEmail (((requireNonNull (ds.child (userID).getValue (Teacher.class)))).getUserEmail ()); //set the role
                   // uInfo.setUserMobile (((requireNonNull (ds.child (userID).getValue (Teacher.class)))).getUserMobile ()); //set the role
                   // uInfo.setUserAddress (((requireNonNull (ds.child (userID).getValue (Teacher.class)))).getUserAddress ()); //set the role
                   // uInfo.setUserIdentity (((requireNonNull (ds.child (userID).getValue (Teacher.class)))).getUserIdentity ()); //set the role
                   // uInfo.setImageURL (((requireNonNull (ds.child (userID).getValue (Teacher.class)))).getImageURL()); //set the role
                    Teacher uInfo=ds.getValue(Teacher.class);
                    username.setText (requireNonNull(uInfo).getUserName ());
                    email.setText (uInfo.getUserEmail ());
                    mobile.setText (uInfo.getUserMobile ());
                    location.setText (uInfo.getUserAddress ());
                    identity.setText (uInfo.getUserIdentity ());
                    role.setText (uInfo.getUserRole ());
                    userImg=uInfo.getImageURL();

            }
        }catch (Exception e){
            Log.d(TAG, "Failed to retrive values"+e);
        }
    }
    private void updatedetails(String ID,
                            String Name,
                            String Email,
                            String Password,
                            String Role,
                            String Mobile,
                            String Address,
                            String Identity,
                            String Status,
                               String Img) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("Users").child(ID);
        Name=username.getText ().toString ().trim ().toUpperCase ();
        Email=email.getText ().toString ().trim ().toLowerCase();
        Mobile=mobile.getText ().toString ().trim ();
        Identity=identity.getText ().toString ().trim ();
        Role=role.getText ().toString ().toUpperCase ();
        Address=location.getText ().toString ().toUpperCase ();
        Status="1";
        //updating artist
        Teacher user = new Teacher (
                ID,
                Name,
                Email,
                Password,
                Role,
                Mobile,
                Address,
                Identity,
                Status,
                Img);

        dR.setValue(user);
        Toast.makeText(getContext(), "User info Updated", Toast.LENGTH_LONG).show();
    }

}