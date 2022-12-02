package com.mat.modularservers.util;

public record Credentials(String username, String password) {
    public final static Credentials DEFAULT = new Credentials(null, null);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Credentials credentials)
            return credentials.username.equals(username) && credentials.password.equals(password);
        return false;
    }
}
