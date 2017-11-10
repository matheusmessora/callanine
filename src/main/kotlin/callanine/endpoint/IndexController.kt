package callanine.endpoint

import callanine.endpoint.facebook.FBNotificationRequest
import callanine.endpoint.ninenine.WebhookRequest
import callanine.service.FBMessageSend
import callanine.service.FBMessageSender
import callanine.service.ninenine.Rides
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
class IndexController(val rides: Rides,
                      val facebook: FBMessageSender) {

    val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun ninenine(@RequestBody body: WebhookRequest): WebhookRequest {
        log.info("request=$body")
//        rides.updateRide(body.data.ride.id, body.data.ride.status)

        val newRide = rides.get(body.data.ride.id)
        if (newRide != null) {
            val status = body.data.ride.status
            if(status.equals("on-the-way", true)){
                facebook.answer(newRide.user, FBMessageSend("Achei seu motorista \uD83D\uDE96 e ele está a caminho. ${body.data.ride.driver?.name} placa ${body.data.ride.vehicle?.plate}"))
            }
            if(status.equals("in-progress", true)){
                facebook.answer(newRide.user, FBMessageSend("Não esquece de botar o cinto viu! $status"))
            }
            if(status.equals("finished", true)){
                facebook.answer(newRide.user, FBMessageSend("A corrida acabou. #voude99corp $status"))
            }
        }
        return body
    }

    @GetMapping("/")
    fun index() = "This is home!"
}