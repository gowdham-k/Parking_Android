package com.polsec.pyrky.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.ar.core.ArCoreApk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.polsec.pyrky.activity.booking.BookingsActivity;
import com.polsec.pyrky.activity.signin.SignInActivity;
import com.polsec.pyrky.fragment.HomeFragment;
import com.polsec.pyrky.fragment.NotificationFragment;
import com.polsec.pyrky.fragment.ProfileFragment;
import com.polsec.pyrky.R;
import com.polsec.pyrky.fragment.SettingsFragment;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.CircleTransformation;
import com.polsec.pyrky.utils.Constants;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    BottomNavigationView.OnNavigationItemSelectedListener {
    Context context = this;
    @Inject
    Retrofit retrofit;
    Boolean isRunning = true;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ActionBar actionbar;
    ActionBarDrawerToggle toggle;
    TextView textview;
    TextView toolbarText,Username;
    RelativeLayout.LayoutParams layoutparams;
    String UsrName;
    private FirebaseAuth mAuth;
    private int avatarSize;
    View view,holderView, contentView,contentView1;
    String profileImageUrl;
    CircleImageView profileImage;
    NavigationView navigationView;
    String Nameval="settings";
    boolean isDrawerLocked;
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ((MyApplication )getApplication()).getNetComponent().inject(this);
//    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkWhetherArEnabled();

        String parkType = PreferencesHelper.getPreference(HomeActivity.this,PreferencesHelper.PREFERENCE_PARKING_TYPES);
        String secRatings = PreferencesHelper.getPreference(HomeActivity.this,PreferencesHelper.PREFERENCE_SECURITY_RATINGS);
        String carCategory = PreferencesHelper.getPreference(HomeActivity.this,PreferencesHelper.PREFERENCE_CAR_CATEGORY);
        Log.e("parkType",parkType);
        Log.e("secRatings",secRatings);
        Log.e("carCategory",carCategory);

        if (!parkType.equals("")&&!secRatings.equals("")&&!carCategory.equals("")){

            Type type = new TypeToken<List<String>>() { }.getType();
            List<String> restoreData1 = new Gson().fromJson(parkType, type);
            List<String> restoreData2 = new Gson().fromJson(secRatings, type);
            List<String> restoreData3 = new Gson().fromJson(carCategory, type);

            Constants.PARKING_TYPES = restoreData1;
            Constants.SECURITY_RATINGS = restoreData2;
            Constants.CAR_CATEGORY = restoreData3;

            if (restoreData1.size() >0 && restoreData2.size() >0 && restoreData3.size() >0){
//                Toast.makeText(context, restoreData1.get(0)+restoreData2.get(0)+restoreData3.get(0), Toast.LENGTH_LONG).show();
            }
        }



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAuth = FirebaseAuth.getInstance();
//        actionbar.setTitle("Home");

        toolbarText = findViewById(R.id.toolbar_text);
        setSupportActionBar(toolbar);
        view = (View)findViewById(R.id.myview);
        view.setVisibility(View.VISIBLE);
        UsrName=PreferencesHelper.getPreference(HomeActivity.this, PreferencesHelper.PREFERENCE_USER_NAME);

//
//        Username=findViewById(R.id.);
//        Username.setText(UsrName);
//        startActivity(getIntent());
//        Intent chatIntent = getIntent();
//        if(chatIntent!=null){
//            String Value=chatIntent.getStringExtra("name");
//
//            if(Nameval.equals(Value)){
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
//                transaction.replace(R.id.main_frame_layout, new SettingsFragment());
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//
//            }
//            else {
//
//            }
        checkWhetherArEnabled();
        holderView = findViewById(R.id.holder);

        contentView = findViewById(R.id.home_coordinator);
        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.offsetTopAndBottom(0);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toolbar.inflateMenu(R.menu.activity_main_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ham_menu);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(v -> {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        profileImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        profileImageUrl= PreferencesHelper.getPreference(HomeActivity.this,PreferencesHelper.PREFERENCE_PROFILE_PIC);
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            Picasso.with(HomeActivity.this)
                    .load(profileImageUrl)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(profileImage);
        }
        txtProfileName.setText(UsrName);


//        drawer.setScrimColor(Color.TRANSPARENT);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                holderView.setTranslationX(slideX);
//                drawer.setScrimColor(Color.TRANSPARENT);
//                holderView.setBackgroundColor(Color.TRANSPARENT);
            }
        };

        drawer.addDrawerListener(actionBarDrawerToggle);

