package com.example.labfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        this.UpdateUi(currentUser);

    }



    public void OnClickButtonSignUp(View view){

        EditText editTextEmail = findViewById(R.id.editTextLoginEmail);
        EditText editTextPassword = findViewById(R.id.editTextLoginPassword);
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        this.SignUp(email,password);


    }

    public void OnClickButtonLogin(View view){

        EditText editTextEmail = findViewById(R.id.editTextLoginEmail);
        EditText editTextPassword = findViewById(R.id.editTextLoginPassword);
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        this.SignIn(email,password);
    }


    public void SignUp(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(getApplicationContext(), "Usuario registrado",
                                    Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUi(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            UpdateUi(null);
                        }

                        // ...
                    }
                });
    }


    public void SignIn(String email, String password){

        this.mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUi(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Fallo en la autenticacion del usuario",
                                    Toast.LENGTH_LONG).show();
                            UpdateUi(null);
                        }

                        // ...
                    }
                });

    }


    public void UpdateUi(FirebaseUser currentUser){

        if(currentUser == null){

        }
        else{

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

    }
}
