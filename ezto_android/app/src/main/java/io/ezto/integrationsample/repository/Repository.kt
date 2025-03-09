package io.ezto.integrationsample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.ezto.integrationsample.ApiService
import io.ezto.integrationsample.model.PostRequest
import io.ezto.integrationsample.model.PostResponse

class Repository {
    private val _postResult = MutableLiveData<PostResponse?>()
    val postResult: LiveData<PostResponse?> = _postResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun makePostRequest(postRequest: PostRequest) {
        ApiService.makePostRequest(postRequest) { response, errorMessage ->
            if (response != null) {
                _postResult.postValue(response)
            } else {
                _error.postValue(errorMessage)
            }
        }
    }
}