package com.example.project.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "recoverCode")
public class RecoverCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;

    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;


    public boolean isValid(){
        return LocalDateTime.now().isBefore(expiresAt);
    }
}
