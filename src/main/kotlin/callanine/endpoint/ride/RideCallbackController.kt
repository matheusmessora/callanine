package callanine.endpoint.ride

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RideCallbackController {

    @RequestMapping("ninenine")
    fun createUser(event: EventResource): EventResource {

        return event
    }
}

class EventResource {

}
