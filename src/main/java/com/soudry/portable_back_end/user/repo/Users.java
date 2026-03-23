package com.soudry.portable_back_end.user.repo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import com.fasterxml.uuid.Generators;

@Entity
public class Users {

    @Id
    private String id;
    private String name;
    private String password;
    private String email;
    private String role;

    public Users() { }

    public Users(String name, String password, String role, String email) {
        id = Generators.randomBasedGenerator().generate().toString();
        this.name = name;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

}