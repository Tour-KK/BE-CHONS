package konkuk.tourkk.chons.domain.festival.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sigungu_TB")
public class Sigungu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long code;

    @Column(nullable = false)
    String name;

    @Builder
    private Sigungu(Long code, String name) {
        this.code = code;
        this.name = name;
    }
}
