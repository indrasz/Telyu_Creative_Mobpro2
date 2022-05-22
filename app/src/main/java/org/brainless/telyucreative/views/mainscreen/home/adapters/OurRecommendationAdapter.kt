package org.brainless.telyucreative.views.mainscreen.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ItemOurRecommendationBinding
import org.brainless.telyucreative.model.Creation

class OurRecommendationAdapter (private var items: ArrayList<Creation>, var handler: (Creation) -> Unit) :
    RecyclerView.Adapter<OurRecommendationAdapter.OurRecommendationViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data : ArrayList<Creation>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OurRecommendationViewHolder(
        ItemOurRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: OurRecommendationViewHolder, position: Int) = with(holder) {
        bind(items[position])
        this.binding.root.setOnClickListener {
            handler(items[position])
            if (onClickListener != null) {
                onClickListener!!.onClick(position, items[position])
            }
        }
    }

    override fun getItemCount() = items.size

    class OurRecommendationViewHolder(var binding: ItemOurRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(creation: Creation) = with(binding) {
            Glide.with(this.root)
                .load(creation.image)
                .into(ivPopularSerach)
            Glide.with(this.root)
                .load(R.drawable.ic_user_profile)
                .into(ivUserProfile)
            tvUsername.text = creation.userName
            tvCategory.text = creation.category
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {

        fun onClick(position: Int, creation: Creation)
    }
}