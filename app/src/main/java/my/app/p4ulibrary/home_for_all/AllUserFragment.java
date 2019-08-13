package my.app.p4ulibrary.home_for_all;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import my.app.p4ulibrary.main_menu.HomeFragment;
import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.User;


public class AllUserFragment extends HomeFragment {
    public int openedLine=0;
    private ListView listViewUsers;
    private List<User> users;
    private View dialogView;
    private TextView dtvUsername,dtvEmail, dtvMobile , dtvCenterName, dtvIdentity , dtvRole;
    private Button dbuttonBack,dbuttonDelete;
    private DatabaseReference databaseUsers;
    private androidx.appcompat.app.AlertDialog.Builder builder;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate
                (R.layout.fragment_all_user, container, false);
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        openedLine=1;
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
                    if(Objects.requireNonNull (user).getUserRole().equals("USER")) {
                        users.add(user);
                    }

                }
         UserList userAdapter = new UserList(getActivity(), users);
                listViewUsers.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        dialogBuilder.setTitle ("User Details");
        final AlertDialog b = dialogBuilder.create ();
        b.show ();
        dtvUsername.setText (userName);
        dtvEmail.setText (userEmail);
        dtvMobile.setText (userMobile);
        dtvCenterName.setText (userAddress);
        dtvIdentity.setText (userIdentity);
        dtvRole.setText (userRole);
        dbuttonDelete.setVisibility (View.GONE);
        dbuttonBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });
    }
    private void init_dialog_views(){

        dtvUsername = (TextView) dialogView.findViewById(R.id.etUserName);
        dtvEmail = (TextView) dialogView.findViewById(R.id.etEmailID);
        dtvMobile = (TextView) dialogView.findViewById(R.id.etMobile);
        dtvCenterName = (TextView) dialogView.findViewById(R.id.tvCenterName);
        dtvIdentity = (TextView) dialogView.findViewById(R.id.etIdentity);
        dtvRole = (TextView) dialogView.findViewById(R.id.tvRole);
        dbuttonBack = (Button) dialogView.findViewById(R.id.dbuttonBack);
        dbuttonDelete = (Button) dialogView.findViewById(R.id.dbuttonDelete);
    }
    @Override
    public boolean onBackPressed() {
        return openedLine < 0;
    }

}
