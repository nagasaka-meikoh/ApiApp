package jp.techacademy.nagasaka.yasushi.apiapp

interface FragmentCallback {
    fun onClickItem(data: Shop)
    fun onClickItem2(data: FavoriteShop)
    fun onAddFavorite(shop: Shop)
    fun onDeleteFavorite(id: String)
}