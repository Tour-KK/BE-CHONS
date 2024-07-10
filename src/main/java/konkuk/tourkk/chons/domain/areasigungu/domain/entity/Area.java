package konkuk.tourkk.chons.domain.areasigungu.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "area_TB")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long code;

    @Column(nullable = false)
    String name;

    @Builder
    private Area(Long code, String name) {
        this.code = code;
        this.name = name;
    }
}
