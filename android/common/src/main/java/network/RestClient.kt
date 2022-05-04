package network

import userdata.User


object RestClient {
    fun <T> serviceWithExternalEndPoint(service: Class<T>, externalBaseUrl: String): T {
        return RestAdapterBuilderFactory.externalEndPoint(externalBaseUrl).build()
            .create(service)
    }

    fun <T> serviceWithUserAuthentication(service: Class<T>): T {
        var authKey = User.getCurrent().sessionKey
        return RestAdapterBuilderFactory.authenticated(authKey).build().create(service)
    }

    fun <T> service(service: Class<T>): T {
        return RestAdapterBuilderFactory.unauthenticated().build().create(service)
    }
}