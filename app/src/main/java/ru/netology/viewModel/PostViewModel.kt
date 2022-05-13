package ru.netology.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.data.PostRepository
import ru.netology.data.impl.InMemoryPostRepository

class PostViewModel : ViewModel() {

    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data
    fun onLikeClicked() = repository.like()
    fun onRepostClicked() = repository.shared()


}