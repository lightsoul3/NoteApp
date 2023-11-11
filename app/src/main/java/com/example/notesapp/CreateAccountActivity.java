package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emaiEditText,passwordEditText,confirmPasswordEditText;
    Button createAccountButton;
    ProgressBar progressBar;
    TextView loginButtonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emaiEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountButton = findViewById(R.id.create_account_button);
        progressBar = findViewById(R.id.progress_bar);
        loginButtonTextView = findViewById(R.id.login_text_view_button);

        createAccountButton.setOnClickListener((v)-> createAccount());
        loginButtonTextView.setOnClickListener((v)-> finish());
    }

    void createAccount(){
        String email = emaiEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);
        if(!isValidated){
            return;
        }

        createAccountInFireBase(email,password);
    }

    boolean validateData(String email, String password, String confirmPassword){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emaiEditText.setError("Email is Invalid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password less then 6 symbols");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Passwords not matched");
            return false;
        }
        return true;
    }

    void createAccountInFireBase(String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            // creating account is done
                            Utility.showToast(CreateAccountActivity.this,"Succesfully created account, Check email to varify");
                            firebaseAuth.signOut();
                            finish();
                        }else{
                            Utility.showToast(CreateAccountActivity.this,
                                    task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountButton.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            createAccountButton.setVisibility(View.VISIBLE);
        }
    }
}