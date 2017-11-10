package callanine

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.context.annotation.Bean



@EnableAutoConfiguration
@Configuration
@SpringBootApplication
class ApplicationBoot

fun main(args: Array<String>) {
    SpringApplication.run(ApplicationBoot::class.java, *args)
}