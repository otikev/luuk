package network

import userdata.User


object RestClient {
    fun <T> serviceWithExternalEndPoint(service: Class<T>, externalBaseUrl: String): T {
        return RestAdapterBuilderFactory.externalEndPoint(externalBaseUrl).build()
            .create(service)
    }

    fun <T> serviceWithUserAuthentication(service: Class<T>): T {

        var authKey = "null"
        if (User.getCurrent() != null) {
            authKey = User.getCurrent().userDetails.sessionKey!!
        }
        return RestAdapterBuilderFactory.authenticated(authKey).build().create(service)
    }

    fun <T> service(service: Class<T>): T {
        return RestAdapterBuilderFactory.unauthenticated().build().create(service)
    }
}