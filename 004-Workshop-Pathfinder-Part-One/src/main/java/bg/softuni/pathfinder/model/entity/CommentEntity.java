package bg.softuni.pathfinder.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_comments")
public class CommentEntity extends BaseEntity {

    @Column(nullable = false)
    private boolean approved;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String textContent;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private RouteEntity route;

    public boolean isApproved() {
        return approved;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getTextContent() {
        return textContent;
    }

    public UserEntity getAuthor() {
        return author;
    }
}
