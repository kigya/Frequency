package com.example.frequency.foundation.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.frequency.model.Options


typealias ResultListener<T> = (T) -> Unit

// Generates access to the navigator from a fragment
fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

/**
 * Interface for moving between fragments and transferring data
 * */
interface Navigator {

    // TODO необходимо определить сущьности перемещающиеся между фрагментами у всех функций перемещения и поместить в аргументы.
    fun openSignInRequest() // TODO

    fun openSignUp() // TODO

    fun openHomeScreen(options: Options) // TODO

    fun openSettings() // TODO

    fun openSong() // TODO

    fun openLyrics() // TODO

    fun openProfile() //TODO

    fun openContactUs() //TODO

    fun openFaqs()

    fun goBack()

    fun goToMenu()

    fun openFragment(
        fragment: Fragment,
        firstTime: Boolean = false,
        addToBackStack: Boolean = true,
        clearBackstack: Boolean = false
    )

    fun clearBackStack()

    fun <T : Parcelable> provideResult(result: T)

    fun <T : Parcelable> listenResults(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    )

}