package com.example.frequency

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.frequency.databinding.ActivityMainBinding
import com.example.frequency.databinding.ActivityMainBinding.inflate
import com.example.frequency.foundation.contract.*
import com.example.frequency.model.Options

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                //.add(R.id.main_container, MenuFragment())
                .commit()
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        updateUI()
        return true
    }

    private fun updateUI() {
        val fragment = supportFragmentManager.findFragmentById(R.id.appbar) // TODO MAIN CONTAINER

        if (fragment is ProvidesCustomTitle) {
            //binding.toolBar.title = getString(fragment.getTitleRes())
        } else {
            //binding.toolBar.title = getString(R.string.foo)
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

        if (fragment is HasCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        } else {
            //binding.toolBar.menu.clear()
        }
    }

    private fun createCustomToolbarAction(action: CustomAction) {
        //binding.toolBar.menu.clear()

        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
        iconDrawable.setTint(Color.WHITE)

        /*val menuItem = binding.toolBar.menu.add(action.textRes)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.onCustomAction.run()
            return@setOnMenuItemClickListener true
        }*/
    }

    private fun openFragment(
        fragment: Fragment,
        clearBackstack: Boolean = false
    ) {
        if (clearBackstack) {
            clearBackStack()
        }
        supportFragmentManager
            .beginTransaction()
            /*.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )*/
            .addToBackStack(fragment.javaClass.name)
            //.replace(R.id.main_container, fragment, fragment.javaClass.name)
            .commit()
    }

    private fun clearBackStack() =
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    override fun openSplash() {
        TODO("Not yet implemented")
    }

    override fun openSignInRequest() {
        TODO("Not yet implemented")
    }

    override fun openSignUp() {
        TODO("Not yet implemented")
    }

    override fun openHomeScreen(options: Options) {
        TODO("Not yet implemented")
    }

    override fun openSettings() {
        TODO("Not yet implemented")
    }

    override fun openSong() {
        TODO("Not yet implemented")
    }

    override fun openLyrics() {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun goToMenu() {
        clearBackStack()
    }

    override fun <T : Parcelable> provideResult(result: T) {
        supportFragmentManager.setFragmentResult(
            result.javaClass.name,
            bundleOf(KEY_RESULT to result)
        )
    }

    override fun <T : Parcelable> listenResults(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    ) {
        supportFragmentManager.setFragmentResultListener(
            clazz.name,
            owner // viewModelOwner from fragment
        ) { _, bundle -> listener.invoke(bundle.getParcelable(KEY_RESULT)!!) } // FragmentResultListener arguments
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        @JvmStatic
        private val KEY_RESULT = "KEY_RESULT"
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }


}