package com.maxkavun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Matches")
@Data
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "Player1", nullable = false)
    private Player player1;

    @ManyToOne
    @JoinColumn(name = "Player2" , nullable = false)
    private Player player2;

    @ManyToOne
    @JoinColumn(name = "Winner" , nullable = false)
    private Player winner;

    @PrePersist
    @PreUpdate
    private void validatePlayers() {
        if (player1.equals(player2)) {
            throw new IllegalArgumentException("Player1 and Player2 must be different!");
        }
    }
}
