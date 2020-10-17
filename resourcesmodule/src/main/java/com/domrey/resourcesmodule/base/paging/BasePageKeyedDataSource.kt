package com.domrey.resourcesmodule.base.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.domrey.resourcesmodule.data.network.NetworkRequest
import com.domrey.resourcesmodule.data.type.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BasePageKeyedDataSource<T>(
   private val scope: CoroutineScope
) : PageKeyedDataSource<Int, T>() {

   var networkState = MutableLiveData<Resource<List<T>>>()
   var retry: (() -> Unit)? = null

   abstract fun getRequest(size: Int, page: Int = 1): NetworkRequest<List<T>>

   override fun loadInitial(
      params: LoadInitialParams<Int>,
      callback: LoadInitialCallback<Int, T>
   ) {
      loadDataItems(1, getRequest(params.requestedLoadSize)) { items, nextOffset ->
         callback.onResult(items, 1, nextOffset)
      }
   }

   override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
      loadDataItems(
         params.key,
         getRequest(params.requestedLoadSize, params.key)
      ) { items, nextOffset ->
         callback.onResult(items, nextOffset)
      }
   }

   override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {}

   open fun loadDataItems(
      currentPage: Int,
      request: NetworkRequest<List<T>>,
      completion: ((items: List<T>, nextOffset: Int) -> Unit)
   ) {
      scope.launch(Dispatchers.Main) {
         request.result.observeForever {
            networkState.postValue(it)
         }
      }
      scope.launch {
         request.fetchFromNetwork()
         request.asLiveData().observeForever {
            if (it is Resource.Success) {
               val data = it.data ?: return@observeForever
               val nextPage = currentPage.plus(1)
               data.let { items -> completion.invoke(items, nextPage) }
            } else if (it is Resource.Error) {
               retry = {
                  scope.launch {
                     request.fetchFromNetwork()
                  }
               }
            }
         }
      }
   }

   fun retryDataItem() {
      retry?.invoke()
   }

}