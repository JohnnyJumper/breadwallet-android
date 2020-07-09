package com.breadwallet.ui.send

import android.os.AsyncTask
import com.unstoppabledomains.exceptions.NamingServiceException
import com.unstoppabledomains.resolution.Resolution

data class ResolutionResult(val error: NamingServiceException?, val address: String?) {}

class AsyncResolution : AsyncTask<String, String, ResolutionResult>() {
    private val tool: Resolution =
        Resolution("https://mainnet.infura.io/v3/0728d3860f484d6683ec3e3033f73b08")

    override fun doInBackground(vararg params: String?): ResolutionResult {
        val domain =  params[0]
        val currency = params[1]
        return try {
            val address = this.tool.addr(domain, currency)
            ResolutionResult(null, address)
        } catch(err: NamingServiceException) {
            err.printStackTrace();
            ResolutionResult(err, null)
        }
    }
}