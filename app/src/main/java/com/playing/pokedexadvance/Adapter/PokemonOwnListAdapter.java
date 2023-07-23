package com.playing.pokedexadvance.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.playing.pokedexadvance.Model.Pokemon;
import com.playing.pokedexadvance.Model.PokemonInfoFirebase;
import com.playing.pokedexadvance.R;
import com.playing.pokedexadvance.databinding.PokemonWonLayoutBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PokemonOwnListAdapter extends RecyclerView.Adapter<PokemonOwnListAdapter.ViewHolder> {

    private List<PokemonInfoFirebase> pokemonList;

    public PokemonOwnListAdapter(List<PokemonInfoFirebase> pokemonList) {
        this.pokemonList = pokemonList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PokemonWonLayoutBinding pokemonWonLayoutBinding = PokemonWonLayoutBinding.inflate(inflater, parent, false);
        Log.d("onCreateViewHolder", "done");
        return new ViewHolder(pokemonWonLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load(pokemonList.get(position).getUrl_image()).into(holder.recyclerWonBinding.imageOwnPokemon);
        holder.recyclerWonBinding.nameOwnPokemon.setText(pokemonList.get(position).getName());
        //holder.textView.setText(pokemonList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
    //private PokemonOwnListAdapter.RecyclerViewClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        PokemonWonLayoutBinding recyclerWonBinding;

        public ViewHolder(@NonNull PokemonWonLayoutBinding recyclerWonBinding) {
            super(recyclerWonBinding.getRoot());
            this.recyclerWonBinding = recyclerWonBinding;
        }

    }
}
