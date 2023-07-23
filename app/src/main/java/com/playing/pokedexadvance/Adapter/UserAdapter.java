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
import com.playing.pokedexadvance.databinding.RankinUsersLayoutBinding;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<Users> usersList;

    public UserAdapter(List<Users> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RankinUsersLayoutBinding binding = RankinUsersLayoutBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ViewHolder holder, int position) {

        holder.rankingbinding.showUserName.setText(usersList.get(position).getName());
        Picasso.get().load(usersList.get(position).getImage())
                .resize(300,300).onlyScaleDown()
                .into(holder.rankingbinding.showUserImage);

        //holder.rankingbinding.showUserRanking.setText(String.valueOf(usersList.get(position).gettingRankingNro()));
        holder.rankingbinding.textPokeQty.setText(String.valueOf(usersList.get(position).getQty_pokemon()));
        holder.rankingbinding.buttonUser.setText(String.valueOf(usersList.get(position).gettingRankingNro()));

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        public ImageView profile_img;
//        public TextView name_txt;
//        public TextView ranking_txt;
//        public ImageView pokemon_img;
//        public TextView catched_qty;
//        public Button ranking_button;

        private RankinUsersLayoutBinding rankingbinding;

        public ViewHolder(@NonNull RankinUsersLayoutBinding binding) {

            super(binding.getRoot());
            this.rankingbinding = binding;

//            profile_img = itemView.findViewById(R.id.show_user_image);
//            name_txt = itemView.findViewById(R.id.show_user_name);
//            ranking_txt = itemView.findViewById(R.id.show_user_ranking);
//            pokemon_img= itemView.findViewById(R.id.icon_poke_qty);
//            catched_qty = itemView.findViewById(R.id.text_poke_qty);
//            ranking_button = itemView.findViewById(R.id.button_user);
        }
    }
}
