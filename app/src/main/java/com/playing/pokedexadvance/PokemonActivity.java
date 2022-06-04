package com.playing.pokedexadvance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.playing.pokedexadvance.Model.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PokemonActivity extends AppCompatActivity {


    private RequestQueue requestQueue;
    SharedPreferences sharedpreferences;

    private List<Pokemon> listPokemon;
    private StorageReference ProfileImagRef;
    private Uri selectImage;

    TextView texttoCharging;
    ProgressBar progressBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        texttoCharging = findViewById(R.id.textView3);
        progressBar = findViewById(R.id.progress_circular);

        ProfileImagRef = FirebaseStorage.getInstance().getReference().child("Pokemons");

        listPokemon = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        loadPokemonWithAux();

        printingListPokemon();
    }


    private void loadPokemonWithAux(){




    }

    private void printingListPokemon(){
        System.out.println("====================================>");
        for (int i=0; i<listPokemon.size(); i++){
            System.out.println("Name: "+listPokemon.get(i).getName());

        }
        System.out.println("====================================>");

    }

    /*private void StoringInformation(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currenDate = new SimpleDateFormat("yyyy-MM-dd");
        String saveCurrentDate = currenDate.format(calendar.getTime());

        SimpleDateFormat currenTime = new SimpleDateFormat("HH:mm:ss");
        String saveCurrenTime = currenTime.format(calendar.getTime());

        String productRandomKey = saveCurrentDate + saveCurrenTime;

            final StorageReference filePath = ProfileImagRef.child(selectImage.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask = filePath.putFile(selectImage);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadImageURL = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {
                                downloadImageURL = task.getResult().toString();
                                System.out.println(downloadImageURL);
                                Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT);
                                saveTweetProduct(downloadImageURL);

                            }
                        }
                    });
                }
            });


    }

    /*public void loadPokemon() {
        final String url = "https://pokeapi.co/api/v2/pokemon?limit=151";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        String name = result.getString("name");
                        String urltoPokemon = result.getString("url");

                        Pokemon aux = new Pokemon();
                        aux.setName(name);
                        aux.setUrl(urltoPokemon);

                        listPokemon.add(aux);

                        load(urltoPokemon, aux); // me retorna type los type y photos!!
                        String nuevurl = "https://pokeapi.co/api/v2/pokemon-species/"+name+"/";
                        String description = loadDescription(nuevurl);

                        /*System.out.println("Nombre: "+name);
                        System.out.println("url: "+urltoPokemon);
                        System.out.println("Nombre from Object: "+aux.getName());
                        System.out.println("utl from object: "+aux.getUrl());
                        System.out.println("slot1 from Object: "+aux.getSprite2());
                        System.out.println("slot2 from Object: "+aux.getSprite2());
                        System.out.println("imagen from Object: "+aux.getImagen());
                        System.out.println("description from Object: "+description);


                    }

                } catch (JSONException e) {
                    Log.e("cs50", "Json error", e);
                }
            }
        }, new Response.ErrorListener() {
       //     @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon list error", error);
            }
        });

        requestQueue.add(request);
    }

    public void load(String urlpokemon, final Pokemon toCharge) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlpokemon, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray typeEntries = response.getJSONArray("types");
                    for (int i = 0; i < typeEntries.length(); i++) {
                        JSONObject typeEntry = typeEntries.getJSONObject(i);
                        int slot = typeEntry.getInt("slot");
                        String type = typeEntry.getJSONObject("type").getString("name");

                        if (slot == 1) {
                            toCharge.setSprite1(type);
                        }
                        else if (slot == 2) {
                            toCharge.setSprite2(type);
                        }
                    }
                    JSONObject imageEntry = response.getJSONObject("sprites");
                    toCharge.setImagen(imageEntry.getString("front_shiny"));
                   // new DownloadSpriteTask().execute(imageEntry.getString("front_shiny"));

                } catch (JSONException e) {
                    Log.e("cs50", "Pokemon json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon details error", error);
            }
        });

        requestQueue.add(request);
    }

  /*  private String loadDescription(String nuevaURL){
        System.out.println(nuevaURL);
        final String[] retornando = new String[1];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, nuevaURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray flavorTextEntries = response.getJSONArray("flavor_text_entries");
                    for (int j=0; j< flavorTextEntries.length(); j++){
                        JSONObject flavorEntry = flavorTextEntries.getJSONObject(j);
                        String sstr = flavorEntry.getJSONObject("language").getString("name");
                        if (sstr.equals("en")){
                            String flavor_text =flavorEntry.getString("flavor_text");
                            retornando[0] = flavor_text;

                        }
                    }

                } catch (JSONException e) {
                    Log.e("cs50", "Pokemon json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon details error", error);
            }
        });

        requestQueue.add(request);

        if (TextUtils.isEmpty(retornando[0])){
            return null;
        }else {
            return retornando[0];
        }


    }


   private class DownloadSpriteTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());

            } catch (IOException e){
                Log.e("cs50", "Download sprite error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

        }
    }*/

}
