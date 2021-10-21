package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoadScreen extends AppCompatActivity {

    private TextView title;
    private Button signIn;
    private SignInButton google_signIn;
    TextView newuser;
    TextView signup;
    //public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    //private GoogleSignInClient mGoogleSignInClient;

    /*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_screen);
        title = findViewById(R.id.loadScreen);
        signIn = findViewById((R.id.sign_in));
        newuser = findViewById(R.id.newuser);
        signup = findViewById(R.id.signup);

        Typeface mont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf");
        title.setTypeface(mont);
        newuser.setTypeface(mont);
        signup.setTypeface(mont);

        /*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);*/
        google_signIn = findViewById(R.id.google_signin);
        mAuth = FirebaseAuth.getInstance();

        google_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        signIn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoadScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoadScreen.this, SignUpGeneral.class);
                startActivity(intent);
            }
        });
    }


    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(LoadScreen.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
                FirebaseGoogleAuth(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoadScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoadScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoadScreen.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser fUser = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoadScreen.this, "Sorry Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/




}