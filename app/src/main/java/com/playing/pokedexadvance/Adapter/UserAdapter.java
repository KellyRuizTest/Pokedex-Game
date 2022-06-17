package com.playing.pokedexadvance.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playing.pokedexadvance.Model.Users;
import com.playing.pokedexadvance.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<Users> usersList = new ArrayList<>();

    public UserAdapter(List<Users> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rankin_users_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ViewHolder holder, int position) {

        holder.name_txt.setText(usersList.get(position).getName());
        Picasso.get().load(usersList.get(position).getImage()).into(holder.profile_img);

        holder.ranking_txt.setText(String.valueOf(usersList.get(position).gettingRankingNro()));
        holder.catched_qty.setText(String.valueOf(usersList.get(position).getQty_pokemon()));
        holder.ranking_button.setText(String.valueOf(usersList.get(position).gettingRankingNro()));

    }

   /* private void settingCatched(final TextView ranking_txt, final TextView catched_qty, final Users aux) {

        DatabaseReference userFirebase = FirebaseDatabase.getInstance("https://pokedex-app-71958-default-rtdb.firebaseio.com/")
                .getReference().child("Users")
                .child(aux.getId().toString()).child("Catched");

        userFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String qty_aux = String.valueOf(dataSnapshot.getChildrenCount());
                    catched_qty.setText(qty_aux);

                    System.out.println("Qty POKEMON: "+qty_aux);
                    aux.setQty_pokemon(Integer.parseInt(qty_aux));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_img;
        public TextView name_txt;
        public TextView ranking_txt;
        public ImageView pokemon_img;
        public TextView catched_qty;
        public Button ranking_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img = itemView.findViewById(R.id.show_user_image);
            name_txt = itemView.findViewById(R.id.show_user_name);
            ranking_txt = itemView.findViewById(R.id.show_user_ranking);
            pokemon_img= itemView.findViewById(R.id.icon_poke_qty);
            catched_qty = itemView.findViewById(R.id.text_poke_qty);
            ranking_button = itemView.findViewById(R.id.button_user);
        }
    }
}
