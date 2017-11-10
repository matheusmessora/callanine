package callanine.endpoint.facebook

import callanine.service.FBMessageSend
import callanine.service.FBMessageSender
import callanine.service.MessageProcessor
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.*
import org.springframework.web.filter.CommonsRequestLoggingFilter

@RestController
@RequestMapping("/facebook/messenger")
class ChatController(val facebook: FBMessageSender,
                     val messageProcessor: MessageProcessor) {

    val log = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun validate(@RequestParam("hub.challenge") challenge: String) = challenge

    @PostMapping
    fun webhook(@RequestBody body: FBNotificationRequest): FBNotificationRequest {
        log.info("request=$body")
        body.entry
                .flatMap { it.messaging }
                .forEach {
                    if(!messageProcessor.read(it)){
                        facebook.answer(it.sender, FBMessageSend(it.message.text ?: "empty-text"))
                    }
                }
        return body
    }


}