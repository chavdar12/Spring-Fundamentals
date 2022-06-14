package bg.softuni.workshop.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_comments")
public class CommentEntity extends BaseEntity {

    @Column(nullable = false)
    private Boolean approved;

    @Column(columnDefinition = "TEXT")
    private String textContent;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @ManyToOne
    private RouteEntity route;

    @ManyToOne
    private UserEntity author;
}
