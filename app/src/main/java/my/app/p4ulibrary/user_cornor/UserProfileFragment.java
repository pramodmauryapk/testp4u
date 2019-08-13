package my.app.p4ulibrary.user_cornor;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import my.app.p4ulibrary.classes.Book;
import my.app.p4ulibrary.classes.User;
import my.app.p4ulibrary.main_menu.HomeFragment;
import my.app.p4ulibrary.R;

import static java.util.Objects.requireNonNull;

public class UserProfileFragment extends HomeFragment {
    private static final String TAG = "UserProfileActivity";
   ;
    private String userID,userName,userEmail,userMobile,userIdentity,userPassword,userAgainPassword,roll,address,status;
  private View v;
  private EditText username,email,mobile,identity,password,againpassword;
  private TextView location,role;
    private List<User> users;
private Button save,change;
LinearLayout l9;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate (R.layout.show_user, container, false);
        initViews ();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        //Declare database reference
        DatabaseReference myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
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
        save.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                get_values ();
                updatedetails (userID,
                        userName,
                        userEmail,
                        roll,
                        userMobile,
                        address,
                        userIdentity,
                        status);
            }
        });

       change.setOnClickListener (new View.OnClickListener () {
           @Override
           public void onClick(View view) {

                switchFragment (new UpdatePasswordFragment ());
           }
       });
        return v;
    }
    private void switchFragment(Fragment fragment) {

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("my_fragment").commit();
    }
    private void initViews(){
        username=(EditText)v.findViewById(R.id.etUserName);
        email=(EditText)v.findViewById(R.id.etEmailID);
        mobile=(EditText)v.findViewById(R.id.etMobile);
        identity=(EditText)v.findViewById(R.id.etIdentity);
        location=(TextView)v.findViewById(R.id.tvCenterName);
        role=(TextView)v.findViewById(R.id.tvRole);
        save=(Button)v.findViewById (R.id.dbuttonSave);
        change=(Button)v.findViewById (R.id.dbuttonchangepassword);

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


                    User uInfo = new User ();
                    uInfo.setUserRole (((requireNonNull (ds.child (userID).getValue (User.class)))).getUserRole ()); //set the role
                    uInfo.setUserName (((requireNonNull (ds.child (userID).getValue (User.class)))).getUserName ()); //set the role
                    uInfo.setUserEmail (((requireNonNull (ds.child (userID).getValue (User.class)))).getUserEmail ()); //set the role
                    uInfo.setUserMobile (((requireNonNull (ds.child (userID).getValue (User.class)))).getUserMobile ()); //set the role
                    uInfo.setUserAddress (((requireNonNull (ds.child (userID).getValue (User.class)))).getUserAddress ()); //set the role
                    uInfo.setUserIdentity (((requireNonNull (ds.child (userID).getValue (User.class)))).getUserIdentity ()); //set the role
                    username.setText (uInfo.getUserName ());
                    email.setText (uInfo.getUserEmail ());
                    mobile.setText (uInfo.getUserMobile ());
                    location.setText (uInfo.getUserAddress ());
                    identity.setText (uInfo.getUserIdentity ());
                    role.setText (uInfo.getUserRole ());

            }
        }catch (Exception e){
            Log.d(TAG, "Failed to retrive values"+e);
        }finally {

        }
    }
    private void updatedetails(String ID,
                            String Name,
                            String Email,
                            String roll,
                            String Mobile,
                            String address,
                            String Identity,
                            String status) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(ID);
        Name=username.getText ().toString ().trim ().toUpperCase ();
        Email=email.getText ().toString ().trim ().toUpperCase ();
        Mobile=mobile.getText ().toString ().trim ();
        ID=null;
        Identity=identity.getText ().toString ().trim ();
        roll=role.getText ().toString ().toUpperCase ();
        address=location.getText ().toString ().toUpperCase ();
        status="1";
        //updating artist
        User user = new User (
                ID,
                Name,
                Email,
                roll,
                Mobile,
                address,
                Identity,
                status);

        dR.setValue(user);
        Toast.makeText(getContext(), "User info Updated", Toast.LENGTH_LONG).show();
    }

}