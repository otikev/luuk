package com.luuk.common.network

import userdata.User


object RestClient {
    fun <T> serviceWithExternalEndPoint(service: Class<T>, externalBaseUrl: String): T {
        return com.luuk.common.network.RestAdapterBuilderFactory.externalEndPoint(externalBaseUrl).build()
            .create(service)
    }

    fun <T> serviceWithUserAuthentication(service: Class<T>): T {

        var authKey = "null"
        if (User.getCurrent() != null) {
            authKey = User.getCurrent().userDetails.authToken
        }
        return RestAdapterBuilderFactory.authenticated(authKey).build().create(service)
    }

    fun <T> service(service: Class<T>): T {
        return RestAdapterBuilderFactory.unauthenticated().build().create(service)
    }
}