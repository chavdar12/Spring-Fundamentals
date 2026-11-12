package bg.softuni.pathfinder.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_messages")
public class MessageEntity extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String textContent;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private UserEntity recipient;
}
