package com.lucasgirard.projetdevmobile;

public class SingletonFirebase {
    private static SingletonFirebase instance = null;
    private String userId;

    private SingletonFirebase() {
    }

    public static SingletonFirebase getInstance() {
        if (instance == null) {
            instance = new SingletonFirebase();
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
