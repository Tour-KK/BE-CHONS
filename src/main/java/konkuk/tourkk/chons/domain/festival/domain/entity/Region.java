package konkuk.tourkk.chons.domain.festival.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "region_TB")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long code;

    @Column(nullable = false)
    String name;

    @Builder
    private Region(Long code, String name) {
        this.code = code;
        this.name = name;
    }
}
