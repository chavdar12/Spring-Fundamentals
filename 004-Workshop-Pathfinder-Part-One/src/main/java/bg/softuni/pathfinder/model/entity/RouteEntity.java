package bg.softuni.pathfinder.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_routes")
public class RouteEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String gpxCoordinates;

    @Enumerated(EnumType.STRING)
    private Level level;

    private String videoUrl;

    @ManyToOne
    private UserEntity author;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<CategoryEntity> categories = new HashSet<>();

    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER)
    private List<PictureEntity> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "route")
    private List<CommentEntity> comments = new ArrayList<>();

    public String getName() {
        return name;
    }

    public RouteEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RouteEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getGpxCoordinates() {
        return gpxCoordinates;
    }

    public RouteEntity setGpxCoordinates(String gpxCoordinates) {
        this.gpxCoordinates = gpxCoordinates;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    public RouteEntity setLevel(Level level) {
        this.level = level;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public RouteEntity setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public RouteEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public List<PictureEntity> getPictures() {
        return pictures;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }
}
