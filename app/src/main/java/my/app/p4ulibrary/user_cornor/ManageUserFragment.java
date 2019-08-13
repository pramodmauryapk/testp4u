package my.app.p4ulibrary.user_cornor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.User;
import my.app.p4ulibrary.home_for_all.UserList;
import my.app.p4ulibrary.main_menu.HomeFragment;
import androidx.appcompat.app.AlertDialog.Builder;

import static java.util.Objects.requireNonNull;

public class ManageUserFragment extends HomeFragment {

    private ListView listViewUsers;
    private List<User> users;
    private View dialogView;
    private DatabaseReference databaseUsers;
    private EditText dtvUsername,dtvEmail, dtvMobile , dtvCenterName, dtvIdentity , dtvRole;
    private Button dbuttonBack,dbuttonDelete,dbuttonUpdate;
    private View v;
    private Builder builder;
    private String Name,Email,Mobile,location,Identity,Role,status;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate
                (R.layout.fragment_all_user, container, false);
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        //getting views

        initViews();

        users = new ArrayList<>();
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                showDeleteDialog(
                        user.getUserId (),
                        user.getUserName (),
                        user.getUserEmail (),
                        user.getUserRole (),
                        user.getUserMobile (),
                        user.getUserAddress (),
                        user.getUserIdentity ());
            }
        });

        return v;
    }
    @SuppressLint("InflateParams")
    private void showDeleteDialog(final String userId,
                                  final String userName,
                                  final String userEmail,
                                  final String userRole,
                                  final String userMobile,
                                  final String userAddress,
                                  final String userIdentity) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder (getContext ());
        LayoutInflater inflater = getLayoutInflater ();
        dialogView = inflater.inflate (R.layout.manage_user_info, null);
        dialogBuilder.setView (dialogView);

        init_dialog_views ();

        dialogBuilder.setTitle ("Manage User");
        final AlertDialog b = dialogBuilder.create ();
        b.show ();
        dtvUsername.setText (userName);
        dtvEmail.setText (userEmail);
        dtvMobile.setText (userMobile);
        dtvCenterName.setText (userAddress);
        dtvIdentity.setText (userIdentity);
        dtvRole.setText (userRole);
        dbuttonDelete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                builder = new Builder (Objects.requireNonNull(getContext()));
                builder.setMessage("Are you sure you want to Delete User?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               if(userId!=null){
                                   deleteUser(userEmail);
                               }
                               dialog.cancel ();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        });
                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();

            }
        });
        dbuttonBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });
        dbuttonUpdate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                get_values ();
                updatedetails (
                        userId,
                        Name,
                        Email,
                        Role,
                        Mobile,
                        location,
                        Identity);
            }
        });

    }
    private void deleteUser(String user_email) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child (user_email);
        dR.removeValue();
        Toast.makeText(getContext(), "User Account Deleted", Toast.LENGTH_LONG).show();

    }
    private void init_dialog_views(){

        dtvUsername = (EditText) dialogView.findViewById(R.id.etUserName);
        dtvEmail = (EditText) dialogView.findViewById(R.id.etEmailID);
        dtvMobile = (EditText) dialogView.findViewById(R.id.etMobile);
        dtvCenterName = (EditText) dialogView.findViewById(R.id.tvCenterName);
        dtvIdentity = (EditText) dialogView.findViewById(R.id.etIdentity);
        dtvRole = (EditText) dialogView.findViewById(R.id.tvRole);
        dbuttonBack = (Button) dialogView.findViewById(R.id.dbuttonBack);
        dbuttonDelete = (Button) dialogView.findViewById(R.id.dbuttonDelete);
        dbuttonUpdate = (Button) dialogView.findViewById(R.id.dbuttonUpdate);
    }
    private void initViews(){

        listViewUsers = (ListView) v.findViewById(R.id.view_list);


    }
    public void onStart() {

        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);

                        users.add (user);
                }
                UserList userAdapter = new UserList(getActivity(), users);
                listViewUsers.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void get_values(){
        Name=dtvUsername.getText ().toString ().trim ().toUpperCase ();
        Email=dtvEmail.getText ().toString ().trim ().toUpperCase ();
        Mobile=dtvMobile.getText ().toString ().trim ();
        location=dtvCenterName.getText ().toString ().trim ().toUpperCase ();
        Identity=dtvIdentity.getText ().toString ().trim ().toUpperCase ();
        Role=dtvRole.getText ().toString ().trim ().toUpperCase ();
    }

    private void updatedetails(String ID,
                               String Name,
                               String Email,
                               String roll,
                               String Mobile,
                               String address,
                               String Identity) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(ID);
    status="1";
        //updating artist
        ID=null;
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

    private void switchFragment(Fragment fragment) {

       Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("my_fragment").commit();
    }

}
