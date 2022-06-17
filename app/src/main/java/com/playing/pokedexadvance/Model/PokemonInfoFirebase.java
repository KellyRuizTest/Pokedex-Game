package com.playing.pokedexadvance.Model;

public class PokemonInfoFirebase {

    private String name;
    private Integer id_pokemon;
    private String weight;
    private String height;
    private String type1;
    private String type2;
    private String url_image;
    private String url_info;
    private String hp;
    private String def;
    private String atack;
    private String speed;
    private String exp;
    private String id_user;
    private String pid;
    private Integer cost;

    public PokemonInfoFirebase(){
        this.name = null;
        this.id_pokemon = null;
        this.url_image = null;
        this.url_info = null;
    }

    public PokemonInfoFirebase(String name, Integer id_nro, String url_image, String url_info) {
        this.name = name;
        this.id_pokemon = id_nro;
        this.url_image = url_image;
        this.url_info = url_info;
    }

    public PokemonInfoFirebase(String name, String url_image, String url_info) {
        this.name = name;
        this.url_image = url_image;
        this.url_info = url_info;
        this.id_pokemon = 0;
        this.weight = null;
        this.height = null;
        this.type1 = null;
        this.type2 = null;
        this.hp = null;
        this.def = null;
        this.speed = null;
        this.exp = null;
        this.id_user = null;
        this.pid = null;
        this.cost = null;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId_pokemon() {
        return id_pokemon;
    }

    public void setId_pokemon(Integer id_pokemon) {
        this.id_pokemon = id_pokemon;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getUrl_info() {
        return url_info;
    }

    public void setUrl_info(String url_info) {
        this.url_info = url_info;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getAtack() {
        return atack;
    }

    public void setAtack(String atack) {
        this.atack = atack;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
