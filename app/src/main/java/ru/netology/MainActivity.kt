package ru.netology

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.databinding.ActivityMainBinding
import ru.netology.viewModel.PostViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.render(post)
        }

        binding.likesIcon.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.repostIcon.setOnClickListener {
            viewModel.onRepostClicked()
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        authorName.text = post.author
        content.text = post.content
        date.text = post.published
        likesIcon.setImageResource(getLikeIconResId(post.likedByMe))
        likesCount.text = calculate(post.likes, post.likedByMe)
        repostCount.text = numberConversion(post.shared)
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