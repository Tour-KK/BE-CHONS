package konkuk.tourkk.chons.domain.festival.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "festival_TB")
@Getter
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String address;

    String detailAddress;

    @Column(nullable = false)
    Long contentId;

    @Column(nullable = false)
    String startDate;

    @Column(nullable = false)
    String endDate;

    String imageUrl;

    String posX;

    String posY;

    String tel;

    String title;

    @Builder
    private Festival(String address, String detailAddress, Long contentId, String startDate, String endDate, String imageUrl, String posX, String posY, String tel, String title) {
        this.address = address;
        this.detailAddress = detailAddress;
        this.contentId = contentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
        this.posX = posX;
        this.posY = posY;
        this.tel = tel;
        this.title = title;
    }
}
