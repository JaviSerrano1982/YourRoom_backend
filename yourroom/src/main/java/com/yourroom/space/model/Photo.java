package com.yourroom.space.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "photos")
public class Photo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(name = "is_primary", nullable = false)
    private boolean primaryPhoto = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Getters/Setters
    public Long getId() { return id; }
    public Space getSpace() { return space; }
    public void setSpace(Space space) { this.space = space; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public boolean isPrimaryPhoto() { return primaryPhoto; }
    public void setPrimaryPhoto(boolean primaryPhoto) { this.primaryPhoto = primaryPhoto; }
    public Instant getCreatedAt() { return createdAt; }
}
