package konkuk.tourkk.chons.domain.review.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_TB")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    @Column(nullable = false)
    private Integer star;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Long houseId;

    @ElementCollection
    private List<String> photos;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private Review(String content, Integer star, Long userId, Long houseId, String userName, List<String> photos) {
        this.content = content;
        this.star = star;
        this.userId = userId;
        this.houseId = houseId;
        this.userName = userName;
        this.photos = photos;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeStar(Integer star) {
        this.star = star;
    }
}
