package com.FarmVibe.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_details")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long user_Id;

    @Column(name = "user_firstname")
    private String user_firstName;

    @Column(name = "user_lastname")
    private String user_lastName;

    @Column(name = "user_email")
    private String user_email;

    @Column(name = "mobile_number")
    private Long mobile_Number;

    @Column(name = "user_password")
    private String user_password;

    @Column(name = "profile_image_url")
    private String profileImageUrl;
}
