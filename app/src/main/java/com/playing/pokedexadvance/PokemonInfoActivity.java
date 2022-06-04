package com.playing.pokedexadvance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.playing.pokedexadvance.Model.PokemonInfo;
import com.playing.pokedexadvance.Model.PokemonInfoFirebase;
import com.playing.pokedexadvance.Model.PokemonResponse;
import com.playing.pokedexadvance.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonInfoActivity extends AppCompatActivity {

    TextView PokemonWeight;
    TextView PokemonHeight;
    Chip ChipType1;
    Chip ChipType2;
    ProgressBar PokemonHPprogress;
    ProgressBar PokemonDEFprogress;
    ProgressBar PokemonATKprogress;
    ProgressBar PokemonSPDprogress;
    ProgressBar PokemonEXPprogress;
    LottieAnimationView lottieAnimationView;
    private FirebaseUser firebaseUser;
    private DatabaseReference productsRef;
    private PokemonInfoFirebase pokemonFirebase;
    private String FINSTANCE = "https://pokedex-app-71958-default-rtdb.firebaseio.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_info);

        TextView PokemonName = findViewById(R.id.name_pokemon);
        ImageView PokemonImage = findViewById(R.id.image_pokemon);
        PokemonWeight = findViewById(R.id.weight);
        PokemonHeight = findViewById(R.id.height);
        ChipType1 = findViewById(R.id.type1);
        ChipType2 = findViewById(R.id.type2);
        PokemonHPprogress = findViewById(R.id.hp_progress);
        PokemonATKprogress = findViewById(R.id.atq_progress);
        PokemonSPDprogress = findViewById(R.id.spd_progress);
        PokemonDEFprogress = findViewById(R.id.def_progress);
        PokemonEXPprogress = findViewById(R.id.exp_progress);
        lottieAnimationView = findViewById(R.id.catch_poke);
        productsRef = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/").getReference().child("Pokemon");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        String name = "";
        String url = "";
        String url_image = "";
        String url_info = "";
        String id_pokemon = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null){

            name = extras.getString("name");
            url = extras.getString("url");
            url_image = extras.getString("url_image");
            url_info = extras.getString("url_info");

            pokemonFirebase = new PokemonInfoFirebase(name, url_image, url_info);
        }

        PokemonName.setText(name);
        Picasso.get().load(url_image).into(PokemonImage);

        fetchdataInfo(url_info);

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lottieAnimationView.playAnimation();
                catchPokemonForUser();
            }
        });

    }

    private void catchPokemonForUser() {

        String username = firebaseUser.getUid().toString();
        pokemonFirebase.setId_user(username);
        final String pid = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/").getReference().child("Pokemon").push().getKey();

        String type2;

        if (!pokemonFirebase.getType2().equals("") || pokemonFirebase.getType2() != null){
            type2 = pokemonFirebase.getType2();
        }else{
            type2 = "";
        }

        //put pokemon in Firebase
        HashMap<String, Object> pokemonMap = new HashMap<>();

        pokemonMap.put("id_pokemon", pokemonFirebase.getId_pokemon());
        pokemonMap.put("id_user", username);
        pokemonMap.put("pid", pid);
        pokemonMap.put("name", pokemonFirebase.getName());
        pokemonMap.put("weight", pokemonFirebase.getWeight());
        pokemonMap.put("height", pokemonFirebase.getHeight());
        pokemonMap.put("type1", pokemonFirebase.getType1());
        pokemonMap.put("type2", type2);
        pokemonMap.put("url_image", pokemonFirebase.getUrl_image());
        pokemonMap.put("url_info", pokemonFirebase.getUrl_info());
        pokemonMap.put("hp", pokemonFirebase.getHp());
        pokemonMap.put("def", pokemonFirebase.getDef());
        pokemonMap.put("atack", pokemonFirebase.getSpeed());
        pokemonMap.put("exp", pokemonFirebase.getExp());
        pokemonMap.put("selling", false);
        pokemonMap.put("cost", 3000);
        System.out.println(pokemonMap);

        productsRef.child(pid).updateChildren(pokemonMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Catched", Toast.LENGTH_LONG).show();
                    FirebaseDatabase.getInstance(FINSTANCE).getReference().child("Users").child(firebaseUser.getUid().toString()).child("Catched").child(pid).setValue(true);

                }else{
                    Toast.makeText(getApplicationContext(), "Error in catching", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void fetchdataInfo(String url_info) {

        RetrofitClient.getClient().getPokemonInfo(url_info).enqueue(new Callback<PokemonInfo>() {
            @Override
            public void onResponse(Call<PokemonInfo> call, Response<PokemonInfo> response) {
                if (response.isSuccessful() && response.body() != null){

                    PokemonInfo data = response.body();
                    String format_height = String.format( "%.1f m",data.getHeight().floatValue()/10);
                    String format_weight = String.format( "%.1f kg",data.getWeight().floatValue()/10);
                    pokemonFirebase.setHeight(data.getHeight().toString());
                    pokemonFirebase.setWeight(data.getWeight().toString());

                    PokemonHeight.setText(format_height);
                    PokemonWeight.setText(format_weight);

                    PokemonEXPprogress.setProgress(data.getBaseExperience());
                    pokemonFirebase.setExp(data.getBaseExperience().toString());
                    pokemonFirebase.setId_pokemon(data.getId() );

                    for (int i =0; i< data.getStats().size(); i++){

                        System.out.println(data.getStats().get(i).getStat().getName().toString());
                        System.out.println(data.getStats().get(i).getBaseStat());

                        if (data.getStats().get(i).getStat().getName().toString().equals("hp")){
                            Integer hp = Integer.valueOf(data.getStats().get(i).getBaseStat().toString());
                            PokemonHPprogress.setProgress(hp);
                            pokemonFirebase.setHp(hp.toString());
                        }
                        if (data.getStats().get(i).getStat().getName().toString().equals("attack")){
                            Integer attack = Integer.valueOf(data.getStats().get(i).getBaseStat().toString());
                            PokemonATKprogress.setProgress(attack);
                            pokemonFirebase.setAtack(attack.toString());
                        }
                        if (data.getStats().get(i).getStat().getName().toString().equals("defense")){
                            Integer defense = Integer.valueOf(data.getStats().get(i).getBaseStat().toString());
                            PokemonDEFprogress.setProgress(defense);
                            pokemonFirebase.setDef(defense.toString());
                        }
                        if (data.getStats().get(i).getStat().getName().toString().equals("speed")){
                            Integer speed = Integer.valueOf(data.getStats().get(i).getBaseStat().toString());
                            PokemonSPDprogress.setProgress(speed);
                            pokemonFirebase.setSpeed(speed.toString());
                        }

                    }

                    if (data.getTypes().size() > 1){
                        ChipType1.setText(data.getTypes().get(0).getType().getName());
                        ChipType2.setText(data.getTypes().get(1).getType().getName());
                        pokemonFirebase.setType1(data.getTypes().get(0).getType().getName().toString());
                        pokemonFirebase.setType2(data.getTypes().get(1).getType().getName().toString());

                    } else {
                        ChipType1.setText(data.getTypes().get(0).getType().getName());
                        ChipType2.setChipIconVisible(false);
                        ChipType2.setVisibility(View.GONE);
                        pokemonFirebase.setType1(data.getTypes().get(0).getType().getName().toString());
                        pokemonFirebase.setType2("");
                    }

                }
            }

            @Override
            public void onFailure(Call<PokemonInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}