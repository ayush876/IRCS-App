package com.example.ircsa;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Spinner;

//import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private CircleImageView setupImage;
    private FloatingActionButton addPostBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

         if(currentuser == null){
            Log.i("Output","Not Logged in");
            Intent loginintent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginintent);


            // No user is signed in
        }

         addPostBtn = findViewById(R.id.add_post_btn);
         addPostBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent newPostIntent = new Intent(MainActivity.this,NewPostActivity.class);
                 startActivity(newPostIntent);
             }
         });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new HomeFragment()).commit();
        } else if (id == R.id.nav_volunteer) {
            goToUrl( "https://docs.google.com/forms/d/e/1FAIpQLSf5TBcpJGkTq1KIPgg6Gj_mM9aypP6Bzmg1vzoZGrRrY8R2zg/viewform?usp=sf_link/");
        } else if (id == R.id.nav_intern) {
            goToUrl("https://docs.google.com/forms/d/e/1FAIpQLSeHeWZflRnWP5q-8qintDiNAk-uYyF4wZ1ZkY2ic6VCAGij3g/viewform?usp=sf_link");
        } else if (id == R.id.nav_bloodb) {
            openbloodbank();
        } else if (id == R.id.nav_oc) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new OpticalFragment()).commit();
        } else if (id == R.id.nav_jac) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new JanAushadiFragment()).commit();
        } else if (id == R.id.nav_au) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new AboutUsFragment()).commit();
        } else if (id == R.id.nav_dc) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new DistrictBankFragment()).commit();
        } else if (id == R.id.nav_developer) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new DeveloperFragment()).commit();
        } else if (id == R.id.nav_bbl) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new BblFragment()).commit();
        } else if (id == R.id.nav_donate) {
           FragmentManager fm = getSupportFragmentManager();
           fm.beginTransaction().replace(R.id.main,new CharityFragment()).commit();
        } else if (id == R.id.nav_cu) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new CsFragment()).commit();
        } else if (id == R.id.nav_emc) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new EmergencyFragment()).commit();
        } else if (id == R.id.nav_tc) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new TalukaFragment()).commit();
        } else if (id == R.id.nav_logout) {
            FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
            mAuth.signOut();
            Intent login_activityIntent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(login_activityIntent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openbloodbank(){
        Intent intent = new Intent(this, NewsFragment.class);
        startActivity(intent);
    };

    public void writeNewUser1(String name, String gender, String dob, String age, String no,String email,String address,String district,String daysavailable,String volunteer) {
        User1 user = new User1(name, gender, dob, age, no, email, address,district, daysavailable, volunteer);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("VolunteerList");
        myRef.child(user.name+" "+user.age).child("Name").setValue(user.name);
        myRef.child(user.name+" "+user.age).child("Gender").setValue(user.gender);
        myRef.child(user.name+" "+user.age).child("DOB").setValue(user.dob);
        myRef.child(user.name+" "+user.age).child("Age").setValue(user.age);
        myRef.child(user.name+" "+user.age).child("Contact Number").setValue(user.no);
        myRef.child(user.name+" "+user.age).child("Email Address").setValue(user.email);
        myRef.child(user.name+" "+user.age).child("Permanent Address").setValue(user.address);
        myRef.child(user.name+" "+user.age).child("District").setValue(user.district);
        myRef.child(user.name+" "+user.age).child("Days Available").setValue(user.dayavailable);
        myRef.child(user.name+" "+user.age).child("Interseted in").setValue(user.volunteer);

    }

    public void register(View view) {
        final EditText nameField = (EditText) findViewById(R.id.input_name);
        String name = nameField.getText().toString();

        final EditText nameField1 = (EditText) findViewById(R.id.dob);
        String name1 = nameField1.getText().toString();

        final EditText nameField2 = (EditText) findViewById(R.id.age);
        String name2 = nameField2.getText().toString();

        final EditText nameField3 = (EditText) findViewById(R.id.input_mn);
        String name3 = nameField3.getText().toString();

        final EditText nameField4 = (EditText) findViewById(R.id.input_email);
        String name4 = nameField4.getText().toString();

        final EditText nameField5 = (EditText) findViewById(R.id.input_pa);
        String name5 = nameField5.getText().toString();

        final EditText nameField6 = (EditText) findViewById(R.id.input_district);
        String name6 = nameField6.getText().toString();

        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.gender);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();

        final Spinner feedbackSpinner1 = (Spinner) findViewById(R.id.dayselect);
        String feedbackType1 = feedbackSpinner1.getSelectedItem().toString();

        final Spinner feedbackSpinner2 = (Spinner) findViewById(R.id.volunteer);
        String feedbackType2 = feedbackSpinner2.getSelectedItem().toString();

        writeNewUser1(name, feedbackType , name1, name2, name3, name4, name5, name6, feedbackType1, feedbackType2);
    }

    public void writeIntern(String fname, String lname, String mn, String amn, String age,String dob,String pa,String email,String district,String q1,String q2,String q3,String q4,String q5) {
        InternUser user = new InternUser(fname,lname,mn , amn,age,dob, pa, email , district, q1, q2, q3, q4, q5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Intern_Application_List");
        myRef.child(user.fname+" "+user.age).child("First Name").setValue(user.fname);
        myRef.child(user.fname+" "+user.age).child("Last Name").setValue(user.lname);
        myRef.child(user.fname+" "+user.age).child("Mobile Number").setValue(user.mn);
        myRef.child(user.fname+" "+user.age).child("Alternate Mobile Number").setValue(user.amn);
        myRef.child(user.fname+" "+user.age).child("Age").setValue(user.age);
        myRef.child(user.fname+" "+user.age).child("Date of Birth").setValue(user.dob);
        myRef.child(user.fname+" "+user.age).child("Permanent Address").setValue(user.address);
        myRef.child(user.fname+" "+user.age).child("Email").setValue(user.email);
        myRef.child(user.fname+" "+user.age).child("District").setValue(user.district);
        myRef.child(user.fname+" "+user.age).child("Question 1").setValue(user.q1);
        myRef.child(user.fname+" "+user.age).child("Question 2").setValue(user.q2);
        myRef.child(user.fname+" "+user.age).child("Question 3").setValue(user.q3);
        myRef.child(user.fname+" "+user.age).child("Question 4").setValue(user.q4);
        myRef.child(user.fname+" "+user.age).child("Question 5").setValue(user.q5);

    };

    public void internRegister(View view) {
        final EditText nameField = (EditText) findViewById(R.id.fname);
        String name = nameField.getText().toString();

        final EditText nameField1 = (EditText) findViewById(R.id.lname);
        String name1 = nameField1.getText().toString();

        final EditText nameField2 = (EditText) findViewById(R.id.mn);
        String name2 = nameField2.getText().toString();

        final EditText nameField3 = (EditText) findViewById(R.id.amn);
        String name3 = nameField3.getText().toString();

        final EditText nameField4 = (EditText) findViewById(R.id.age);
        String name4 = nameField4.getText().toString();

        final EditText nameField5 = (EditText) findViewById(R.id.dob);
        String name5 = nameField5.getText().toString();

        final EditText nameField7 = (EditText) findViewById(R.id.pa);
        String name7 = nameField7.getText().toString();

        final EditText nameField8 = (EditText) findViewById(R.id.email);
        String name8 = nameField8.getText().toString();

        final EditText nameField9 = (EditText) findViewById(R.id.q1);
        String name9 = nameField9.getText().toString();

        final EditText nameField10 = (EditText) findViewById(R.id.q2);
        String name10 = nameField10.getText().toString();

        final EditText nameField11 = (EditText) findViewById(R.id.q3);
        String name11 = nameField11.getText().toString();

        final EditText nameField12 = (EditText) findViewById(R.id.q4);
        String name12 = nameField12.getText().toString();

        final EditText nameField13 = (EditText) findViewById(R.id.q5);
        String name13 = nameField13.getText().toString();

        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.district);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();

        writeIntern(name, name1, name2, name3, name4, name5,name7, name8,feedbackType, name9, name10, name11, name12, name13);
    }

    //developers Page

    public void goTo_n_insta (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_n_fb (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_n_linkedin (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_k_insta (View view) {
        goToUrl ("https://www.instagram.com/kshitij_ver/?hl=en");
    }
    public void goTo_k_fb (View view) {
        goToUrl ("https://www.facebook.com/kshitij.verma.1422");
    }
    public void goTo_k_linkedin (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_ay_insta (View view) {
        goToUrl ("https://www.instagram.com/ayush__chandak/");
    }
    public void goTo_ay_fb (View view) {
        goToUrl ("https://www.facebook.com/aayushchandakroxx");
    }
    public void goTo_ay_linkedin (View view) {
        goToUrl ("https://www.linkedin.com/in/ayush-chandak-520003160/");
    }
    public void goTo_kar_insta (View view) {
        goToUrl ("https://www.instagram.com/kartikeya_aryan/");
    }
    public void goTo_kar_fb (View view) {
        goToUrl ("https://www.facebook.com/profile.php?id=100009289614261");
    }
    public void goTo_kar_linkedin (View view) {
        goToUrl ("https://www.linkedin.com/in/kartikeya-aryan-agarwal-466869167/");
    }

    public void goToSu (View view) {
        goToUrl ( "http://superuser.com/");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    //account setup

    public void onClick_AccountSetup(MenuItem menuItem)
    {
        Intent account_setup_Intent=new Intent(MainActivity.this, AccountSetup.class);
        startActivity(account_setup_Intent);
    }



}

