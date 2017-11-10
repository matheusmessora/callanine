package callanine.endpoint.ninenine

data class WebhookRequest(val event: EventMetadataResource, val data: EventDataResource)

data class EventDataResource(val ride: RideResource)

data class RideResource(val id: String, val status: String, val driver: DriverResource?, val vehicle: VehicleResource?)

data class DriverResource(val name: String, val phone: String)

data class VehicleResource(val model: String, val plate:String)

data class EventMetadataResource(val id: String, val notification: String)
