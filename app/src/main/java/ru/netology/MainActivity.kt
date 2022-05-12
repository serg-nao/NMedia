package ru.netology

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.databinding.ActivityMainBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 0L,
            author = "Нетология. Меняем карьеру через образование",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсовов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "10.05.2022 в 16:41"
        )

        binding.render(post)
        binding.likesIcon.setOnClickListener {
            post.likedByMe = !post.likedByMe
            binding.likesIcon.setImageResource(getLikeIconResId(post.likedByMe))
            binding.likesCount.setText(calculate(post.likes, post.likedByMe))
            post.likes = if (post.likedByMe) post.likes++ else post.likes
        }

        binding.repostIcon.setOnClickListener {
            post.shared++
            binding.repostCount.setText(numberConversion(post.shared))
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