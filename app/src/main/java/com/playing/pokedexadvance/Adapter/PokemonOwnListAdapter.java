package com.playing.pokedexadvance.Adapter;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PokemonOwnListAdapter extends RecyclerView.Adapter<PokemonOwnListAdapter.ViewHolder> {

    private List<PokemonInfoFirebase> pokemonList = new ArrayList<>();
    private Context context;

    public PokemonOwnListAdapter(List<PokemonInfoFirebase> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_won_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load(pokemonList.get(position).getUrl_image()).into(holder.imageView);
        //Glide.with(context).load(pokemonList.get(position).ge)
        holder.textView.setText(pokemonList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
    //private PokemonOwnListAdapter.RecyclerViewClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_own_pokemon);
            textView = itemView.findViewById(R.id.name_own_pokemon);

        }
    }
}
