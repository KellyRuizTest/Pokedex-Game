package com.playing.pokedexadvance;

import static com.playing.pokedexadvance.R.drawable.agua;
import static com.playing.pokedexadvance.R.drawable.bug;
import static com.playing.pokedexadvance.R.drawable.electric;
import static com.playing.pokedexadvance.R.drawable.fight;
import static com.playing.pokedexadvance.R.drawable.fire;
import static com.playing.pokedexadvance.R.drawable.flying;
import static com.playing.pokedexadvance.R.drawable.fuego;
import static com.playing.pokedexadvance.R.drawable.normal;
import static com.playing.pokedexadvance.R.drawable.plant_1;
import static com.playing.pokedexadvance.R.drawable.poison;
import static com.playing.pokedexadvance.R.drawable.rock;
import static com.playing.pokedexadvance.R.drawable.water;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.playing.pokedexadvance.Model.PokemonInfo;
import com.playing.pokedexadvance.Model.PokemonInfoFirebase;
import com.playing.pokedexadvance.Model.PokemonResponse;
import com.playing.pokedexadvance.Model.Users;
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
    TextView pokemonCatched;
    Button buttonCatched;
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

        ImageView arrow = findViewById(R.id.arrow);
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
        buttonCatched = findViewById(R.id.button_catch);
        pokemonCatched =findViewById(R.id.possible_catch);

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

        isCathed(pokemonFirebase.getName().toString());

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                catchPokemonForUser();
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void catchPokemonForUser() {

        lottieAnimationView.playAnimation();
        String username = firebaseUser.getUid().toString();
        pokemonFirebase.setId_user(username);
        final String pid = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/").getReference().child("Pokemon").push().getKey();

        String type2;

        if (!pokemonFirebase.getType2().equals("") || pokemonFirebase.getType2() != null) {
            type2 = pokemonFirebase.getType2();
        } else {
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

        productsRef.child(pid).updateChildren(pokemonMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(pokemonCatched, "Pokemon has been catched", Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(getResources().getColor(R.color.colorFondo));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                    snackbar.show();

                    FirebaseDatabase.getInstance(FINSTANCE).getReference().child("Users").child(firebaseUser.getUid().toString()).child("Catched").child(pid).setValue(true);

                    addCatchedQty();


                } else {
                    Toast.makeText(getApplicationContext(), "Error in catching", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addCatchedQty() {

        DatabaseReference userFIre = FirebaseDatabase.getInstance(FINSTANCE).getReference().child("Users").child(firebaseUser.getUid().toString());

        userFIre.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Users auxiliar = dataSnapshot.getValue(Users.class);

                    int qty_pokemon = auxiliar.getQty_pokemon() + 1;
                    FirebaseDatabase.getInstance(FINSTANCE).getReference().child("Users").child(firebaseUser.getUid().toString()).child("qty_pokemon").setValue(qty_pokemon);
                    lottieAnimationView.cancelAnimation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void isCathed(final String pid) {

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/").getReference().child("Pokemon");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot each : dataSnapshot.getChildren()){
                        PokemonInfoFirebase aux = each.getValue(PokemonInfoFirebase.class);
                        if(aux.getName().equals(pid)){
                            pokemonCatched.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            buttonCatched.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                        String type1 = data.getTypes().get(0).getType().getName().toString();
                        String type2 = data.getTypes().get(1).getType().getName().toString();

                        ChipType1.setText(data.getTypes().get(0).getType().getName());
                        ChipType2.setText(data.getTypes().get(1).getType().getName());
                        setIcon(type1, ChipType1);
                        setIcon(type2, ChipType2);

                        pokemonFirebase.setType1(data.getTypes().get(0).getType().getName().toString());
                        pokemonFirebase.setType2(data.getTypes().get(1).getType().getName().toString());

                    } else {

                        String type1 = data.getTypes().get(0).getType().getName().toString();

                        ChipType1.setText(data.getTypes().get(0).getType().getName());
                        setIcon(type1, ChipType1);

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

    private void setIcon(String type, Chip chipType){

        if (type.equals("water")){

            Drawable img = getApplicationContext().getResources().getDrawable(water);
            chipType.setChipIcon(img);
        }

        if (type.equals("fire")){

            Drawable img = getApplicationContext().getResources().getDrawable(fire);
            chipType.setChipIcon(img);
        }

        if (type.equals("electric")){

            Drawable img = getApplicationContext().getResources().getDrawable(electric);
            chipType.setChipIcon(img);
        }
        if (type.equals("poison")){

            Drawable img = getApplicationContext().getResources().getDrawable(poison);
            chipType.setChipIcon(img);
        }
        if (type.equals("fighting")){

            Drawable img = getApplicationContext().getResources().getDrawable(fight);
            chipType.setChipIcon(img);
        }
        if (type.equals("rock")){

            Drawable img = getApplicationContext().getResources().getDrawable(rock);
            chipType.setChipIcon(img);
        }

        if (type.equals("normal")){

            Drawable img = getApplicationContext().getResources().getDrawable(normal);
            chipType.setChipIcon(img);
        }

        if (type.equals("grass")){

            Drawable img = getApplicationContext().getResources().getDrawable(plant_1);
            chipType.setChipIcon(img);
        }

        if (type.equals("flying")){

            Drawable img = getApplicationContext().getResources().getDrawable(flying);
            chipType.setChipIcon(img);
        }

        if (type.equals("bug")){
            Drawable img = getApplicationContext().getResources().getDrawable(bug);
            chipType.setChipIcon(img);
        }


    }

}