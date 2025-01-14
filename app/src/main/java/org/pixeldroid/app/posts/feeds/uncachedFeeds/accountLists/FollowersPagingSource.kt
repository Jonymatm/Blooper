package org.pixeldroid.app.posts.feeds.uncachedFeeds.accountLists

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.pixeldroid.app.utils.api.PixelfedAPI
import org.pixeldroid.app.utils.api.objects.Account
import retrofit2.HttpException

class FollowersPagingSource(
    private val api: PixelfedAPI,
    private val accountId: String,
    private val following: Boolean
) : PagingSource<String, Account>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Account> {
        val position = params.key
        return try {
            val response =
            // Pixelfed and Mastodon don't implement this in the same fashion. Pixelfed uses
            // Laravel's paging mechanism, while Mastodon uses the Link header for pagination.
                // No need to know which is which, they should ignore the non-relevant argument
                if(following) {
                    api.followers(
                        account_id = accountId,
                        max_id = position,
                        limit = params.loadSize,
                        page = position
                    )
                } else {
                    api.following(
                        account_id = accountId,
                        max_id = position,
                        limit = params.loadSize,
                        page = position
                    )
                }

            val accounts = if(response.isSuccessful){
                response.body().orEmpty()
            } else {
                throw HttpException(response)
            }

            val nextPosition: String = response.headers()["Link"]
                // Header is of the form:
                // Link: <https://mastodon.social/api/v1/accounts/1/followers?limit=2&max_id=7628164>; rel="next", <https://mastodon.social/api/v1/accounts/1/followers?limit=2&since_id=7628165>; rel="prev"
                // So we want the first max_id value. In case there are arguments after
                // the max_id in the URL, we make sure to stop at the first '?'
                ?.substringAfter("max_id=", "")
                ?.substringBefore('?', "")
                ?.substringBefore('>', "")

                ?: // No Link header, so we just increment the position value (Pixelfed case)
                (position?.toBigIntegerOrNull() ?: 1.toBigInteger()).inc().toString()

            LoadResult.Page(
                data = accounts,
                prevKey = null,
                nextKey = if (accounts.isEmpty() || nextPosition.isEmpty() || nextPosition == position) null else nextPosition
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Account>): String? = null
}