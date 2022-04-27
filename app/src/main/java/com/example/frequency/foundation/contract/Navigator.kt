package com.example.frequency.foundation.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.frequency.data.model.network.station.Station


typealias ResultListener<T> = (T) -> Unit

// Generates access to the navigator from a fragment
fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

/**
 * Interface for moving between fragments and transferring data
 * */
interface Navigator {

    fun openWelcome()

    fun openSignIn()

    fun openSignUp()

    fun openSettings()

    fun openStation(station: Station)

    fun openLyrics()

    fun openProfile()

    fun openFavourite()

    fun openContactUs()

    fun openFaqs()

    fun openPreview(uri: String)

    fun goBack()

    fun goToMenu()

    fun openFragment(
        fragment: Fragment,
        firstTime: Boolean = false,
        addToBackStack: Boolean = true,
        clearBackstack: Boolean = false
    )

    fun showProgress(state: Boolean)

    fun clearBackStack()

    fun <T : Parcelable> provideResult(result: T)

    fun <T : Parcelable> listenResults(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    )

}