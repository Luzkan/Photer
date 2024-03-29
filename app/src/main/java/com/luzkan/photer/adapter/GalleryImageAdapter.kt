package com.luzkan.photer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.luzkan.photer.R
import com.luzkan.photer.helper.GlideApp
import kotlinx.android.synthetic.main.item_gallery_image.view.*

// Adapter is initialized in onCreate and everytime user wants to change the number of columns
// https://antonioleiva.com/recyclerview-adapter-kotlin/ for future quick reference
class GalleryImageAdapter(private val itemList: List<Image>) : RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {

    private var context: Context? = null
    var listener: GalleryImageClickListener? = null

    // item_gallery_image.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImageAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_gallery_image, parent,
                false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: GalleryImageAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            val image = itemList[adapterPosition]
            // Loads the Image
            GlideApp.with(context!!)
                .load(image.imageUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.ivGalleryImage)

            // Every image gets its own click handler (see interface)
            itemView.container.setOnClickListener {
                listener?.onClick(adapterPosition)
            }
        }
    }
}