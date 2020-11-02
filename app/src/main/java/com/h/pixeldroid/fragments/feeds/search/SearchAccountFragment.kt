package com.h.pixeldroid.fragments.feeds.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.h.pixeldroid.R
import com.h.pixeldroid.fragments.feeds.AccountListFragment
import com.h.pixeldroid.fragments.feeds.FeedFragment
import com.h.pixeldroid.objects.Account
import com.h.pixeldroid.objects.Results
import com.h.pixeldroid.objects.Tag
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchAccountFragment: AccountListFragment(){

    private lateinit var query: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        query = arguments?.getSerializable("searchFeed") as String

        return view
    }

    inner class SearchAccountListDataSource: FeedDataSource<String, Account>(){

        override fun newSource(): SearchAccountListDataSource {
            return SearchAccountListDataSource()
        }

        override fun getKey(item: Account): String {
            return content.value?.loadedCount.toString()
        }

        private fun searchMakeInitialCall(requestedLoadSize: Int): Call<Results> {
            return pixelfedAPI
                .search("Bearer $accessToken",
                    limit="$requestedLoadSize", q = query,
                    type = Results.SearchType.accounts)
        }
        private fun searchMakeAfterCall(requestedLoadSize: Int, key: String): Call<Results> {
            return pixelfedAPI
                .search("Bearer $accessToken", offset = key,
                    limit="$requestedLoadSize", q = query,
                    type = Results.SearchType.accounts)
        }

        override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<Account>
        ) {
            searchEnqueueCall(searchMakeInitialCall(params.requestedLoadSize), callback)
        }

        override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<Account>) {
            searchEnqueueCall(searchMakeAfterCall(params.requestedLoadSize, params.key), callback)
        }
        private fun searchEnqueueCall(call: Call<Results>, callback: LoadCallback<Account>) {

            call.enqueue(object : Callback<Results> {
                override fun onResponse(call: Call<Results>, response: Response<Results>) {
                    if (response.code() == 200) {
                        val notifications = response.body()!!.accounts as ArrayList<Account>
                        callback.onResult(notifications as List<Account>)
                    } else{
                        showError()
                    }
                    swipeRefreshLayout.isRefreshing = false
                    loadingIndicator.visibility = View.GONE
                }

                override fun onFailure(call: Call<Results>, t: Throwable) {
                    showError(errorText = R.string.feed_failed)
                    Log.e("FeedFragment", t.toString())
                }
            })
        }

        override fun makeInitialCall(requestedLoadSize: Int): Call<List<Account>> {
            throw NotImplementedError("Should not be called, reimplemented for Search fragment")
        }

        override fun makeAfterCall(requestedLoadSize: Int, key: String): Call<List<Account>> {
            throw NotImplementedError("Should not be called, reimplemented for Search fragment")
        }

        override fun enqueueCall(call: Call<List<Account>>, callback: LoadCallback<Account>) {
            throw NotImplementedError("Should not be called, reimplemented for Search fragment")
        }
    }

    override fun makeContent(): LiveData<PagedList<Account>> {
        val config: PagedList.Config = PagedList.Config.Builder().setPageSize(10).build()
        factory = FeedFragment().FeedDataSourceFactory(SearchAccountListDataSource())
        return LivePagedListBuilder(factory, config).build()
    }
}