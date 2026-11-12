package bg.softuni.pathfinder.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_pictures")
public class PictureEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private RouteEntity route;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
