package `in`.vrkhedkr.wardrobe.view

import `in`.vrkhedkr.wardrobe.R
import `in`.vrkhedkr.wardrobe.model.Ware
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class PagerAdapter:RecyclerView.Adapter<PagerAdapter.ViewHolder>() {

    var items:ArrayList<Ware> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_for_pager,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(File(items[position].imgUrl))
            .into(holder.image)
    }

    fun setData(list:ArrayList<Ware>){
        items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(item: View):RecyclerView.ViewHolder(item){
        val image:ImageView = item.findViewById<ImageView>(R.id.ware)
    }

}