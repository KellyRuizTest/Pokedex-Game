package com.playing.pokedexadvance.Model;

import java.util.List;

public class PokemonResponse {

    private Integer count;
    private String next;
    private Object previous;
    private List<Pokemon> results;

    public PokemonResponse(Integer count, String next, Object previous, List<Pokemon> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public Integer getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public Object getPrevious() {
        return previous;
    }

    public List<Pokemon> getResults() {
        return results;
    }
}



