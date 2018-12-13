package com.example.anushajasti.ase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TextView txtStatus;
    LoginButton login_button;
    CallbackManager callbackManager;
    GoogleSignInClient GoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    Button login, signup;
    EditText email, password;
    GoogleSignInOptions gso;
    GoogleSignInClient mgooglesignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.login);
        firebaseAuth = FirebaseAuth.getInstance();
        signup = (Button) findViewById(R.id.signup);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
//starts here

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mgooglesignin = GoogleSignIn.getClient(this, gso);

        // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //printToken(account);
        this.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = mgooglesignin.getSignInIntent();
                startActivityForResult(signinIntent, 101);
            }
        });


//Ends here

    /*    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("995063175050-vrah0c4tsiudiq2o3did6cglcah5tvgt.apps.googleusercontent.com")
                .build();
        initializeControls();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient = GoogleSignIn.getClient(this, gso);
       findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }*/
//Starts here




   /* private void printToken(GoogleSignInAccount account) {
        if (account == null) {
            Toast.makeText(this, "You are not authenticated", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "email : "+account.getEmail()+"    Token :"+account.getIdToken(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "You are authenticated", Toast.LENGTH_SHORT).show();
        }
    }*/

//Ends here

   /* private void signIn() {
        Intent signInIntent = GoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.d("out",task.toString());
            GoogleSignInAccount gsa= null;
            try {
                gsa = task.getResult(ApiException.class);
                //String x=gsa.getId().toString();
                //  txtStatus.setText("Login Successfull\n"+gsa.getEmail());
                Intent homepage= new Intent(MainActivity.this,Home.class);
                startActivity(homepage);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("err",e.getMessage().toString());
            }

        };
    }*/
//End of Google Authentication


//Facebook Authentication Starts Here
        // private void initializeControls(){
        callbackManager = CallbackManager.Factory.create();
        // txtStatus = (TextView)findViewById(R.id.);
        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email", "user_friends"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //txtStatus.setText("Login Successful\n"+loginResult.getAccessToken());
                Intent homepage = new Intent(MainActivity.this, Home.class);
                startActivity(homepage);
            }

            @Override
            public void onCancel() {
                txtStatus.setText("Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                txtStatus.setText("Login Error: " + error.getMessage());
                Log.e("err", error.toString());
            }
        });
    }

    //End of Facebook Authentication
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Intent redirecthome = new Intent(MainActivity.this, Home.class);
                startActivity(redirecthome);
                // printToken(account);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("error", e.getMessage().toString());
                //  printToken(null);
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Firebase Authentication
    public void login(View v) {
        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "please fill in Details", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        //Toast.makeText(MainActivity.this, firebaseAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(MainActivity.this, Home.class);
                        startActivity(home);
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication Failure", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

//End of Firebase Authentication

    public void registeruser(View v) {
        Intent register = new Intent(MainActivity.this, Registration.class);
        startActivity(register);
    }
}


