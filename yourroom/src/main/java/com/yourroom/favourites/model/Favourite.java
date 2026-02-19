package com.yourroom.favourites.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "favourites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "space_id"})
)
public class Favourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "space_id", nullable = false)
    private Long spaceId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Favourite() {}

    public Favourite(Long userId, Long spaceId) {
        this.userId = userId;
        this.spaceId = spaceId;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getSpaceId() { return spaceId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
