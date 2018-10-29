package com.example.anushajasti.ase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TextView txtStatus;
    LoginButton login_button;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    Button login,signup;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        login=(Button)findViewById(R.id.login);
        firebaseAuth=FirebaseAuth.getInstance();
        signup=(Button)findViewById(R.id.signup);
        email=(EditText) findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestEmail()
                .build();
        initializeControls();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,101);
    }

    private void initializeControls(){
        callbackManager = CallbackManager.Factory.create();
       // txtStatus = (TextView)findViewById(R.id.);
        login_button = (LoginButton)findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile","email","user_friends"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //txtStatus.setText("Login Successful\n"+loginResult.getAccessToken());
                Intent homepage= new Intent(MainActivity.this,Home.class);
                startActivity(homepage);
            }

            @Override
            public void onCancel() {
                txtStatus.setText("Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                txtStatus.setText("Login Error: "+error.getMessage());
                Log.e("err",error.toString());
            }
        });
    }
    //Google Sign OAuth is written here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                        GoogleSignInAccount gsa=task.getResult();
                        String x=gsa.getIdToken().toString();
                  //  txtStatus.setText("Login Successfull\n"+gsa.getEmail());
            Intent homepage= new Intent(MainActivity.this,Home.class);
            startActivity(homepage);
                };
            }
            public void login(View v){
        firebaseAuth.signInWithEmailAndPassword(email.toString().trim(),password.toString().trim()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Intent home=new Intent(MainActivity.this,Home.class);
                startActivity(home);
            }
        });
            }
            public void registeruser(View v){
        Intent register= new Intent(MainActivity.this,Registration.class);
        startActivity(register);
            }
        }


