package ru.netology.data

import androidx.lifecycle.LiveData
import ru.netology.Post

interface PostRepository {
    val data: LiveData<List<Post>>

    fun like(postId: Long)
    fun shared(postId: Long)
}