package org.example.dto;

public class TaiKhoanDTO {
    private String username;
    private String password;
    private String position;

    public enum Role{
        ADMIN,
        STAFF,
    }

    public TaiKhoanDTO() {
    }

    public TaiKhoanDTO(String username, String password, String position) {
        this.username = username;
        this.password = password;
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPosition() {
        return position;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Role getRole(){
        if (position != null && position.equalsIgnoreCase("Quản lí"))
            return Role.ADMIN;
        return Role.STAFF;
    }
}