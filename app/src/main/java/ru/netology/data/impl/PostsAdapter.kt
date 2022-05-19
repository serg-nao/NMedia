package ru.netology.data.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.Post
import ru.netology.R
import ru.netology.databinding.PostBinding
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.properties.Delegates

internal class PostsAdapter(
    private val  onLikeClicked: (Post) -> Unit,
    private val  onRepostClicked: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: PostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.likesIcon.setOnClickListener { onLikeClicked(post) }
        }

        init {
            binding.repostIcon.setOnClickListener { onRepostClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                content.text = post.content
                date.text = post.published
                likesIcon.setImageResource(getLikeIconResId(post.likedByMe))
                likesCount.text = calculate(post.likes, post.likedByMe)
                repostCount.text = numberConversion(post.shared)
            }

        }

        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24

        private fun calculate(currentLikes: Int, liked: Boolean): String {
            val likes = if (!liked) currentLikes else currentLikes + 1
            return numberConversion(likes)
        }

        private fun numberConversion(number: Int): String {
            val numberConverted: String
            val thousands = (number/1000.0)
            val millions = (thousands/1000.0)
            numberConverted = when {
                (millions >= 10) -> millions.toInt().toString() + "M"
                (millions >= 1) -> roundOffDecimal(millions).toString() + "M"
                (thousands >= 10) -> thousands.toInt().toString() + "K"
                (thousands >= 1) -> roundOffDecimal(thousands).toString() + "K"
                else -> number.toString()
            }
            return numberConverted
        }

        private fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.DOWN
            return df.format(number).toDouble()
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem

    }
}
