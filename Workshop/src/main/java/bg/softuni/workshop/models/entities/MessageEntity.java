package bg.softuni.workshop.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_messages")
public class MessageEntity extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String textContent;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private UserEntity recipient;
}
