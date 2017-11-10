package callanine.service.callanine

import callanine.endpoint.facebook.FBUser
import callanine.repository.ninenine.LocationRideRequest
import org.springframework.stereotype.Service

@Service
class Prospects {

    private val cache: MutableMap<String, Prospect> = HashMap()

    fun get(sender: FBUser): Prospect? {
        return cache.get(sender.id)
    }

    fun insert(sender: FBUser) {
        cache.put(sender.id, Prospect(sender.id))
    }

    fun update(prospect: Prospect) {
        cache.put(prospect.senderID, prospect)
    }

//    fun get(senderID: String): String {
//        return cache.get(rideID)
//    }
//
//    fun create(rideID: String, user: FBUser) {
//        cache.put(rideID, Ride(user, "WAITING_DRIVERS_ANSWERS"))
//    }

}

data class Prospect(val senderID: String,
                    val phoneNumber: String? = null,
                    val from: LocationRideRequest? = null,
                    val to: LocationRideRequest? = null,
                    val category: String? = null,
                    val step: String = "nafaixa"
                    )