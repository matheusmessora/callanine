package callanine.endpoint

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {


    @RequestMapping("/")
    fun index() = "This is home!"
}