package com.hadinawa.leasing_management_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @Column(nullable = false)
    private String username;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private AppUserRole role;
}
