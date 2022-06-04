package com.playing.pokedexadvance.Retrofit;

import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //retrofit is ourInstance
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";
    private static Retrofit retrofit = null;

    //getClient is something like get
    public static IPokemon getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(IPokemon.class);
    }

}
