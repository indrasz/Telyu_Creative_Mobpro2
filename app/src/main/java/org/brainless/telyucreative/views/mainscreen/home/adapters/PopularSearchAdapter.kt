package org.brainless.telyucreative.views.mainscreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.brainless.telyucreative.databinding.ItemPopularSearchBinding
import org.brainless.telyucreative.model.Category

class PopularSearchAdapter (var items: ArrayList<Category>, var handler: (Category) -> Unit) :
    RecyclerView.Adapter<PopularSearchAdapter.PopularSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PopularSearchViewHolder(
        ItemPopularSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PopularSearchViewHolder, position: Int) = with(holder) {
        bind(items[position])
        this.binding.root.setOnClickListener { handler(items[position]) }
    }

    override fun getItemCount() = items.size

    class PopularSearchViewHolder(var binding: ItemPopularSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) = with(binding) {
            Glide.with(this.root)
                .load(category.image)
                .into(ivPopularSerach)
            tvPopularSearch.text = category.name
        }
    }
}