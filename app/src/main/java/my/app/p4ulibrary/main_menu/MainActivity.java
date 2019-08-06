package my.app.p4ulibrary.main_menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import my.app.p4ulibrary.LoginActivity;
import my.app.p4ulibrary.R;
import my.app.p4ulibrary.book_cornor.AddBookFragment;
import my.app.p4ulibrary.book_cornor.IssueBookFragment;
import my.app.p4ulibrary.book_cornor.SearchBookFragment;
import my.app.p4ulibrary.book_cornor.SubmitBookFragment;
import my.app.p4ulibrary.book_cornor.UpdateBookFragment;
import my.app.p4ulibrary.home_for_all.FeedbackAddFragment;
import my.app.p4ulibrary.home_for_all.NewsAddFragment;
import my.app.p4ulibrary.user_cornor.AddUserFragment;
import my.app.p4ulibrary.user_cornor.ManageUserFragment;
import my.app.p4ulibrary.user_cornor.UserProfileFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    TextView username,email;
    View HeaderView;
    Context context;
    Fragment newContent;
    Bundle bundle;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private String userID;
    private DatabaseReference myRef;
    NavigationView navigationView;
    public String user_name,user_email,user_roll;
    public Intent intent;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        get_values_from_intent();

        setContentView(R.layout.activity_main);
        //Access real time database
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull (getSupportActionBar ()).setTitle("P4U Library");
        FloatingActionButton fab = findViewById(R.id.fab);
        context = this;
        newContent=new HomeFragment ();
            newContent.setArguments(bundle);
            switchFragment(newContent);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                switchFragment (new HomeFragment ());
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        addMenuItemInNavMenuDrawer ();


    // NavigationView Header
        HeaderView =  navigationView.getHeaderView(0);
        username=(TextView)HeaderView.findViewById(R.id.username);
        email=(TextView)HeaderView.findViewById (R.id.email);
        username.setText("Welcome! "+user_name);
        email.setText(user_email);

        //Access real time database
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null) {
            userID = ((Objects.requireNonNull (user))).getUid ();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }
    private void get_values_from_intent(){
        intent = getIntent ();
        if(intent!=null) {

            user_name = intent.getStringExtra ("user_name");
            user_email = intent.getStringExtra ("user_email");
            user_roll = intent.getStringExtra ("user_role");
        }

    }
    private void addMenuItemInNavMenuDrawer() {
        navigationView.getMenu().clear();
        if(Objects.requireNonNull (user_roll).equals ("ADMIN")){

            navigationView.inflateMenu(R.menu.main_drawer);
        }
        else if(user_roll.equals ("HEAD")){

            navigationView.inflateMenu(R.menu.head_drawer);
        }
        else{

            navigationView.inflateMenu(R.menu.user_drawer);
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            List fragmentList = getSupportFragmentManager().getFragments();
            boolean handled = false;
            for(Object f : fragmentList) {
                if(f instanceof HomeFragment) {
                    handled = ((HomeFragment)f).onBackPressed();
                    if(handled) {


                        break;

                    }
                }
            }

            if(!handled) {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.cornor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       if (id == R.id.action_update) {
           newContent=new UserProfileFragment();
           newContent.setArguments(bundle);
           switchFragment(newContent);
           return true;
        }
        if (id == R.id.action_logout) {

            mAuth.signOut();
            startActivity(new Intent(context, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        bundle = new Bundle();
        switch (id){
            case R.id.nav_home:newContent=new HomeFragment();
                break;
            case R.id.nav_add_book:newContent=new AddBookFragment();
                break;
            case R.id.nav_update_book:newContent=new UpdateBookFragment ();
                break;
            case R.id.nav_search_book:newContent=new SearchBookFragment ();
                break;
            case R.id.nav_issue_book:newContent=new IssueBookFragment ();
                break;
            case R.id.nav_submit_book:newContent=new SubmitBookFragment ();
                break;
            case R.id.nav_add_user:newContent=new AddUserFragment ();
                break;

            case R.id.nav_manage_user:newContent=new ManageUserFragment ();
                break;

            case R.id.nav_manage_news:newContent=new NewsAddFragment ();
                break;
            case R.id.nav_feedback:newContent=new FeedbackAddFragment ();
                break;

        }

        if (newContent != null) {
            newContent.setArguments(bundle);
            switchFragment(newContent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("my_fragment").commit();
    }


}
