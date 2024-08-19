package ryuunta.iot.ryuuntaesp.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import ryuunta.iot.ryuuntaesp.data.network.RetrofitHelper
import ryuunta.iot.ryuuntaesp.data.network.RetrofitService

open class BaseViewModel : ViewModel() {

    val retrofitService: RetrofitService = RetrofitHelper.apiService

    val errorMessage = MutableLiveData<Map<String, String>>()
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(mapOf("exceptionHandler" to "Exception handled: ${throwable.localizedMessage}"))
    }
    var job = CoroutineScope(Dispatchers.IO + exceptionHandler)

    fun onError(message: Map<String, String>) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    val baseResult = MutableLiveData<Map<String, Any?>>()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}