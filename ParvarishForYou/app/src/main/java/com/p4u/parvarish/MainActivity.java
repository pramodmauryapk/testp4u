package com.p4u.parvarish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.p4u.parvarish.R;
import com.p4u.parvarish.contact_us.ContactUsFragment;
import com.p4u.parvarish.fancydialog.Animation;
import com.p4u.parvarish.fancydialog.FancyAlertDialog;
import com.p4u.parvarish.fancydialog.FancyAlertDialogListener;
import com.p4u.parvarish.fancydialog.Icon;
import com.p4u.parvarish.login.Login_emailActivity;
import com.p4u.parvarish.login.UserRegistrationActivity;
import com.p4u.parvarish.menu_items.AboutUsFragment;
import com.p4u.parvarish.menu_items.FeedbackAddFragment;
import com.p4u.parvarish.menu_items.HomeFragment;
import com.p4u.parvarish.menu_items.JoinUsFragment;
import com.p4u.parvarish.menu_items.OurWorkFragment;
import com.p4u.parvarish.user_pannel.UserProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "newgallary";
    private Fragment newContent;
    private Bundle bundle;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private String user_name,user_email,user_roll,user_img;
    private Intent intent;
    private TextView name,email;
    public CircleImageView img;
    public FirebaseUser user;
    public Stack<Fragment> fragmentStack;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //setting layout file
        setContentView(R.layout.activity_main);
        //auth instance
        mAuth=FirebaseAuth.getInstance();
        //setting toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setting drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //setting nevigation view
        navigationView = findViewById(R.id.nav_view);

        //initviews
        init();
        //intent object
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        user_email=intent.getStringExtra("user_email");
        user_roll=intent.getStringExtra("user_role");
        if(intent.getStringExtra("user_img")!=null) {
            user_img = intent.getStringExtra("user_img");
        }// user_img= getApplication().getResources().getDrawable(R.drawable.logo).toString();

        set_profile(user_name,user_email,user_roll,user_img);
        //settting floation action button
       // FloatingActionButton fab = findViewById(R.id.fab);

        //setting title
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        //adding menus in navigationview
        addMenuItemInNavMenuDrawer();
        //login default homefragment
        fragmentStack = new Stack<>();
        //transferring some fields to fragment
        Bundle bundle=new Bundle();
        bundle.putString("user_name",user_email);
        bundle.putString("user_role",user_roll);
        bundle.putString("user_name",user_name);
        bundle.putString("user_img",user_img);
        newContent = new HomeFragment();
        newContent.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.content_frame, newContent);
        fragmentStack.push(newContent);
        ft.commit();


/*        fab.setOnClickListener(view -> {
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //    .setAction("Action", null).show();
            switchFragment(new HomeFragment());
        });
*/

        //Access real time database

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void init(){
        // NavigationView Header
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.username);
        email = headerView.findViewById(R.id.email);
        img= headerView.findViewById(R.id.imageView);
    }
    @SuppressLint({"SetTextI18n", "ResourceType"})
    public void set_profile(String username,String useremail,String userrole,String imgURL){

          name.setText(username);
          email.setText(userrole);
          Picasso.get().load(imgURL).into(img);

    }
    private void addMenuItemInNavMenuDrawer() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.main_drawer);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            //super.onBackPressed();
            new FancyAlertDialog.Builder(this)
                    .setTitle("Rate us if you like the app")
                    .setMessage("Do you really want to Exit ?")
                    .setNegativeBtnText("Cancel")
                    .setPositiveBtnText("Close")
                    .setAnimation(Animation.POP)
                    .isCancellable(true)
                    .setIcon(R.drawable.logo, Icon.Visible)
                    .OnPositiveClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {

                            //Intent ActivityIndent = new Intent(newgallary.this, ExitActivity.class);
                            //startActivity(ActivityIndent);
                            finish();

                        }
                    })
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {



                        }
                    })
                    .build();
        } else {

            getSupportFragmentManager().popBackStack();
        }


    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.cornor_menu, menu);
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            menu.findItem(R.id.action_login_logout).setTitle("Login");
            menu.findItem(R.id.action_update).setTitle("Register");


        }
        else {
            menu.findItem(R.id.action_login_logout).setTitle("Logout");
            menu.findItem(R.id.action_update).setTitle("Update Profile");

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            if (id == R.id.action_update) {
                newContent = new UserProfileFragment();
                newContent.setArguments(bundle);
                switchFragment(newContent);
            }
            if (id == R.id.action_login_logout) {
                mAuth.signOut();
                item.setTitle("Login");
                user_name = "USER";
                user_email="EMAIL";
                user_roll="USER";
                // user_img=null;
                set_profile(user_name,user_email,user_roll,user_img);




            }

        }else{
            if (id == R.id.action_update) {
                startActivity(new Intent(this, UserRegistrationActivity.class));
                finish();
            }
            if (id == R.id.action_login_logout) {
                startActivity(new Intent(this, Login_emailActivity.class));
                finish();
            }



        }


        return super.onOptionsItemSelected(item);
    }
    // Handle navigation view item clicks here.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        bundle = new Bundle();

        switch (id){

            case R.id.nav_our_work:newContent=new OurWorkFragment();
                break;
            case R.id.nav_about_us:newContent=new AboutUsFragment();
                break;
            case R.id.nav_feedback:newContent=new FeedbackAddFragment();
                break;
            case R.id.nav_contact_us:newContent=new ContactUsFragment();
                break;
            case R.id.nav_joinus:
                Bundle bundle=new Bundle();
                bundle.putString("user_name",user_email);
                bundle.putString("user_role",user_roll);
                bundle.putString("user_name",user_name);
                bundle.putString("user_img",user_img);
                newContent=new JoinUsFragment();
                newContent.setArguments(bundle);

                break;

            case R.id.nav_share:
                shareApp();
                break;
            case R.id.nav_exit:new FancyAlertDialog.Builder(this)
                    .setTitle("Rate us if you like the app")
                    .setMessage("Do you really want to Exit ?")
                    .setNegativeBtnText("Cancel")
                    .setPositiveBtnText("Close")
                    .setAnimation(Animation.POP)
                    .isCancellable(true)
                    .setIcon(R.drawable.logo, Icon.Visible)
                    .OnPositiveClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {

                            //Intent ActivityIndent = new Intent(newgallary.this, ExitActivity.class);
                            //startActivity(ActivityIndent);
                            finish();

                        }
                    })
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {



                        }
                    })
                    .build();

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

    public void switchFragment(Fragment fragment) {
      FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentStack.push(fragment);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();

     }
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        String shareSubText = "Kindly install our android app Parvarish4U: From ParvarishForYou Team ";
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareSubText + "https://play.google.com/store/apps/details?id=com.p4u.parvarish&hl=en");
        startActivity(Intent.createChooser(shareIntent,"Share link using"));
    }





}