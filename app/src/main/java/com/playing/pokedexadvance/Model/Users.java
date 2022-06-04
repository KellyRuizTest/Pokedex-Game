package com.playing.pokedexadvance.Model;

public class Users {

    private String id;
    private String name;
    private String email;
    private String password;
    private String bio;
    private String image;
    private int coin;
    private float money;

    Users() {

        email = "No Email setted";
        password = "NoPW";
        name = "NoName";
        image = "#noURLimage";
        bio = "No Bio";
        coin = 0;
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
}
