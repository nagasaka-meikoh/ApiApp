package jp.techacademy.nagasaka.yasushi.apiapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class FavoriteAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<FavoriteShop>()

    var onClickDeleteFavorite: ((FavoriteShop) -> Unit)? = null
    var onClickItem: ((FavoriteShop) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_EMPTY -> EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_favorite_empty, parent, false))
            else -> FavoriteItemViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_favorite, parent, false))
        }
    }


    fun refresh(list: List<FavoriteShop>) {
        items.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (items.isEmpty()) 1 else items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(items.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FavoriteItemViewHolder) {
            updateFavoriteItemViewHolder(holder, position)
        }
    }

    private fun updateFavoriteItemViewHolder(holder: FavoriteItemViewHolder, position: Int) {
        val data = items[position]
        holder.apply {
            rootView.apply {
                setBackgroundColor(ContextCompat.getColor(context, if (position % 2 == 0) android.R.color.white else R.color.cell))
                setOnClickListener {
                    onClickItem?.invoke(data)
                }
            }
            nameTextView.text = data.name
            nameTextView2.text = data.address
            Picasso.get().load(data.imageUrl).into(imageView)
            favoriteImageView.setOnClickListener {
                onClickDeleteFavorite?.invoke(data)
                notifyItemChanged(position)
            }
        }
    }

    class FavoriteItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val rootView : ConstraintLayout = view.findViewById(R.id.rootView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val nameTextView2: TextView = view.findViewById(R.id.nameTextView2)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val favoriteImageView: ImageView = view.findViewById(R.id.favoriteImageView)
    }

    class EmptyViewHolder(view: View): RecyclerView.ViewHolder(view)


    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_EMPTY = 1
    }
}