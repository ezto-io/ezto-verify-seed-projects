package io.ezto.integrationsample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.ezto.integrationsample.model.PostRequest
import io.ezto.integrationsample.model.PostResponse
import io.ezto.integrationsample.repository.Repository

class MainViewModel : ViewModel() {
    private val repository = Repository()

    val postResult: LiveData<PostResponse?> = repository.postResult
    val error: LiveData<String?> = repository.error

    fun sendPostRequest() {
        val request = PostRequest("THIS_IS_A_SAMPLE_REQUEST", "ADOPT_TO_YOUR_BACKEND_API")
        repository.makePostRequest(request)
    }
}