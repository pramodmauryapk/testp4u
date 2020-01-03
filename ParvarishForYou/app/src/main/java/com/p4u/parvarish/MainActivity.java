package com.p4u.parvarish;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.HelpLine.EmergencyFragment;
import com.p4u.parvarish.contact_us.ContactUsFragment;
import com.p4u.parvarish.fancydialog.Animation;
import com.p4u.parvarish.fancydialog.FancyAlertDialog;
import com.p4u.parvarish.fancydialog.FancyAlertDialogListener;
import com.p4u.parvarish.fancydialog.Icon;
import com.p4u.parvarish.login.Login_emailActivity;
import com.p4u.parvarish.login.UserRegistrationActivity;
import com.p4u.parvarish.menu_items.AboutUsFragment;
import com.p4u.parvarish.menu_items.FeedbackAddFragment;
import com.p4u.parvarish.menu_items.JoinUsFragment;
import com.p4u.parvarish.menu_items.OurWorkFragment;
import com.p4u.parvarish.user_pannel.Teacher;
import com.p4u.parvarish.user_pannel.UserProfileFragment;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Objects.requireNonNull;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Fragment newContent;
    private Bundle bundle;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private String user_name, user_email, user_roll, user_img, user_relative;
    private Intent intent;
    public String callmobile;
    private TextView name, email;
    public CircleImageView img;
    public FirebaseUser user;
    private DatabaseReference myref;
    public Stack<Fragment> fragmentStack;
    SettingsContentObserver settingsContentObserver;
    private LocationManager locManager;
    private Location lastLocation;
    private final static int PERMISSION_REQUEST = 1;
    private final static int MY_PERMISSIONS_REQUEST_SEND_SMS=2;
    private String location_data=null;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final LocationListener locListener = new LocationListener() {
        public void onLocationChanged(Location loc) {
            updateLocation(loc);
        }

        public void onProviderEnabled(String provider) {
            updateLocation();
        }

        public void onProviderDisabled(String provider) {

            updateLocation();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    public MainActivity(){

    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting layout file
        setContentView(R.layout.activity_main);
        settingsContentObserver = new SettingsContentObserver(this, new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, settingsContentObserver);
        //auth instance
        mAuth = FirebaseAuth.getInstance();
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
        load_intents();
        //settting floation action button
        final FloatingActionButton fab = findViewById(R.id.fab);
        //setting title
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        //adding menus in navigationview
        addMenuItemInNavMenuDrawer();
        //login default homefragment
        load_home();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        fab.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null) {
                    if (get_mobile() != null) {
                        call(get_mobile());
                        if (location_data != null) {

                            sendSMS(get_mobile(), location_data);
                        } else {
                            sendSMS(get_mobile(), "Your Relative is in Emergency");
                        }

                    } else {
                        Bundle bundle = new Bundle();
                        newContent = new EmergencyFragment();
                        newContent.setArguments(bundle);
                        switchFragment(newContent);

                    }
                }else{
                    Toast.makeText(MainActivity.this,"Please Register to get Emergency Contact Service",Toast.LENGTH_LONG).show();
                }
                    //Snackbar.make(view, "Kindly Add Number", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void load_intents() {
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        user_email = intent.getStringExtra("user_email");
        user_roll = intent.getStringExtra("user_role");
        user_relative = get_mobile();

        if (intent.getStringExtra("user_img") != null) {
            user_img = intent.getStringExtra("user_img");

        }
        set_profile(user_name, user_roll, user_img, user_relative);
    }
    private void load_home() {
        fragmentStack = new Stack<>();
        //transferring some fields to fragment
        Bundle bundle = new Bundle();
        bundle.putString("user_name", user_email);
        bundle.putString("user_role", user_roll);
        bundle.putString("user_name", user_name);
        bundle.putString("user_img", user_img);
        newContent = new HomeFragment();
        newContent.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.content_frame, newContent);
        fragmentStack.push(newContent);
        ft.commit();
    }
    private String get_mobile() {
        myref= FirebaseDatabase.getInstance().getReference().child("USERS");

        user = FirebaseAuth.getInstance().getCurrentUser();
            myref.addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "Accessing database");

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Teacher uInfo = ds.getValue(Teacher.class);
                        if (requireNonNull(uInfo).getUserId().equals(user.getUid())) {
                           user_relative = uInfo.getUserRelative();

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "failed to read values", databaseError.toException());
                }
            });

        return user_relative;
    }
    public class SettingsContentObserver extends ContentObserver {
        int previousVolume;
        Context context;

        SettingsContentObserver(Context c, Handler handler) {
            super(handler);
            context = c;
            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            previousVolume =
                    Objects.requireNonNull(audio).getStreamVolume(AudioManager.STREAM_MUSIC);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int currentVolume =
                    Objects.requireNonNull(audio).getStreamVolume(AudioManager.STREAM_MUSIC);
            int delta = previousVolume - currentVolume;
            //  if(delta<0) {
            //for activate google assistant
            // startActivity(new Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            // }
            if (delta > 0) {
                // Toast.makeText(MainActivity.this, "Volume Decreased", Toast.LENGTH_SHORT).show();
                previousVolume = currentVolume;

            } else if (delta < 0) {

                //Toast.makeText(MainActivity.this, "Volume Increased", Toast.LENGTH_SHORT).show();
                previousVolume = currentVolume;

            }
        }
    }
    public void senemail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"youremail@yahoo.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "subject");
        email.putExtra(Intent.EXTRA_TEXT, "message");
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
    private void call(final String mobile) {

        final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

        // Write your code here to execute after dialog
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobile));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this),
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            try {
                startActivity(callIntent);

            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onDestroy() {
        getApplicationContext().getContentResolver().unregisterContentObserver(settingsContentObserver);
        super.onDestroy();
    }
    private void init() {
        // NavigationView Header
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.username);
        email = headerView.findViewById(R.id.email);
        img = headerView.findViewById(R.id.imageView);

    }
    @Override
    protected void onResume() {
        super.onResume();
        startRequestingLocation();
        updateLocation();

    }
    public void updateLocation() {
        // Trigger a UI update without changing the location
        updateLocation(lastLocation);
    }
    public void startRequestingLocation() {
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this,"Please Enable Your GPS",Toast.LENGTH_LONG).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
            return;
        }

        // GPS enabled and have permission - start requesting location updates
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
    }
    private void updateLocation(Location location) {
        boolean locationEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean waitingForLocation = locationEnabled && !validLocation(location);
        boolean haveLocation = locationEnabled && !waitingForLocation;
/*
       // Update display area
        gpsButton.setVisibility(locationEnabled ? View.GONE : View.VISIBLE);
        progressTitle.setVisibility(waitingForLocation ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(waitingForLocation ? View.VISIBLE : View.GONE);
        detailsText.setVisibility(haveLocation ? View.VISIBLE : View.GONE);

        // Update buttons
        shareButton.setEnabled(haveLocation);
        copyButton.setEnabled(haveLocation);
        viewButton.setEnabled(haveLocation);
*/
        if (haveLocation) {
            //String newline = System.getProperty("line.separator");
          /*  detailsText.setText(String.format("%s: %s%s%s: %s (%s)%s%s: %s (%s)",
                    getString(R.string.accuracy), getAccuracy(location), newline,
                    getString(R.string.latitude), getLatitude(location), getDMSLatitude(location), newline,
                  getString(R.string.longitude), getLongitude(location), getDMSLongitude(location)));

            location_data= String.format("%s: %s%s%s: %s (%s)%s%s: %s (%s)",
                    getString(R.string.accuracy), getAccuracy(location), newline,
                    getString(R.string.latitude), getLatitude(location), getDMSLatitude(location), newline,
                    getString(R.string.longitude), getLongitude(location), getDMSLongitude(location));*/
          //  Toast.makeText(this,location_data,Toast.LENGTH_LONG).show();

            location_data=String.format("I am in trouble at this location.I need your urgent help.मैं इस स्थान पर मुसीबत में हूं। मुझे आपकी तत्काल मदद चाहिए:"+"http://maps.google.com/?q=%s,%s",getLatitude(location),getLongitude(location));
            lastLocation = location;
        }
    }
    public void sendSMS(String phoneNumber,String message) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(message);
        int messageCount = parts.size();

        Log.i("Message Count", "Message Count: " + messageCount);

        ArrayList<PendingIntent> deliveryIntents = new ArrayList<>();
        ArrayList<PendingIntent> sentIntents = new ArrayList<>();

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        for (int j = 0; j < messageCount; j++) {
            sentIntents.add(sentPI);
            deliveryIntents.add(deliveredPI);
        }

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {

                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
       // smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
         sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);
    }

    @SuppressLint("ObsoleteSdkInt")
    private boolean validLocation(Location location) {
        if (location == null) {
            return false;
        }

        // Location must be from less than 30 seconds ago to be considered valid
        if (Build.VERSION.SDK_INT < 17) {
            return System.currentTimeMillis() - location.getTime() < 30e3;
        } else {
            return SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos() < 30e9;
        }
    }
    private String getAccuracy(Location location) {
        float accuracy = location.getAccuracy();
        if (accuracy < 0.01) {
            return "?";
        } else if (accuracy > 99) {
            return "99+";
        } else {
            return String.format(Locale.US, "%2.0fm", accuracy);
        }
    }
    private String getLatitude(Location location) {
        return String.format(Locale.US, "%2.5f", location.getLatitude());
    }
    private String getDMSLatitude(Location location) {
        double val = location.getLatitude();
        return String.format(Locale.US, "%.0f° %2.0f′ %2.3f″ %s",
                Math.floor(Math.abs(val)),
                Math.floor(Math.abs(val * 60) % 60),
                (Math.abs(val) * 3600) % 60,
                val > 0 ? "N" : "S"
        );
    }
    private String getDMSLongitude(Location location) {
        double val = location.getLongitude();
        return String.format(Locale.US, "%.0f° %2.0f′ %2.3f″ %s",
                Math.floor(Math.abs(val)),
                Math.floor(Math.abs(val * 60) % 60),
                (Math.abs(val) * 3600) % 60,
                val > 0 ? "E" : "W"
        );
    }
    private String getLongitude(Location location) {
        return String.format(Locale.US, "%3.5f", location.getLongitude());
    }
    private String formatLocation(Location location, String format) {
        return MessageFormat.format(format,
                getLatitude(location), getLongitude(location));
    }
    @SuppressLint({"SetTextI18n", "ResourceType"})
    public void set_profile(String username, String userrole, String imgURL, String relative) {

        name.setText(username);
        email.setText(userrole);
        Picasso.get().load(imgURL).into(img);
        callmobile = relative;

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
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            menu.findItem(R.id.action_login_logout).setTitle("Login");
            menu.findItem(R.id.action_update).setTitle("Register");


        } else {
            menu.findItem(R.id.action_login_logout).setTitle("Logout");
            menu.findItem(R.id.action_update).setTitle("Update Profile");

        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_login_logout:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    mAuth.signOut();
                    item.setTitle("Login");
                    user_name = "USER";
                    user_email = "EMAIL";
                    user_roll = "USER";
                    // user_img=null;
                    set_profile(user_name, user_roll, user_img, user_relative);
                } else {
                    startActivity(new Intent(this, Login_emailActivity.class));
                    finish();
                }
                return true;
            case R.id.action_update:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    newContent = new UserProfileFragment();
                    newContent.setArguments(bundle);
                    switchFragment(newContent);
                } else {
                    startActivity(new Intent(this, UserRegistrationActivity.class));
                    finish();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Handle navigation view item clicks here.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        bundle = new Bundle();

        switch (id) {

            case R.id.nav_our_work:
                newContent = new OurWorkFragment();
                break;
            case R.id.nav_about_us:
                newContent = new AboutUsFragment();
                break;
            case R.id.nav_Emergengy:
                newContent = new EmergencyFragment();
                break;
            case R.id.nav_feedback:
                newContent = new FeedbackAddFragment();
                break;
            case R.id.nav_contact_us:
                newContent = new ContactUsFragment();
                break;
            case R.id.nav_joinus:
                Bundle bundle = new Bundle();
                bundle.putString("user_name", user_email);
                bundle.putString("user_role", user_roll);
                bundle.putString("user_name", user_name);
                bundle.putString("user_img", user_img);
                newContent = new JoinUsFragment();
                newContent.setArguments(bundle);

                break;

            case R.id.nav_share:
                shareApp();
                break;
            case R.id.nav_locshare:
                startActivity(new Intent(this, LocationActivity.class));
            case R.id.nav_exit:
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
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }
}
