package ru.netology.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
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
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: PostBinding,
        listener: PostInteractionListener
        //private val  onRepostClicked: OnPostLikeClicked (возможно не понадобится)
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menu).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likesIcon.setOnClickListener { listener.onLikeClicked(post) }
        }

        init {
            binding.repostIcon.setOnClickListener { listener.onRepostClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                content.text = post.content
                date.text = post.published
                likesIcon.setImageResource(getLikeIconResId(post.likedByMe))
                likesCount.text = numberConversion(post.likes)
                repostCount.text = numberConversion(post.shared)
                menu.setOnClickListener {popupMenu.show()}
            }
        }

        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24

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
