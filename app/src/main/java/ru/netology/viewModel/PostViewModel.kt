package ru.netology.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.Post
import ru.netology.data.PostRepository
import ru.netology.data.impl.InMemoryPostRepository

class PostViewModel : ViewModel() {

    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data
    fun onLikeClicked(post: Post) = repository.like(post.id)
    fun onRepostClicked(post: Post) = repository.shared(post.id)


}