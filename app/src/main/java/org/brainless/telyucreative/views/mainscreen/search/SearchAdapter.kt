package org.brainless.telyucreative.views.mainscreen.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.databinding.ItemSearchBinding

class SearchAdapter (private var items: ArrayList<Creation>, var handler: (Creation) -> Unit) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data : ArrayList<Creation>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchViewHolder(
        ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) = with(holder) {
        bind(items[position])
        this.binding.root.setOnClickListener {
            handler(items[position])
            if (onClickListener != null) {
                onClickListener?.onClick(position, items[position])
            }
        }
    }

    override fun getItemCount() = items.size

    class SearchViewHolder(var binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(creation: Creation) = with(binding) {
            Glide.with(this.root)
                .load(creation.image)
                .into(ivCreation)
            tvCreation.text = creation.title
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {
        fun onClick(position: Int, creation: Creation)
    }
}