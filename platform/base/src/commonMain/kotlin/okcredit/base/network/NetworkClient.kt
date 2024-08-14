package okcredit.base.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import okcredit.base.di.BaseUrl

suspend inline fun <reified Request, reified ResponseClazz> HttpClient.patch(
    baseUrl: BaseUrl,
    endPoint: String,
    requestBody: Request,
    queryParams: Map<String, Any> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): Response<ResponseClazz> {
    val apiResponse = this.patch(finalUrl(baseUrl, endPoint)) {
        contentType(ContentType.Application.Json)
        setBody(requestBody)
        headers.forEach { (key, value) ->
            this.headers[key] = value
        }
        if (queryParams.isNotEmpty()) {
            url {
                queryParams.forEach {
                    parameters.append(it.key, it.value.toString())
                }
            }
        }
    }

    return if (apiResponse.status.isSuccess()) {
        Response.success(
            body = apiResponse.body(),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    } else {
        Response.error(
            body = apiResponse.body(),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    }
}

suspend inline fun <reified Request, reified ResponseClazz> HttpClient.post(
    baseUrl: BaseUrl,
    endPoint: String,
    requestBody: Request,
    queryParams: Map<String, Any> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): Response<ResponseClazz> {
    val apiResponse = this.post(finalUrl(baseUrl, endPoint)) {
        contentType(ContentType.Application.Json)
        setBody(requestBody)
        headers.forEach { (key, value) ->
            this.headers[key] = value
        }
        if (queryParams.isNotEmpty()) {
            url {
                queryParams.forEach {
                    parameters.append(it.key, it.value.toString())
                }
            }
        }
    }

    return if (apiResponse.status.isSuccess()) {
        Response.success(
            body = apiResponse.body(),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    } else {
        Response.error(
            body = apiResponse.body(),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    }
}

suspend inline fun <reified Request, reified ResponseClazz> HttpClient.put(
    baseUrl: BaseUrl,
    endPoint: String,
    requestBody: Request,
    queryParams: Map<String, Any> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): Response<ResponseClazz> {
    val apiResponse = this.put(finalUrl(baseUrl, endPoint)) {
        contentType(ContentType.Application.Json)
        setBody(requestBody)
        headers.forEach { (key, value) ->
            this.headers[key] = value
        }
        if (queryParams.isNotEmpty()) {
            url {
                queryParams.forEach {
                    parameters.append(it.key, it.value.toString())
                }
            }
        }
    }

    return if (apiResponse.status.isSuccess()) {
        Response.success(
            body = apiResponse.body(),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    } else {
        Response.error(
            body = apiResponse.body(),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    }
}

suspend inline fun <reified ResponseClazz> HttpClient.get(
    baseUrl: BaseUrl,
    endPoint: String,
    queryParams: Map<String, Any> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): Response<ResponseClazz> {
    val apiResponse = this.get(finalUrl(baseUrl, endPoint)) {
        contentType(ContentType.Application.Json)
        headers.forEach { (key, value) ->
            this.headers[key] = value
        }
        if (queryParams.isNotEmpty()) {
            url {
                queryParams.forEach {
                    parameters.append(it.key, it.value.toString())
                }
            }
        }
    }

    return if (apiResponse.status.value in 200..299) {
        Response.success(
            body = (apiResponse.body() as ResponseClazz),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    } else {
        Response.error(
            body = apiResponse.body(),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    }
}

suspend inline fun <reified ResponseClazz> HttpClient.delete(
    baseUrl: BaseUrl,
    endPoint: String,
    queryParams: Map<String, Any> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): Response<ResponseClazz> {
    val apiResponse = delete(finalUrl(baseUrl, endPoint)) {
        headers.forEach { (key, value) ->
            this.headers[key] = value
        }
        if (queryParams.isNotEmpty()) {
            url {
                queryParams.forEach {
                    parameters.append(it.key, it.value.toString())
                }
            }
        }
    }

    return if (apiResponse.status.value in 200..299) {
        Response.success(
            body = (apiResponse.body() as ResponseClazz),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    } else {
        Response.error(
            body = apiResponse.body(),
            status = apiResponse.status,
            headers = apiResponse.headers,
        )
    }
}

fun finalUrl(baseUrl: BaseUrl, endPoint: String): String {
    val finalEndpoint = if (endPoint.startsWith("/")) endPoint else "/$endPoint"
    val finalBaseUrl = if (baseUrl.endsWith("/")) baseUrl.removeSuffix("/") else baseUrl
    return "$finalBaseUrl$finalEndpoint"
}
