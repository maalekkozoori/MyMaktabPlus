package com.pourkazemi.mahdi.mymaktabplus.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pourkazemi.mahdi.mymaktabplus.data.Repository
import com.pourkazemi.mahdi.mymaktabplus.data.remotedata.model.PictureItem
import javax.inject.Inject

class MyPagingSource @Inject constructor(
    private val repository: Repository
) : PagingSource<Int, PictureItem>(){

    override fun getRefreshKey(state: PagingState<Int, PictureItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PictureItem> {

        return try {
            val pageNumber = params.key ?: 1
            val response = repository.getRemoteItemList(pageNumber,params.loadSize)
            val result = response.body()
            if (response.isSuccessful && result!=null){
                LoadResult.Page(
                    data = result,
                    prevKey = if (pageNumber == 1) null else pageNumber.minus(1),
                    nextKey = if (result.isEmpty()) null else pageNumber.plus(1)
                )
            }else{
                throw Exception("Response not found!")
            }
        }catch (e :Exception){
            LoadResult.Error(e)
        }

    }
}