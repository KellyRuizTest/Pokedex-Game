package com.playing.pokedexadvance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playing.pokedexadvance.Adapter.PokemonOwnListAdapter;
import com.playing.pokedexadvance.Adapter.UserAdapter;
import com.playing.pokedexadvance.Model.PokemonInfoFirebase;
import com.playing.pokedexadvance.Model.SortingUsers;
import com.playing.pokedexadvance.Model.Users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ranking extends AppCompatActivity {

    private List<Users> listUsers = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    private Button rankingUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);

        recyclerView = findViewById(R.id.pokemon_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new UserAdapter(listUsers);
        recyclerView.setAdapter(adapter);

        rankingUser = findViewById(R.id.button_user);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.pokerank);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.marketplace:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                   /* case R.id.pokeicon:
                        startActivity(new Intent(getApplicationContext(), PokeIcon.class));
                        overridePendingTransition(0,0);
                        return true;*/

                    case R.id.pokerank:
                        return true;
                }
                return false;
            }
        });


    fetchUser();


    }

    private void fetchUser() {

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/").getReference().child("Users");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listUsers.clear();
                    for (DataSnapshot each : dataSnapshot.getChildren()){

                        Users aux = each.getValue(Users.class);
                        //if (aux.getUsername().equals(firebaseUser.getUid().toString())){
                        listUsers.add(aux);
                        // }

                    }
                    Collections.sort(listUsers, Users.comparatorByRanking);
                    adapter.notifyDataSetChanged();
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();

    }

}