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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.playing.pokedexadvance.Model.Users;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText editName;
    private EditText editEmail;
    private EditText editPassw;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    //private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerButton = findViewById(R.id.register_account_btn);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPassw = findViewById(R.id.edit_pass);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAccountWithValidation();

            }
        });

    }

    private void createAccountWithValidation() {

        final String name = editName.getText().toString();
        final String email = editEmail.getText().toString();
        final String password =editPassw.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(getApplicationContext(), "Name is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() > 20 ){
            Toast.makeText(getApplicationContext(), "name is too large", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Email is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!validationEmail(email)){
            editEmail.setError("Invalid email");
            return;
        }

        if (validationPassword(password)){
            editPassw.setError("Must contain at least 4 characteres, at least 1 letter and no spaces");
            return;
        }

        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Please wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String id = firebaseUser.getUid();

                    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/").getReference().child("Users");
                    String image = "https://firebasestorage.googleapis.com/v0/b/pokedex-app-71958.appspot.com/o/Profiles%2Fusermale.png?alt=media&token=715448bc-0e74-4f78-9ef5-a397f495d119";
                    int iconSet = 100;

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("id", id);
                    userdataMap.put("name", name);
                    userdataMap.put("email", email);
                    userdataMap.put("password", password);
                    userdataMap.put("bio", "");
                    userdataMap.put("image", image);
                    userdataMap.put("coin", iconSet);

                    //final Users aux = new Users(id, name, email, password, "", image, icon);

                    rootRef.child(id).setValue(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadingBar.dismiss();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                loadingBar.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Network issue, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                } else {

                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "This email account already exists", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }



    private boolean validationPassword(String password) {
        Pattern pattern = Pattern.compile("^" + "(?=.*[a-zA-Z])" + "(?=\\S+$)" + ".{4,10}" + "$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        boolean matchFound = matcher.find();

        return matchFound;
    }

    private boolean validationEmail(String email) {

        Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Matcher matcher = pattern.matcher(email);
        boolean matchFound = matcher.find();

        return matchFound;
    }


}