package info.oregonliquor.oregonliquorsearch

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

object OregonLiquorSearch {

    private final val logger = LoggerFactory.getLogger(OregonLiquorSearch::class.java)

    private val restTemplate = RestTemplate()

    private val OREGON_LIQUOR_SEARCH_HOST = "http://oregonliquorsearch.com"

    fun auth(url: String = "$OREGON_LIQUOR_SEARCH_HOST/servlet/WelcomeController", cookie: String = ""): String {
        val postData : MultiValueMap<String, String> = LinkedMultiValueMap()
        postData.add("btnSubmit", "Enter+Site")
        postData.add("selDay", "5")
        postData.add("selMonth", "4")
        postData.add("selYear", "1982")

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        httpHeaders.set("Cookie", cookie)
        val httpEntity = HttpEntity(postData, httpHeaders)

        val response = restTemplate.postForEntity(url, httpEntity, String::class.java)
        val setCookie = response.headers["Set-Cookie"]?.get(0)
        if (response.statusCode == HttpStatus.FOUND && response.headers.location != null) {
            return auth(response.headers.location.toString(), setCookie.orEmpty())
        }
        return setCookie.orEmpty()
    }

    fun request(path: String, cookie: String): Document {
        logger.debug("Requesting $path")
        return Jsoup
                .connect("$OREGON_LIQUOR_SEARCH_HOST$path")
                .header("Cookie", cookie)
                .get()
    }
}
