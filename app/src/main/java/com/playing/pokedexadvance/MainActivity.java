package com.playing.pokedexadvance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.playing.pokedexadvance.Adapter.PokemonListAdapter;
import com.playing.pokedexadvance.Model.Pokemon;
import com.playing.pokedexadvance.Model.PokemonResponse;
import com.playing.pokedexadvance.Retrofit.IPokemon;
import com.playing.pokedexadvance.Retrofit.RetrofitClient;
import com.playing.pokedexadvance.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PokemonListAdapter adapter;
    private List<Pokemon> pokemonList = new ArrayList<>();
    //private ProgressBar progressBar;

    private TextView pokecarga;

    public IPokemon iPokemon;
    public CompositeDisposable compositeDisposable;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.marketplace);

        compositeDisposable = new CompositeDisposable();

        //recyclerView = findViewById(R.id.pokemon_recycler);

        // Build Adapter
        adapter = new PokemonListAdapter(pokemonList);
        //setOnClickListener();
        binding.pokemonRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.pokemonRecycler.setAdapter(adapter);

        adapter.setOnClickListener(new PokemonListAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Pokemon model) {
                Intent intent = new Intent(MainActivity.this, PokemonInfoActivity.class);
                intent.putExtra("name", pokemonList.get(position).getName());
                intent.putExtra("url", pokemonList.get(position).getUrl());
                intent.putExtra("url_image", pokemonList.get(position).getUrlImage());
                intent.putExtra("url_info", pokemonList.get(position).getUrlInfo());

                startActivity(intent);
            }
        });

        fetchData();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.marketplace:
                        return true;

                    case R.id.pokerank:
                        startActivity(new Intent(getApplicationContext(), RankingActivity.class));
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

                    PokemonResponse data = response.body(); // has a lot fields but we are interesed in List "Results"

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}