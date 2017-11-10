package callanine.service

import callanine.endpoint.facebook.FBMessaging
import callanine.repository.ninenine.LocationRideRequest
import callanine.repository.ninenine.RideCaller
import callanine.service.callanine.Prospects
import callanine.service.ninenine.Rides
import org.springframework.stereotype.Service

@Service
class MessageProcessor(val rideCaller: RideCaller,
                       val facebook: FBMessageSender,
                       val prospects: Prospects,
                       val rides: Rides) {

    fun read(message: FBMessaging): Boolean {
        if (equals(message, "NaFaixa")) {
            prospects.insert(message.sender)
            return true
        }

        val prospect = prospects.get(message.sender)
        if(prospect != null) {
            val extractedCellphone = extractNumber(message)
            if(extractedCellphone != -1L && prospect.step == "nafaixa") {
                prospects.update(prospect.copy(step = "phone", phoneNumber = extractedCellphone.toString()))
                facebook.answer(message.sender, FBMessageSend("Opa obrigada!. Agora preciso saber seu local de partida.", listOf(FBQuickReplySend())))
                return true
            }

            val attachments = message.message.attachments?.firstOrNull()
            if(attachments?.type.equals("location", true)) {
                    attachments?.payload?.coordinates?.let {
                        if(prospect.step == "phone") {
                            prospects.update(prospect.copy(from = LocationRideRequest(it.lat, it.long), step = "from"))
                            facebook.answer(message.sender, FBMessageSend("Ótimo!. Agora preciso saber para onde você quer ir.", listOf(FBQuickReplySend())))
                            return true
                        }else {
                            prospects.update(prospect.copy(to = LocationRideRequest(it.lat, it.long), step = "to"))
                            val replies = listOf(FBQuickReplySend("text", "99POP", "pop99"),
                                    FBQuickReplySend("text", "TAXI", "regular-taxi"))
                            facebook.answer(message.sender, FBMessageSend("Maravilha, agora escolha o seu carro", replies))
                            return true
                        }
                    }
            }

            message.message.quick_reply?.let {
                if(prospect.step == "to") {
                    val callRideProspect = prospect.copy(category = it.payload, step = "callride")
                    prospects.update(callRideProspect)
                    val createRide = rideCaller.createRide(callRideProspect)
                    if (createRide != null){
                        facebook.answer(recipient = message.sender, message = FBMessageSend("Seu carro está a caminho!! ${createRide.rideID}"))
                        rides.create(createRide.rideID.toString(), message.sender)
                        return true
                    }else {
                        facebook.answer(recipient = message.sender, message = FBMessageSend("Não consegui chamar seu carro \uD83D\uDE22"))
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun extractNumber(message: FBMessaging): Long {
        val textMessage = message.message.text ?: ""
        if(textMessage.length == 11){
            return textMessage.toLongOrNull() ?: -1L
        }
        return -1L
    }


    private fun equals(message: FBMessaging, checker: String) = message.message.text.equals(checker, true)
}