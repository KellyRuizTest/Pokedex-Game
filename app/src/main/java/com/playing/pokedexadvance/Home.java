package com.playing.pokedexadvance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.playing.pokedexadvance.Adapter.PokemonOwnListAdapter;
import com.playing.pokedexadvance.Model.PokemonInfoFirebase;
import com.playing.pokedexadvance.Model.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import github.com.st235.lib_expandablebottombar.ExpandableBottomBar;
import github.com.st235.lib_expandablebottombar.ExpandableBottomBarMenuItem;

public class Home extends AppCompatActivity {

    private static final String TAG = "HOME";
    private ImageView logouff;
    private FirebaseUser firebaseUser;
    private TextView show_money;
    private TextView bio;
    private ImageView profileImage;
    private TextView pokemonQty;
    private TextView iconQty;
    private TextView usernameProfile;
    private List<PokemonInfoFirebase> listOwnPokemons;

    private PokemonOwnListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);


        logouff = findViewById(R.id.logout_btn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        listOwnPokemons = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview_own_pokemon);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PokemonOwnListAdapter(listOwnPokemons);
        recyclerView.setAdapter(adapter);

        show_money = findViewById(R.id.money_available);

        profileImage = findViewById(R.id.profile_image);
        pokemonQty = findViewById(R.id.pokemon_qty);
        iconQty = findViewById(R.id.icon_user);
        usernameProfile = findViewById(R.id.id_profile_frag);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        return true;

                    case R.id.marketplace:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.pokerank:
                        startActivity(new Intent(getApplicationContext(), Ranking.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        logouff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        userCompleteInfo();
        userRetrieveQty();
        userRetrievePokemonInfo();


    }


    private void userRetrievePokemonInfo() {

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/").getReference().child("Pokemon");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listOwnPokemons.clear();
                    for (DataSnapshot each : dataSnapshot.getChildren()){

                        PokemonInfoFirebase aux = each.getValue(PokemonInfoFirebase.class);
                        if (aux.getId_user().toString().equals(firebaseUser.getUid().toString())){
                            listOwnPokemons.add(aux);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void userCompleteInfo() {

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/").getReference().child("Users").child(firebaseUser.getUid());
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Users aux = dataSnapshot.getValue(Users.class);

                    String money;
                    money = String.valueOf(aux.getName());
                    show_money.setText(money);
                    Picasso.get().load(aux.getImage()).fit().centerCrop().into(profileImage);
                    iconQty.setText(""+aux.getCoin()+"");
                    usernameProfile.setText(aux.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void userRetrieveQty(){
        DatabaseReference firebaseUserPokemon = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/").getReference().child("Users").child(firebaseUser.getUid()).child("Catched");
        firebaseUserPokemon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String aux = String.valueOf(dataSnapshot.getChildrenCount());
                    pokemonQty.setText(aux);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}