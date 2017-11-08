package callanine

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

@EnableAutoConfiguration
@Configuration
@SpringBootApplication
class ApplicationBoot

fun main(args: Array<String>) {
    SpringApplication.run(ApplicationBoot::class.java, *args)
}