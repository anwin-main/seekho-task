package com.anwin.seekho.ui.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anwin.seekho.R
import com.anwin.seekho.databinding.AnimeListItemBinding
import com.anwin.seekho.service.local.db.entity.AnimeEntity
import com.anwin.seekho.utils.TestUtils.formatAgeRating
import com.anwin.seekho.utils.TestUtils.formatCount
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AnimeListAdapter(
    private val list: MutableList<AnimeEntity> = mutableListOf(),
    private val listener: OnAnimeClickListener
) : RecyclerView.Adapter<AnimeListAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: AnimeListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.txtTitle
        val rating: TextView = binding.txtRating
        // Other views are accessed through binding directly
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AnimeListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        
        // Title
        holder.title.text = item.title
        
        // Subtitle/Description
        val subtitle = when {
            !item.titleEnglish.isNullOrEmpty() -> item.titleEnglish
            !item.titleJapanese.isNullOrEmpty() -> item.titleJapanese
            !item.genres.isNullOrEmpty() -> item.genres
            else -> "Anime Series & Entertainment"
        }
        holder.binding.txtSubtitle.text = subtitle
        
        // Rating
        val ratingText = String.format("%.1f", item.rating ?: 0.0)
        holder.rating.text = ratingText

        // Age Rating (based on rating or default)
        val ageRating = formatAgeRating(item.ageRating)
        holder.binding.txtAgeRating.text = ageRating

        val favouriteCount = formatCount(item.favorites)
        holder.binding.txtFavourites.text = favouriteCount

        val views = formatCount(item.members)
        holder.binding.userViewsTV.text = views

        // Load image with Glide
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.sample_image)
            .error(R.drawable.sample_image)
            .centerCrop()
            .into(holder.binding.imgPoster)

        // Click listeners
        holder.binding.root.setOnClickListener {
            listener.onAnimeClick(item, position)
        }
        
        holder.binding.btnGet.setOnClickListener {
            listener.onAnimeClick(item, position)
        }
    }

    override fun getItemCount(): Int = list.size

    // 🔹 Clear all items
    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    // 🔹 Set new data
    fun setData(newList: List<AnimeEntity>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    // 🔹 Add more data (for pagination)
    fun addData(newList: List<AnimeEntity>) {
        val start = list.size
        list.addAll(newList)
        notifyItemRangeInserted(start, newList.size)
    }
}