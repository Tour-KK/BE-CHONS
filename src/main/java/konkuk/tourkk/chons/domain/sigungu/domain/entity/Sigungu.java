package konkuk.tourkk.chons.domain.sigungu.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(nullable = false)
    Long areaCode;

    @Builder
    private Sigungu(Long code, String name, Long areaCode) {
        this.code = code;
        this.name = name;
        this.areaCode = areaCode;
    }
}
