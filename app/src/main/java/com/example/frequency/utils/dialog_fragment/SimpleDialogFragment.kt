package com.example.frequency.utils.dialog_fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

typealias SimpleDialogResultListener = (String) -> Unit

class SimpleDialogFragment : DialogFragment() {

    private val icon get() = requireArguments().getInt(ARG_ICON)
    private val title get() = requireArguments().getInt(ARG_TITLE)
    private val message get() = requireArguments().getInt(ARG_MESSAGE)
    private val positiveButtonTxt get() = requireArguments().getInt(ARG_POS_BUT)
    private val neutralButtonTxt get() = requireArguments().getInt(ARG_NEU_BUT)
    private val negativeButtonTxt get() = requireArguments().getInt(ARG_NEG_BUT)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
            .setIcon(icon)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonTxt) { _, _ ->
                setFragmentResult(POSITIVE_FRAG_RESPONSE)
            }
        if (neutralButtonTxt != 0) {
            builder.setNeutralButton(neutralButtonTxt) { _, _ ->
                setFragmentResult(NEUTRAL_FRAG_RESPONSE)
            }
        }
        if (negativeButtonTxt != 0) {
            builder.setNegativeButton(negativeButtonTxt) { _, _ ->
                setFragmentResult(NEGATIVE_FRAG_RESPONSE)
            }
        }
        if (negativeButtonTxt == 0 && neutralButtonTxt == 0) {
            builder.setCancelable(false)
        } else {
            builder.setCancelable(true)
        }

        return builder.create()

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
        private val ARG_ICON = "ARG_ICON"

        @JvmStatic
        private val ARG_NEG_BUT = "ARG_NEG_BUT"

        @JvmStatic
        private val ARG_NEU_BUT = "ARG_NEU_BUT"

        @JvmStatic
        private val ARG_POS_BUT = "ARG_POS_BUT"

        @JvmStatic
        val POSITIVE_FRAG_RESPONSE = "Confirmed"

        @JvmStatic
        val NEGATIVE_FRAG_RESPONSE = "Denied"

        @JvmStatic
        val NEUTRAL_FRAG_RESPONSE = "Ignore"

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(
            fragmentManager: FragmentManager,
            @DrawableRes icon: Int,
            @StringRes title: Int,
            @StringRes message: Int,
            @StringRes posBut: Int,
            @StringRes neuBut: Int? = null,
            @StringRes negBut: Int? = null
        ) {
            val simpleDialog = SimpleDialogFragment()
            simpleDialog.arguments = bundleOf(
                ARG_ICON to icon,
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
