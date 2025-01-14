package org.pixeldroid.app.posts.feeds.uncachedFeeds.accountLists

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import org.pixeldroid.app.utils.api.PixelfedAPI
import org.pixeldroid.app.posts.feeds.uncachedFeeds.UncachedContentRepository
import org.pixeldroid.app.utils.api.objects.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FollowersContentRepository @ExperimentalPagingApi
@Inject constructor(
    private val api: PixelfedAPI,
    private val accountId: String,
    private val following: Boolean,
): UncachedContentRepository<Account> {
    override fun getStream(): Flow<PagingData<Account>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = NETWORK_PAGE_SIZE,
                pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                FollowersPagingSource(api, accountId, following)
            }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}