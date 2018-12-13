package com.example.anushajasti.ase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {
    TextView name, email;
    Button review, bckhome, logout2;

    FirebaseDatabase fd;
    DatabaseReference ref;
    String firstname, lastname, emailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        name = (TextView) findViewById(R.id.usrname);
        email = (TextView) findViewById(R.id.usremail);
        review = (Button) findViewById(R.id.rev);
        BottomNavigationView bottomNav = findViewById(R.id.btmNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.navuserprofile);
        /*logout2 = (Button) findViewById(R.id.logout2);
        logout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginpage = new Intent(UserProfile.this, MainActivity.class);
                startActivity(loginpage);
            }
        });
        bckhome = (Button) findViewById(R.id.bckhome);
        bckhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homepage = new Intent(UserProfile.this, Home.class);
                startActivity(homepage);
            }
        });*/
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewpage = new Intent(UserProfile.this, ReviewPage.class);
                reviewpage.putExtra("emailid", emailid);
                startActivity(reviewpage);
            }
        });
        fd = FirebaseDatabase.getInstance();
        ref = fd.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstname = dataSnapshot.child("firstname").getValue(String.class);
                lastname = dataSnapshot.child("lastname").getValue(String.class);
                emailid = dataSnapshot.child("emailid").getValue(String.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(firstname + " " + lastname);
                        email.setText(emailid);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent redirect = null;
            switch (menuItem.getItemId()){
                case R.id.navhome:
                    redirect = new Intent(UserProfile.this, Home.class);
                    startActivity(redirect);
                    break;
                case R.id.navreview:
                    break;
                case R.id.navuserprofile:
                    break;
                case R.id.navlogout:
                    FirebaseAuth.getInstance().signOut();
                    redirect = new Intent(UserProfile.this, MainActivity.class);
                    startActivity(redirect);
                    break;
            }
            return true;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
            startActivity(new Intent(UserProfile.this, MainActivity.class));
        }
    }
}
