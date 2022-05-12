package com.pourkazemi.mahdi.mymaktabplus.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.pourkazemi.mahdi.mymaktabplus.data.Repository
import com.pourkazemi.mahdi.mymaktabplus.data.localdetabase.data_store.PreferencesInfo
import com.pourkazemi.mahdi.mymaktabplus.data.localdetabase.data_store.SettingDataStore
import com.pourkazemi.mahdi.mymaktabplus.data.remotedata.model.PictureItem
import com.pourkazemi.mahdi.mymaktabplus.paging.MyPagingSource
import com.pourkazemi.mahdi.mymaktabplus.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {
    /*    private val _pictureListState:
                MutableStateFlow<List<PictureItem>> = MutableStateFlow(listOf())
        val pictureListState get() = _pictureListState.asStateFlow()*/

   /* private val _pictureFlow: MutableStateFlow<ResultWrapper<List<PictureItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val pictureFlow = _pictureFlow.asStateFlow()
*/

    val myPager = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 20)
    ) {
        MyPagingSource(repository)
    }.flow
        .cachedIn(viewModelScope)


    init {
//        getPictureList()
    }

  /*  fun getPictureList() {
        viewModelScope.launch {
            val mRos = repository.getRemoteItemList()
            mRos.collect {
                _pictureFlow.emit(it)
            }
        }
    }*/
}