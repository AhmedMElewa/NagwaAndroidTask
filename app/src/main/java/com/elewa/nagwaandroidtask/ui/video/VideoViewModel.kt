package com.elewa.nagwaandroidtask.ui.video

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elewa.nagwaandroidtask.data.local.ItemDao
import com.elewa.nagwaandroidtask.data.model.ItemModel
import com.elewa.nagwaandroidtask.data.repository.MainRepository
import com.elewa.nagwaandroidtask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable.fromIterable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val _offlineVideos = MutableLiveData<Resource<List<ItemModel>>>()
    val offlineVideos: LiveData<Resource<List<ItemModel>>>
        get() = _offlineVideos

    private val compositeDisposable = CompositeDisposable()


    init {
        fetchVideos()
    }


    fun fetchVideos() {
        _offlineVideos.postValue(Resource.loading(null))
        compositeDisposable.add(
            repository.getRemoteVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.filter { videoList -> videoList.type == "VIDEO" } }
                .subscribe({ videoList ->
                    saveVideosInDB(videoList)
                }, {

                    getVideosFromDB()
                })
        )

    }

    private fun saveVideosInDB(videos: List<ItemModel>) {
        viewModelScope.launch {
            repository.insert(videos)
            getVideosFromDB()
        }
    }

    private fun getVideosFromDB() {
        viewModelScope.launch {
            repository.getVideoFromDB().catch { e ->
                _offlineVideos.value = Resource.noInternet("No Internet connection", emptyList())

            }.collect {
                if (it.isNullOrEmpty()) {
                    _offlineVideos.value =
                        Resource.noInternet("No Internet connection", emptyList())
                } else {
                    _offlineVideos.value = Resource.success(it)
                }
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}