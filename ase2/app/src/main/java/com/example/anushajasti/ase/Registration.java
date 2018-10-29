package com.example.anushajasti.ase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
EditText emailid,pass,confirmpass,phone,firstname,lastname;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth=FirebaseAuth.getInstance();
    }
    public void register(View v){
    emailid=(EditText)findViewById(R.id.emailid);
    pass=(EditText)findViewById(R.id.pass);
    confirmpass=(EditText)findViewById(R.id.confirmpass);
    phone=(EditText)findViewById(R.id.phone);
        firstname=(EditText)findViewById(R.id.firstname);
        lastname=(EditText)findViewById(R.id.lastname);
        if(TextUtils.isEmpty(phone.getText())&&TextUtils.isEmpty(firstname.getText())&&TextUtils.isEmpty(lastname.getText())&&TextUtils.isEmpty(emailid.getText())&&TextUtils.isEmpty(pass.getText())&&TextUtils.isEmpty(confirmpass.getText())){
            Toast.makeText(this,"please fill all details",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.getText().toString().equals(confirmpass.getText().toString())){

            firebaseAuth.createUserWithEmailAndPassword(emailid.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Registration.this,"registration successful",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(Registration.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Registration.this,"failed to register",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(this,"Password Miss Match",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void loginpage(View v){
        Intent loginp=new Intent(Registration.this,MainActivity.class);
        startActivity(loginp);
    }
}
