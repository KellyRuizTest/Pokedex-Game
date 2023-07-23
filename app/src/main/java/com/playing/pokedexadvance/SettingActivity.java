package com.playing.pokedexadvance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.playing.pokedexadvance.Model.Users;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity {

    private TextInputEditText name;
    private TextInputEditText username;
    private TextInputEditText password;
    private MaterialButton save;
    private TextView changeImage;
    private ImageView profileImage;
    private Uri selectImage;

    // Firebase
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    // Utils
    private String FINSTANCE = "https://pokedex-app-71958-default-rtdb.firebaseio.com/";
    private String downloadURL;
    private Boolean updateImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        name = findViewById(R.id.edit_name);
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        save = findViewById(R.id.update_account);
        changeImage = findViewById(R.id.change_image_profile);
        profileImage = findViewById(R.id.circle_change_imageprofile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("Profiles");
        databaseReference = FirebaseDatabase.getInstance(FINSTANCE).getReference().child("Users");

        // fetch all data
        fetchUserdata();

        // textview to change profile image
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String [] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else{
                    openGallery();
                }
            }
        });

        // click on profile image to change "profile image"
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String [] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else{
                    openGallery();
                }
            }
        });

        // Click on Button to update user information
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateImage) {
                    updateImagenAccountWithValidation();
                }else{
                    updateAccountWithValidation();
                }
            }
        });
    }



    private void openGallery() {

        updateImage = true;
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        selectImage = data.getData();

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            Picasso.get().load(selectImage).fit().centerCrop().into(profileImage);
        }else {
            Toast.makeText(SettingActivity.this, "There is a issue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void updateImagenAccountWithValidation() {

        String name_aux = name.getText().toString().trim();
        String username_aux = username.getText().toString();
        String password_aux = password.getText().toString().trim();

        if (TextUtils.isEmpty(name_aux)){
            Toast.makeText(this, "Put anything in the Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(username_aux)){
            Toast.makeText(this, "Put anything in the Username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password_aux)){
            Toast.makeText(this, "Put any Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validationPassword(password_aux)){
            Toast.makeText(this, "Must contain at least 4 characteres, at least 1 letter and no spaces", Toast.LENGTH_SHORT).show();
            return;
        }

        final HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name_aux);
        userMap.put("username", username_aux);
        userMap.put("password", password_aux);

        final StorageReference filePath = storageReference.child(selectImage.getLastPathSegment()+".jpg");
        final UploadTask uploadTask = filePath.putFile(selectImage);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        String downloadURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            downloadURL = task.getResult().toString();
                            Log.d("Image URL", "Sucessfully");
                            userMap.put("image", downloadURL);

                            // saving information
                            databaseReference.child(firebaseUser.getUid().toString()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Profile was updated!", Toast.LENGTH_SHORT).show();
                                        updateImage = false;
                                        Intent inten = new Intent(SettingActivity.this, HomeActivity.class);
                                        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(inten);

                                    }else{
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
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

    private void updateAccountWithValidation() {

        String name_aux = name.getText().toString().trim();
        String username_aux = username.getText().toString();
        String password_aux = password.getText().toString().trim();

        if (TextUtils.isEmpty(name_aux)){
            Toast.makeText(this, "Put anything in the Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(username_aux)){
            Toast.makeText(this, "Put anything in the Username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password_aux)){
            Toast.makeText(this, "Put any Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validationPassword(password_aux)){
            Toast.makeText(this, "Must contain at least 4 characteres, at least 1 letter and no spaces", Toast.LENGTH_SHORT).show();
            return;
        }

        final HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name_aux);
        userMap.put("username", username_aux);
        userMap.put("password", password_aux);

        databaseReference.child(firebaseUser.getUid().toString()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Profile was updated!", Toast.LENGTH_SHORT).show();
                    updateImage = false;
                    Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void fetchUserdata() {

        DatabaseReference Db = FirebaseDatabase.getInstance(FINSTANCE).getReference().child("Users").child(firebaseUser.getUid());
        Db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name_ax = dataSnapshot.getValue(Users.class).getName();
                String username_ax = dataSnapshot.getValue(Users.class).getUsername();
                String password_ax = dataSnapshot.getValue(Users.class).getPassword();
                String url_image = dataSnapshot.getValue(Users.class).getImage();

                username.setText(username_ax.toString());
                name.setText(name_ax.toString().trim());
                password.setText(password_ax.toString());
                Picasso.get().load(url_image).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}