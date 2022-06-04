package com.playing.pokedexadvance.Adapter;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    private List<Pokemon> pokemonList = new ArrayList<>();
    private Context context;
    private RecyclerViewClickListener listener;

    public PokemonListAdapter(List<Pokemon> pokemonList, RecyclerViewClickListener listener)
    {
        this.pokemonList = pokemonList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(pokemonList.get(position).getUrlImage()).into(holder.imageView);
        //Glide.with(context).load(pokemonList.get(position).ge)
        holder.textView.setText(pokemonList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_pokemon);
            textView = itemView.findViewById(R.id.name_pokemon);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {

        void onClick(View v, int position);

    }


}
