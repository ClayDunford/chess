package model;

import com.google.gson.Gson;

public record UserData(String username, String password, String email) {
    public boolean validate() {
        return username != null && password != null;
    }

    public String toString() {return new Gson().toJson(this);
    }
}
