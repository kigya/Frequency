package com.example.frequency.utils.dialog_fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.frequency.R

typealias SimpleDialogResultListener = (String) -> Unit

class SimpleDialogFragment : DialogFragment() {

    private val title get() = requireArguments().getString(ARG_TITLE)
    private val requestMessage get() = requireArguments().getString(ARG_MESSAGE)
    private val positiveButtonTxt get() = requireArguments().getString(ARG_POS_BUT)
    private val neutralButtonTxt get() = requireArguments().getString(ARG_NEU_BUT)
    private val negativeButtonTxt get() = requireArguments().getString(ARG_NEG_BUT)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setIcon(R.drawable.ic_crisis_alert)
            .setTitle(title)
            .setMessage(requestMessage)
            .setPositiveButton(positiveButtonTxt) { _, _ ->
                setFragmentResult("Confirmed")
            }
            .setNeutralButton(neutralButtonTxt) { _, _ ->

            }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(TAG, "onDismiss")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.d(TAG, "onCancel")
    }

    private fun setFragmentResult(response: String) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(KEYS_MESSAGE_RESPONSE to response)
        )
    }

    companion object {
        @JvmStatic
        private val TAG = SimpleDialogFragment::class.java.simpleName

        @JvmStatic
        private val KEYS_MESSAGE_RESPONSE = "MESSAGE_RESPONSE"

        @JvmStatic
        private val ARG_MESSAGE = "ARG_MESSAGE"

        @JvmStatic
        private val ARG_TITLE = "ARG_TITLE"

        @JvmStatic
        private val ARG_NEG_BUT = "ARG_NEG_BUT"

        @JvmStatic
        private val ARG_NEU_BUT = "ARG_NEU_BUT"

        @JvmStatic
        private val ARG_POS_BUT = "ARG_POS_BUT"

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(
            fragmentManager: FragmentManager,
            title: String,
            message: String,
            posBut: String,
            neuBut: String,
            negBut: String? = null
        ) {
            val simpleDialog = SimpleDialogFragment()
            simpleDialog.arguments = bundleOf(
                ARG_TITLE to title,
                ARG_MESSAGE to message,
                ARG_POS_BUT to posBut,
                ARG_NEG_BUT to negBut,
                ARG_NEU_BUT to neuBut
            )
            simpleDialog.show(fragmentManager, TAG)
        }

        fun setUpListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: SimpleDialogResultListener
        ) {
            fragmentManager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(result.getString(KEYS_MESSAGE_RESPONSE).toString())
            }
        }
    }

}