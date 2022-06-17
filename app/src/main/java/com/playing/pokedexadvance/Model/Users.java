package com.playing.pokedexadvance.Model;

import java.util.Comparator;

public class Users {

    private String id;
    private String name;
    private String email;
    private String password;
    private String bio;
    private String image;
    private int coin;
    private float money;
    private String username;
    private int qty_pokemon;

    public Users() {

        id = "Noid";
        name = "NoName";
        email = "No Email setted";
        password = "NoPW";
        bio = "No Bio";
        image = "#noURLimage";
        coin = 0;
        username = "noUSERNAME";
        qty_pokemon = 0;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getQty_pokemon() {
        return qty_pokemon;
    }

    public void setQty_pokemon(int qty_pokemon) {
        this.qty_pokemon = qty_pokemon;
    }

    public Users(String id, String name, String email, String password, String bio, String image, int icon) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.image = image;
        this.coin = icon;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public float gettingRankingNro(){

        return 1* this.money + (float) 1.3* this.coin + (float) 1.7* this.qty_pokemon;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", image='" + image + '\'' +
                ", coin=" + coin +
                ", money=" + money +
                ", username='" + username + '\'' +
                ", qty_pokemon=" + qty_pokemon +
                '}';
    }

    public static Comparator<Users> comparatorByRanking = new Comparator<Users>() {
        @Override
        public int compare(Users s1, Users s2) {

            int aux = (int) s2.gettingRankingNro() - (int) s1.gettingRankingNro();
            return aux;
        }
    };
}
