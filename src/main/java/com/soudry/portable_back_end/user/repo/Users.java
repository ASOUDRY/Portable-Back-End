package com.soudry.portable_back_end.user.repo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.uuid.Generators;
import com.soudry.portable_back_end.auth.tokens.RefreshToken;
import jakarta.persistence.CascadeType;
import com.soudry.portable_back_end.user.AccountRole;

@Entity
public class Users {

    @Id
    private String id;
    private String name;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private AccountRole role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();
    public Users() { }

    public Users(String name, String password, AccountRole role, String email) {
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

    public AccountRole getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
      public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }
}