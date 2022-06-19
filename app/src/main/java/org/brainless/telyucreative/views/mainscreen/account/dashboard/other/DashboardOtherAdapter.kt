package org.brainless.telyucreative.views.mainscreen.account.dashboard.other

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.databinding.ItemDashboardBinding

class DashboardOtherAdapter (private var items: ArrayList<Creation>, var handler: (Creation) -> Unit) :
    RecyclerView.Adapter<DashboardOtherAdapter.DashboardViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data : ArrayList<Creation>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDashboardBinding.inflate(inflater, parent, false)
        return DashboardViewHolder(binding)
    }


    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) = with(holder) {
        bind(items[position])
        this.binding.root.setOnClickListener {
            handler(items[position])
            if (onClickListener != null) {
                onClickListener!!.onClick(position, items[position])
            }
        }
//        this.binding.ivDelete.setOnClickListener {
//            handler(items[position])
//            if (onClickListener != null) {
//                onClickListener!!.onDelete(position, items[position])
//            }
//        }
    }

    override fun getItemCount() = items.size

    class DashboardViewHolder(
        var binding: ItemDashboardBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(creation: Creation) = with(binding) {
            Glide.with(this.root)
                .load(creation.image)
                .into(ivDashboard)
//            tvTitleCreation.text = favorite.title
//            tvCategory.text = favorite.category

        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {
        fun onClick(position: Int, creation: Creation)
//        fun onDelete(position: Int, favorite: Favorite)
    }
}