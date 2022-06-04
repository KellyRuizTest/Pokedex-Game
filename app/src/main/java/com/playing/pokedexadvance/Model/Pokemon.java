package com.playing.pokedexadvance.Model;

import static java.lang.Integer.parseInt;

public class Pokemon {

    private String name;
    private String url;
    private String urlImage;
    private String urlInfo;
    private Integer id;

    public Pokemon(String name, String url) {
        this.name = name;
        this.url = url;
        this.urlImage = null;
        this.urlInfo = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlImage(String urlImage){
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setUrlInfo(String url_info){
        this.urlInfo = url_info;

    }

    public String getUrlInfo(){
        return urlInfo;
    }

    public String bildingImage(String url){

        String URL_BASE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";
        String URL_INFO = "https://pokeapi.co/api/v2/pokemon/";

        int total = url.length()-1;

        String aux = url.substring(34, total);
        String URL_BASE_COMPLETE = URL_BASE+aux+".png";

        int aux_id = parseInt(aux);

        String URL_INFO_COMPLETE = URL_INFO+aux;

        setId(aux_id);
        setUrlInfo(URL_INFO_COMPLETE);

        return URL_BASE_COMPLETE;

    }



}
