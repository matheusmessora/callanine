package callanine.endpoint.facebook

import callanine.service.FBQuickReplySend

data class FBNotificationRequest(val entry: List<FBEntry>, val teste: Boolean?)

data class FBEntry(val time: Long, val messaging: List<FBMessaging>)

data class FBMessaging(val message: FBMessage, val sender: FBUser, val recipient: FBUser, val timestamp: Long)

data class FBMessage(val mid: String, val text: String?, val attachments: List<FBMessageAttachments>?, val quick_reply: FBQuickReply?)

data class FBQuickReply(val payload: String)

data class FBMessageAttachments(val type: String, val payload: FBMessagePayload?)

data class FBMessagePayload(val coordinates: FBCoordinates?)

data class FBCoordinates(val lat: Double, val long: Double)

data class FBUser(val id: String)