package org.brainless.telyucreative.views.mainscreen.save

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.model.Favorite
import org.brainless.telyucreative.databinding.ItemOurRecommendationBinding
import org.brainless.telyucreative.databinding.ItemSaveBinding

class SaveAdapter (private var items: ArrayList<Favorite>, var handler: (Favorite) -> Unit) :
    RecyclerView.Adapter<SaveAdapter.SaveViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data : ArrayList<Favorite>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : SaveViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSaveBinding.inflate(inflater, parent, false)
        return SaveViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SaveViewHolder, position: Int) = with(holder) {
        bind(items[position])
        this.binding.root.setOnClickListener {
            handler(items[position])
            if (onClickListener != null) {
                onClickListener!!.onClick(position, items[position])
            }
        }
        this.binding.ivDelete.setOnClickListener {
            handler(items[position])
            if (onClickListener != null) {
                onClickListener!!.onDelete(position, items[position])
            }
        }
    }

    override fun getItemCount() = items.size

    class SaveViewHolder(
        var binding: ItemSaveBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) = with(binding) {
            Glide.with(this.root)
                .load(favorite.image)
                .into(ivFavorite)
            tvTitleCreation.text = favorite.title
            tvCategory.text = favorite.category

        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {
        fun onClick(position: Int, favorite: Favorite)
        fun onDelete(position: Int, favorite: Favorite)
    }
}