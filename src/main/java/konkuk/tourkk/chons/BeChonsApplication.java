package konkuk.tourkk.chons;

import konkuk.tourkk.chons.domain.festival.application.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class BeChonsApplication {

    private final FestivalService festivalService;

    public static void main(String[] args) {
        SpringApplication.run(BeChonsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> festivalService.makeDefaultData();
    }
}
