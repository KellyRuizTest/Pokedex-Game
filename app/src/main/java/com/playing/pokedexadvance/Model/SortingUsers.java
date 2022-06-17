package com.playing.pokedexadvance.Model;

import java.util.Comparator;

public class SortingUsers implements Comparator<Users> {


    @Override
    public int compare(Users users, Users t1) {

        return (int) (users.gettingRankingNro() - t1.gettingRankingNro());

    }
}
