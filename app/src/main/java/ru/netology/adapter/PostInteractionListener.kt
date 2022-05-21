package ru.netology.adapter

import ru.netology.Post

interface PostInteractionListener {
    fun onLikeClicked(post: Post)
    fun onRepostClicked(post: Post)
    fun onRemoveClicked(post: Post)
    fun onEditClicked(post: Post)
}