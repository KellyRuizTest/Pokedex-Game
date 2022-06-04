package com.playing.pokedexadvance.Retrofit;

import com.playing.pokedexadvance.Model.PokemonInfo;
import com.playing.pokedexadvance.Model.PokemonResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IPokemon {

    @GET("pokemon?limit=1200")
    Call<PokemonResponse> getPokemon();

    @GET
    Call<PokemonInfo> getPokemonInfo(
            @Url String url
    );
}
