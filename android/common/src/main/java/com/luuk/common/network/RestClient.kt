package com.luuk.common.network


object RestClient {
    fun <T> serviceWithExternalEndPoint(service: Class<T>, externalBaseUrl: String): T {
        return com.luuk.common.network.RestAdapterBuilderFactory.externalEndPoint(externalBaseUrl).build()
            .create(service)
    }

    fun <T> serviceWithUserAuthentication(authKey: String,service: Class<T>): T {
        return RestAdapterBuilderFactory.authenticated(authKey).build().create(service)
    }

    fun <T> service(service: Class<T>): T {
        return RestAdapterBuilderFactory.unauthenticated().build().create(service)
    }
}