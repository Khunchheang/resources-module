package com.domrey.resourcesmodule.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren

abstract class BaseViewModel : ViewModel() {

   fun cancelRequests(){
      viewModelScope.coroutineContext.cancelChildren()
   }

}