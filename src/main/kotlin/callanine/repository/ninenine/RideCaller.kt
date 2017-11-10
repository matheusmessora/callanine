package callanine.repository.ninenine

import callanine.service.callanine.Prospect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RideCaller(private val mapper: ObjectMapper) {

    private val log = LoggerFactory.getLogger(javaClass)

    private val url = "https://sandbox-api.corp.99taxis.com/v1/ride"

    fun createRide(prospect: Prospect): RideResponse? {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

        val body = RideRequest(
                phoneNumber = prospect.phoneNumber ?: "11999114437",
                from = prospect.from ?: LocationRideRequest(),
                to = prospect.to,
                categoryID = prospect.category ?: "regular-taxi")
        val jsonBody = mapper.writeValueAsString(body)
        log.info("m=RIDE_REQUEST,body=$jsonBody")

        val response = Request.Post(url)
                .addHeader("x-api-key", "a2658c177976af326e5c601f2f3bed092ba4659e8ab0ebfcfe18120193fac27a")
                .bodyString(jsonBody, ContentType.APPLICATION_JSON)
                .execute().returnResponse()

        val statusCode = response.statusLine.statusCode
        if(statusCode == 200){
            return mapper.readValue(response.entity.content.bufferedReader(), RideResponse::class.java)
        }
        return null
    }
}

data class RideResponse(val rideID: Long)

data class RideRequest(val employeeID: Long = 239283,
                       val phoneNumber: String = "11999999999",
                       val from: LocationRideRequest = LocationRideRequest(),
                       val to: LocationRideRequest? = null,
                       val costCenterID: Long = 92,
                       val categoryID: String = "regular-taxi",
                       val projectID: String = "9155")

data class LocationRideRequest(val latitude: Double = -23.606038,
                               val longitude: Double = -46.691875,
                               val street: String = "Rua Paulo Orozimbo",
                               val number: String = "1100")
