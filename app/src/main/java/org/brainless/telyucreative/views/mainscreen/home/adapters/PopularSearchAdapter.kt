package org.brainless.telyucreative.views.mainscreen.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ItemPopularSearchBinding
import org.brainless.telyucreative.data.model.Category
import org.brainless.telyucreative.data.remote.network.CategoryApi

class PopularSearchAdapter
//    (var items: ArrayList<Category>, var handler: (Category) -> Unit)
    :
    RecyclerView.Adapter<PopularSearchAdapter.PopularSearchViewHolder>() {

    private val items = mutableListOf<Category>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListDataCategory(data : List<Category>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PopularSearchViewHolder(
        ItemPopularSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PopularSearchViewHolder, position: Int)
//    = with(holder)
    {
//        bind(items[position])
//        this.binding.root.setOnClickListener {
//            handler(items[position])
//        }
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class PopularSearchViewHolder(var binding: ItemPopularSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) = with(binding) {
            Glide.with(this.root)
                .load(CategoryApi.getCategoryUrl(category.imageId))
                .error(R.drawable.img_popular4)
                .into(ivPopularSerach)
            tvPopularSearch.text = category.nama

            root.setOnClickListener {
//                val message = root.context.getString(R.string.message, category.nama)
//                Toast.makeText(root.context, message, Toast.LENGTH_LONG).show()

                Snackbar.make(
                    root,
                    category.nama,
                    Snackbar.LENGTH_SHORT,

                    ).show()
            }
//            Glide.with(this.root)
//                .load(category.image)
//                .into(ivPopularSerach)
//            tvPopularSearch.text = category.name
        }
    }
}