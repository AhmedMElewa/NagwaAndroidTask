package com.elewa.nagwaandroidtask.ui.book

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
class BookViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val _offlineBooks = MutableLiveData<Resource<List<ItemModel>>>()
    val offlineBooks: LiveData<Resource<List<ItemModel>>>
        get() = _offlineBooks

    private val compositeDisposable = CompositeDisposable()

    init {
        fetchBooks()
    }

    fun fetchBooks() {
        _offlineBooks.postValue(Resource.loading(null))
        compositeDisposable.add(
            repository.getRemoteVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.filter { videoList -> videoList.type == "PDF" } }
                .subscribe({ videoList ->
                    saveBooksInDB(videoList)
                }, {
                    getBooksFromDB()
                })
        )

    }


    private fun saveBooksInDB(books: List<ItemModel>) {
        viewModelScope.launch {
            repository.insert(books)
            getBooksFromDB()
        }
    }

    private fun getBooksFromDB() {
        viewModelScope.launch {

            repository.getBookFromDB().catch { e ->
                _offlineBooks.value = Resource.noInternet("Check your internet connection", emptyList())
            }.collect {
                if (it.isNullOrEmpty()) {
                    _offlineBooks.value = Resource.noInternet("Check your internet connection", emptyList())
                } else {
                    _offlineBooks.value = Resource.success(it)
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}