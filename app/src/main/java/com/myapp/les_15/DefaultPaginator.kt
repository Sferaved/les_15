package com.myapp.les_15

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}

class DefaultPaginator<Key, Item> (
    private val initialKey: Key,
    private inline val onLoadUpdate: (Boolean) ->Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<Item>,
    private inline val getNextKey: suspend (Item) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: Item, newKey: Key) -> Unit
): Paginator<Key, Item> {
    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if(isMakingRequest) {
            return
        }
        isMakingRequest = true

        onLoadUpdate(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdate(false)
            return
        }

        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdate(false)
    }

    override fun reset() {
        currentKey = initialKey
    }

}
