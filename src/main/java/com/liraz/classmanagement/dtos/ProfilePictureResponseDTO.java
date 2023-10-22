package com.liraz.classmanagement.dtos;

public class ProfilePictureResponseDTO {
    private String login;
    private byte[] image;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
