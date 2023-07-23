package com.playing.pokedexadvance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.playing.pokedexadvance.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HOME";
    private FirebaseUser firebaseUser;
    private List<PokemonInfoFirebase> listOwnPokemons;
    private PokemonOwnListAdapter adapter;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        listOwnPokemons = new ArrayList<>();

        // building RecyclerView & adapters using binding
        adapter = new PokemonOwnListAdapter(listOwnPokemons);
        binding.recyclerviewOwnPokemon.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.recyclerviewOwnPokemon.setAdapter(adapter);

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
                        startActivity(new Intent(getApplicationContext(), RankingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
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
                    binding.moneyAvailable.setText(money);
                    Picasso.get().load(aux.getImage()).fit().centerCrop().into(binding.profileImage);
                    binding.iconUser.setText(""+aux.getCoin()+"");
                    binding.idProfileFrag.setText(aux.getUsername());
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
                    binding.pokemonQty.setText(aux);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}