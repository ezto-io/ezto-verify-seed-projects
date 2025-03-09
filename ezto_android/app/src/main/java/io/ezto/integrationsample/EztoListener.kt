package io.ezto.integrationsample

import io.ezto.verify.sdk.EztoService
import io.ezto.verify.sdk.enums.CloseReason
import io.ezto.verify.sdk.enums.ErrorCode
import io.ezto.verify.sdk.model.GetTokenModel
import io.ezto.verify.sdk.model.PushSupport

class EztoListener: EztoService {
    override suspend fun onPushTokenRequest(): GetTokenModel? {
        //SDK has requested token
        //Get token from firebase and return it
        return GetTokenModel(
            "token",
            PushSupport.firebase
        );
    }

    override fun onResult(reqId: String, authReqId: String) {
        //This will be called if the Result Hook notification way in Ezto dashboard is set to Mobile_Sdk
        //Only called if the authentication flow is completed successfully
    }

    override suspend fun onPermissionDenied(deniedPermissions: List<String>): Boolean {
        //The user has denied runtime permission
        //The app can try again and return true if all requested permissions are granted, else return false
        return false
    }

    override fun onError(error: ErrorCode) {
        //This will be called in case of any errors during the flow
    }

    override fun onClosed(reason: CloseReason) {
        //This will be called if the sdk bottom sheet is closed
    }

    override fun onTransactionRequest(payload: Map<String,String>) {
        //This will be get deeplink payload data
        //Pass encryption key if the the push payload is encrypted
    }
}