package com.playing.pokedexadvance.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.playing.pokedexadvance.Model.Pokemon;
import com.playing.pokedexadvance.R;
import com.playing.pokedexadvance.databinding.PokemonLayoutBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    private List<Pokemon> pokemonList;
    private OnClickListener onClickListener;

    public PokemonListAdapter(List<Pokemon> pokemonList)
    {
        this.pokemonList = pokemonList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PokemonLayoutBinding pokemonLayoutBinding = PokemonLayoutBinding.inflate(inflater, parent, false);
        Log.d("onCreateViewHolder", "done");
        return new ViewHolder(pokemonLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(pokemonList.get(holder.getAdapterPosition()).getUrlImage()).into(holder.pokemonLayoutBinding.imagePokemon);
        //Glide.with(context).load(pokemonList.get(position).ge)
        holder.pokemonLayoutBinding.namePokemon.setText(pokemonList.get(holder.getAdapterPosition()).getName());

        Pokemon model = pokemonList.get(position);

        holder.pokemonLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(holder.getAdapterPosition(), model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClick(int position, Pokemon model);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        PokemonLayoutBinding pokemonLayoutBinding;

        public ViewHolder(@NonNull PokemonLayoutBinding pokemonLayoutBinding) {
            super(pokemonLayoutBinding.getRoot());
            this.pokemonLayoutBinding = pokemonLayoutBinding;

        }

    }

}
