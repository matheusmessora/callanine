package callanine.service.ninenine

import callanine.endpoint.facebook.FBUser
import org.springframework.stereotype.Service

@Service
class Rides{

    private val cachedRides: MutableMap<String, Ride> = HashMap()

    fun get(rideID: String): Ride? {
        return cachedRides.get(rideID)
    }

    fun create(rideID: String, user: FBUser) {
        cachedRides.put(rideID, Ride(user, "WAITING_DRIVERS_ANSWERS"))
    }

    fun updateRide(rideID: String, status: String): Ride? {
        val updatedRide = get(rideID)?.copy(status = status)

        return cachedRides.put(rideID, updatedRide ?: throw IllegalStateException("Ride not found, rideID=$rideID"))
    }

}

data class Ride(val user: FBUser, val status: String)