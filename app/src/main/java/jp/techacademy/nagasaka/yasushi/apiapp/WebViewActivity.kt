package jp.techacademy.nagasaka.yasushi.apiapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_web_view.*
import android.util.Log
import com.google.gson.Gson
import java.io.Serializable
import kotlin.math.log
import androidx.appcompat.app.AlertDialog


open class WebViewActivity : AppCompatActivity(), FragmentCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("test", "check2")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        //var data = intent.getSerializableExtra(KEY_URL) as Shop
        var data = intent.getSerializableExtra(KEY_URL) as Shop
        var url = if (data.couponUrls.sp.isNotEmpty()) data.couponUrls.sp else data.couponUrls.pc

        webView.loadUrl(url)

        Log.d("a", intent.getStringExtra(KEY_URL).toString())

        var isFavorite = FavoriteShop.findByKey_Url(url)!= null


        val listener = FabOnClickListener()
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        Log.d("isFavorite", isFavorite.toString())

        fab.setImageResource(if(isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)
        fab.setOnClickListener {
            if (isFavorite) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.delete_favorite_dialog_title)
                    .setMessage(R.string.delete_favorite_dialog_message)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        deleteFavorite(data.id)
                        isFavorite = false
                        fab.setImageResource(R.drawable.ic_star_border)
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ ->}
                    .create()
                    .show()
            } else {
                onAddFavorite(data)
                isFavorite = true
                fab.setImageResource(R.drawable.ic_star)
            }
        }

    }

    private inner class FabOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            Toast.makeText(applicationContext, "FabOnCLick", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val KEY_URL = "key_url"
        fun start(activity: Activity, data: Shop) {
            Log.d("test", data.id.toString())
            //val intent = Intent(activity, WebViewActivity::class.java)
            //intent.putExtra("test", data)
            //activity.startActivity(intent)
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, data))
        }
    }

    override fun onClickItem(data: Shop) {
    }

    override fun onClickItem2(data: FavoriteShop) {

    }



    override fun onAddFavorite(shop: Shop) { // Favoriteに追加するときのメソッド(Fragment -> Activity へ通知する)
        Log.d("a", shop.id.toString())
        Log.d("a", shop.couponUrls.sp.toString())

        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            imageUrl = shop.logoImage
            address = shop.address
            url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        })
    }

    override fun onDeleteFavorite(id: String) { // Favoriteから削除するときのメソッド(Fragment -> Activity へ通知する)
        showConfirmDeleteFavoriteDialog(id)
    }
    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(id)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }

    private fun deleteFavorite(id: String) {
        FavoriteShop.delete(id)
    }

}