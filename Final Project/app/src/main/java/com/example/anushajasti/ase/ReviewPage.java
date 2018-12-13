package com.example.anushajasti.ase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewPage extends AppCompatActivity {
    EditText productname, reviewmatter;
    String emailid;
    Button subrev;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);
        productname = (EditText) findViewById(R.id.proname);
        reviewmatter = (EditText) findViewById(R.id.reviewmatter);
        subrev = (Button) findViewById(R.id.subrev);
        Intent intent = getIntent();
        emailid = intent.getStringExtra("emailid");
        subrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productname.getText().toString().isEmpty() || reviewmatter.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewPage.this, "please Fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    userreviews1 ur = new userreviews1();
                    ur.setEmailid(emailid);
                    ur.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ur.setProductname(productname.getText().toString());
                    ur.setReview(reviewmatter.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("Reviews").child(productname.getText().toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(ur);
                    Toast.makeText(ReviewPage.this, "Review Submitted", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
