
package com.playing.pokedexadvance.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OfficialArtwork {

    @SerializedName("front_default")
    @Expose
    private String frontDefault;

    public String getFrontDefault() {
        return frontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }

}
