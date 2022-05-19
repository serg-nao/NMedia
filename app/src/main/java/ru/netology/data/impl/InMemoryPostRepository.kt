package ru.netology.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.Post
import ru.netology.data.PostRepository

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData(
        List(10) { index ->
            Post(
                id = index + 1L,
                author = "Нетология. Меняем карьеру через образование",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсовов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                published = "19/05/2022"
            )
        }
    )

    private val posts get() = checkNotNull(data.value) {
        "Data value should not be null"
    }

    override fun like(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
    }

    override fun shared(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                shared = it.shared + 1
            )
        }
    }
}
