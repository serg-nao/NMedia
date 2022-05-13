package ru.netology.data

import androidx.lifecycle.LiveData
import ru.netology.Post

interface PostRepository {
    val data: LiveData<Post>

    fun like()
    fun shared()
}