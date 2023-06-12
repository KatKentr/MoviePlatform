package org.acme.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.acme.model.Movie;

import java.util.Set;

public class SignupDto {

    @NotNull
    @Size(min=2, message="Name should have at least 2 characters")
    private String username;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull
    @Size(min=4, message="Password should be at least 4 characters")
    @NotEmpty(message = "Password cannot be empty")
    private String password;


    @NotNull
    @NotEmpty(message = "role cannot be empty, either ADMIN or USER")           //TODO: add control for this
    private String role;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