//        drawer.setScrimColor(Color.TRANSPARENT);
//        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//                                     @Override
//                                     public void onDrawerSlide(View drawer, float slideOffset) {
//                                         contentView.setX(navigationView.getWidth() * slideOffset);
//
//                                         RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
//                                         lp.height = drawer.getHeight() -
//                                                 (int) (drawer.getHeight() * slideOffset * 0.3f);
//
////                                         lp.topMargin = (drawer.getHeight() - lp.height) / 2;
//                                         contentView.setLayoutParams(lp);
//
//                                     }
//
//                                     @Override
//                                     public void onDrawerClosed(View drawerView) {
//                                     }
//                                 }
//        );

        try {
            //int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                // Do something for lollipop and above versions

                Window window = getWindow();

                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                // finally change the color to any color with transparency
//
//                window.setStatusBarColor(getResources().getColor(R.color.transparent2));
            }

        } catch (Exception e) {

            Crashlytics.logException(e);

        }

//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
//        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
//         isDrawerLocked= true;
//        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.b_nav_home:
                        fragment = new HomeFragment();
                        navigationView.setCheckedItem(R.id.b_nav_home);

//                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        toolbarText.setText(R.string.homepage);
                        break;

                    case R.id.b_nav_notification:
                        fragment = new NotificationFragment();
//                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        toolbarText.setText(R.string.Notification);

                        break;

                    case R.id.b_nav_profile:
                        fragment = new ProfileFragment();
                        navigationView.setCheckedItem(R.id.b_nav_profile);
//                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        toolbarText.setText(R.string.Profile);
                        break;
                }
                return loadFragment(fragment);
            }
        });
//        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        loadFragment(new HomeFragment());
        toolbarText.setText(R.string.homepage);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            bottomNavigationView.setSelectedItemId(R.id.b_nav_home);
            navigationView.setCheckedItem(R.id.nav_home);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
            transaction.replace(R.id.main_frame_layout, new HomeFragment()).commit();
            toolbarText.setText(R.string.homepage);
        } else if (id == R.id.nav_booking) {
//
//            loadFragment(new BookingsFragment());
            navigationView.setCheckedItem(R.id.nav_booking);
            Intent intent=new Intent(HomeActivity.this, BookingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);


        } else if (id == R.id.nav_profile) {

            bottomNavigationView.setSelectedItemId(R.id.b_nav_profile);
            navigationView.setCheckedItem(R.id.nav_profile);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.main_frame_layout, new ProfileFragment()).commit();
//            transaction.addToBackStack(null);
//            transaction.commit();
            toolbarText.setText(R.string.Profile);

//
        } else if (id == R.id.nav_logout) {


            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
//            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            startActivity(intent);
            PreferencesHelper.signOut(HomeActivity.this);
            mAuth.signOut();
            HomeActivity.this.finish();

        }else if (id == R.id.profile_img){
            Toast.makeText(context, "Image selected", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.profile_name){
            Toast.makeText(context, "Image title", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }



    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            Picasso.with(HomeActivity.this)
                    .load(profileImageUrl)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(profileImage);
        }
    }

    void checkWhetherArEnabled() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkWhetherArEnabled();
                }
            }, 300);
        }
        if (availability.isSupported()) {
            Constants.IS_AR_ENABLED = true;
            // indicator on the button.
        } else { // Unsupported or unknown.
            Constants.IS_AR_ENABLED = false;
        }
    }



}
