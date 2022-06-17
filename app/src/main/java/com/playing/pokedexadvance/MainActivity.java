package com.playing.pokedexadvance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playing.pokedexadvance.Adapter.PokemonListAdapter;
import com.playing.pokedexadvance.Model.Pokemon;
import com.playing.pokedexadvance.Model.PokemonResponse;
import com.playing.pokedexadvance.Model.Users;
import com.playing.pokedexadvance.Retrofit.IPokemon;
import com.playing.pokedexadvance.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PokemonListAdapter adapter;
    private List<Pokemon> pokemonList = new ArrayList<>();
    private PokemonListAdapter.RecyclerViewClickListener listener;
    //private ProgressBar progressBar;

    private TextView pokecarga;

    public IPokemon iPokemon;
    public CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.marketplace);

        compositeDisposable = new CompositeDisposable();

        recyclerView = findViewById(R.id.pokemon_recycler);
        setOnClickListener();
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PokemonListAdapter(pokemonList, listener);
        recyclerView.setAdapter(adapter);


        fetchData();



       // adapter = new PokemonListAdapter(getApplicationContext(), );
        //recyclerView.setAdapter(adapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.marketplace:
                        return true;

                    case R.id.pokerank:
                        startActivity(new Intent(getApplicationContext(), Ranking.class));
                        overridePendingTransition(0,0);
                        return true;

                   /* case R.id.pokeicon:
                        startActivity(new Intent(getApplicationContext(), PokeIcon.class));
                        overridePendingTransition(0,0);
                        return true;*/
                }

                return false;
            }
        });

    }


    private void fetchData() {
      //  progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getClient().getPokemon().enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null){

                    PokemonResponse data = response.body(); // has a lot fiel but we are interesed in List "Results"

                    for (int i = 0; i < data.getResults().size(); i++){

                        String url_poke = data.getResults().get(i).getUrl();
                        String name_poke = data.getResults().get(i).getName();
                        Pokemon aux = new Pokemon(name_poke, url_poke);

                        String ImageUrl = aux.bildingImage(data.getResults().get(i).getUrl());
                        aux.setUrlImage(ImageUrl);

                        pokemonList.add(aux);

                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setOnClickListener() {
        listener =  new PokemonListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), PokemonInfoActivity.class);
                intent.putExtra("name", pokemonList.get(position).getName());
                intent.putExtra("url", pokemonList.get(position).getUrl());
                intent.putExtra("url_image", pokemonList.get(position).getUrlImage());
                intent.putExtra("url_info", pokemonList.get(position).getUrlInfo());

                startActivity(intent);
            }
        };

    }


}