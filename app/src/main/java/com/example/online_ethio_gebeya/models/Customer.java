package com.example.online_ethio_gebeya.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

// for session and registration
@JsonRootName(value = "customer")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Customer {
    @JsonProperty("id")
    private long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("password_confirmation")
    private String passwordConfirmation;

    @JsonProperty("current_password")
    private String currentPassword;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    //  required for password change
    @JsonProperty("reset_password_token")
    private String resetPasswordToken;


    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
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

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @JsonIgnore
    public Customer setCredentials(String email, String password) {
        this.email = email;
        this.password = password;
        return this;
    }

    @JsonIgnore
    public CharSequence getFullName() {
        return firstName.concat(" ").concat(lastName);
    }

    @JsonIgnore
    public Customer setFullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
