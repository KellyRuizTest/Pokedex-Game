package com.playing.pokedexadvance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {

    private Button registration_button;
    private Button login_accout;
    private TextView forgot_account;
    private EditText email_edit;
    private EditText password_edit;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayoutbar;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registration_button = findViewById(R.id.registration_button_new);
        login_accout = findViewById(R.id.login_account_new);
        forgot_account = findViewById(R.id.forget_password_text);
        email_edit = findViewById(R.id.email_login);
        password_edit = findViewById(R.id.pass_login);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        login_accout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginUser();
            }
        });

        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intenToRegistartion = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intenToRegistartion);
                finish();
            }
        });


    }

    private void LoginUser() {

        String email = email_edit.getText().toString(); 
        String password = password_edit.getText().toString();
        
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Username and Password are required", Toast.LENGTH_SHORT).show();
        }else{

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(), "Logged Succesfully", Toast.LENGTH_LONG).show();
                                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intentMain);
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                                loadingBar.dismiss();
                            }

                        }
                    });
            }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}