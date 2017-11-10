package callanine.service

import callanine.endpoint.facebook.FBUser
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FBMessageSender(val mapper: ObjectMapper) {

    val log = LoggerFactory.getLogger(javaClass)

    private val url = "https://graph.facebook.com/v2.6/me/messages?access_token=EAAKGO0NssAUBAAz5uqMs0NHmuSmAInsv6IaHljq9LQhWedyLjSDbyCHgV3QkV2T1Pd0WqurtRCS4xqOwwMjyDFgoCjf8xZBDSAtDfUnRkoFCyXMqkYjlyQyw3zVVx0XsaJ5UiR4KTE0jCoOVELlqkONLv0w9LTqqUYZCxaAQZDZD"

    fun answer(recipient: FBUser, message: FBMessageSend) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

        val body = FBMessages(recipient = recipient, message = message)
        val jsonBody = mapper.writeValueAsString(body)
        log.info("m=FACEBOOK_REQUEST,body=$jsonBody")

        val response = Request.Post(url)
                .bodyString(jsonBody, ContentType.APPLICATION_JSON)
                .execute().returnResponse()

        val statusCode = response.statusLine.statusCode

        val responseString = response.entity.content.bufferedReader().use { it.readText() }

        log.info("m=FACEBOOK_RESPONSE,status=$statusCode,body=$responseString")
    }
}

data class FBMessageSend(val text: String, val quick_replies: List<FBQuickReplySend>? = null)

data class FBQuickReplySend(val content_type: String = "location", val title: String? = null, val payload: String? = null)

data class FBMessagesResponse(val recipient_id: String, val message_id: String)

class FBMessages(val message_type: String = "RESPONSE", val recipient: FBUser, val message: FBMessageSend)