package com.example.frequency.foundation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.frequency.foundation.model.async_operation.ErrorResult
import com.example.frequency.foundation.model.async_operation.PendingResult
import com.example.frequency.foundation.model.async_operation.Result
import com.example.frequency.foundation.model.async_operation.SuccessResult
import com.example.frequency.ui.rendering.OnError
import com.example.frequency.ui.rendering.OnPending
import com.example.frequency.ui.rendering.OnSuccess

abstract class BaseFragment : Fragment(){

    abstract val viewModel: BaseVM

    fun <T> renderResult(
        root: ViewGroup,
        result: Result<T>,
        onPending: OnPending,
        onError: OnError,
        onSuccess: OnSuccess<T>
    ) {
        root.children.forEach { it.visibility = View.GONE }
        when(result){
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError(result.exception)
            is PendingResult -> onPending()
        }
    }
}