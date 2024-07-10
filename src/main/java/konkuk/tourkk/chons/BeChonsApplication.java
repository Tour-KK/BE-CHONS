package konkuk.tourkk.chons;

import konkuk.tourkk.chons.domain.areasigungu.application.service.AreaSigunguService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class BeChonsApplication {
    //develop용
//    private final AreaSigunguService areaSigunguService;
    public static void main(String[] args) {
        SpringApplication.run(BeChonsApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner() {
//        areaSigunguService.saveAreas();
//        return args -> {
//        };
//    }
}
