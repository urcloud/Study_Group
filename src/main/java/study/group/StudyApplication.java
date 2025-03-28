package study.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
//@Scheduled 어노테이션을 사용할 수 있도록 설정
@SpringBootApplication
//@ComponentScan, @EnableAutoConfiguration, @Configuration을 포함, 자동으로 Bean 스캔하고 설정을 적용
//Bean은 Spring IoC 컨테이너에서 자동으로 관리되는 객체
public class StudyApplication {
  public static void main(String[] args) {
    SpringApplication.run(StudyApplication.class, args);
    //Spring Boot 애플리케이션을 시작하는 코드
  }
}