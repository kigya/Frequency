package com.example.frequency.ui.rendering

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.example.frequency.R
import com.example.frequency.databinding.InProcessPartResultBinding
import com.example.frequency.foundation.model.async_operation.Result
import com.example.frequency.foundation.views.BaseFragment

typealias OnSuccess<T> = (T) -> Unit
typealias OnPending = () -> Unit
typealias OnError = (Exception) -> Unit

fun <T> BaseFragment.renderSimpleResult(
    root: ViewGroup,
    result: Result<T>,
    onSuccess: OnSuccess<T>
) {
    val binding = InProcessPartResultBinding.bind(root)
    renderResult(
        root = root,
        result = result,
        onPending = {
            binding.progressB.visibility = View.VISIBLE
        },
        onError = {
            binding.errorContainer.visibility = View.VISIBLE
        },
        onSuccess = { data ->
            root.children
                .filter { it.id != R.id.progressB && it.id != R.id.errorContainer }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(data)
        }
    )


}