package com.shubh.unicoop.apihelper

import androidx.lifecycle.Observer

class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }

    fun peekContent() = content

    class EventObserver<T>(
        private inline val onError: ((String) -> Unit)? = null,
        private inline val onLoading: (() -> Unit)? = null,
        private inline val onSuccess: (T) -> Unit
    ) : Observer<Event<Resource<T>>> {
        override fun onChanged(value: Event<Resource<T>>) {
            when (val content = value?.peekContent()) {
                is Resource.Success -> {
                    content.data?.let(onSuccess)
                }
                is Resource.Error -> {
                    value.getContentIfNotHandled()?.let {
                        onError?.let { error ->
                            error(it.message!!)
                        }
                    }
                }
                is Resource.Loading -> {
                    onLoading?.let { loading ->
                        loading()
                    }
                }
                else -> {}
            }
        }
    }
}