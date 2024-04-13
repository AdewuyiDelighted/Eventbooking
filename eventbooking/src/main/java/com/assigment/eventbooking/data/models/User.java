package com.assigment.eventbooking.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
@Validated
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(min = 3, message = "Inputted name too short")
    @Size(max = 100, message = "Inputted name too long")
    private String name;

    private String password;

    @Email()
    private String email;

}
