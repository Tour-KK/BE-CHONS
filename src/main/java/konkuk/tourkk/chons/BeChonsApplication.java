package konkuk.tourkk.chons;

import konkuk.tourkk.chons.domain.festival.application.FestivalService;
import konkuk.tourkk.chons.domain.festival.application.RegionSigunguService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class BeChonsApplication {

    //develop용
    private final RegionSigunguService regionSigunguService;

    public static void main(String[] args) {
        SpringApplication.run(BeChonsApplication.class, args);
    }

    // develop용
    @Bean
    public CommandLineRunner commandLineRunner() {
        regionSigunguService.makeRegions();
        return args -> {};
    }
}
