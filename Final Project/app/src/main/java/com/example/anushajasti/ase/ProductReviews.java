package com.example.anushajasti.ase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductReviews extends AppCompatActivity {
    String productname;
    TextView name;
    ListView reviewlist;
    FirebaseDatabase fd;
    DatabaseReference ref;
    Button homebck;
    List<userreviews1> list;
    userreviews1 ur1;
    CustomAdapter1 customadapter = new CustomAdapter1();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<userreviews1>();
        setContentView(R.layout.activity_product_reviews);
        name = (TextView) findViewById(R.id.proname);
        BottomNavigationView bottomNav = findViewById(R.id.btmNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.navreview);
        /*homebck = (Button) findViewById(R.id.homebck);*/
       /* homebck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(ProductReviews.this, Home.class);
                startActivity(home);
            }
        });*/
        Intent intent = getIntent();
        productname = intent.getStringExtra("productname");
        name.setText(productname);
        reviewlist = (ListView) findViewById(R.id.reviewList);
        reviewlist.setAdapter(customadapter);
        fd = FirebaseDatabase.getInstance();
        ref = fd.getReference("Reviews").child(productname);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String useremailid = ds.child("emailid").getValue(String.class);
                    String userreview = ds.child("review").getValue(String.class);
                    String userid = ds.child("emailid").getValue(String.class);
                    String productname = ds.child("emailid").getValue(String.class);
                    //  Log.d("name",useremailid);
                    ur1 = new userreviews1();
                    ur1.setEmailid(useremailid);
                    ur1.setReview(userreview);
                    ur1.setProductname(productname);
                    ur1.setId(userid);
                    list.add(ur1);
                    reviewlist.setAdapter(customadapter);
                }
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
                    redirect = new Intent(ProductReviews.this, Home.class);
                    startActivity(redirect);
                    break;
                case R.id.navreview:
                    break;
                case R.id.navuserprofile:
                    redirect = new Intent(ProductReviews.this, UserProfile.class);
                    startActivity(redirect);
                    break;
                case R.id.navlogout:
                    FirebaseAuth.getInstance().signOut();
                    redirect = new Intent(ProductReviews.this, MainActivity.class);
                    startActivity(redirect);
                    break;
            }
            return true;
        }
    };
    class CustomAdapter1 extends BaseAdapter {
        TextView emailidl, reviewlist;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlist, null);
            emailidl = (TextView) convertView.findViewById(R.id.emailidcustom);
            reviewlist = (TextView) convertView.findViewById(R.id.reviewcustom);
            // Log.d("name",list.toString());
            userreviews1 ur2 = list.get(position);
            //  Log.d("name",ur2.toString());
            emailidl.setText(ur2.getEmailid());
            //Log.d("name",ur2.getEmailid());
            reviewlist.setText(ur2.getReview());
            return convertView;
        }
    }
}
