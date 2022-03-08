package com.example.frequency

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.frequency.databinding.ActivityMainBinding
import com.example.frequency.databinding.ActivityMainBinding.inflate
import com.example.frequency.databinding.NavigationMenuLayoutBinding
import com.example.frequency.foundation.contract.Navigator
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.ResultListener
import com.example.frequency.foundation.model.Action
import com.example.frequency.model.Options
import com.example.frequency.model.actions.MenuAction
import com.example.frequency.screen.home.HomeFragment
import com.example.frequency.screen.profile.ProfileFragment
import com.example.frequency.screen.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainVM>()

    private lateinit var navigationMenu: NavigationMenuLayoutBinding

    private val currentFragment: Fragment get() = supportFragmentManager.findFragmentById(R.id.main_container)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        binding = inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)
        navigationMenu = binding.navMenu

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_container, HomeFragment())
                .commit()
        }


        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    private fun setNavMenuButton() {

        with(navigationMenu) {
            navProfileMb.setOnClickListener {
                /*if ( ) {
                    openFragment(ProfileFragment())
                } else {

                }*/


            }


        }

    }

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUI()
            navigationMenu.root.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        updateUI()
        return true
    }

    private fun updateUI() {
        val fragment = currentFragment

        if (fragment is ProvidesCustomTitle) {
            binding.toolbar.title = getString(fragment.getTitleRes())
            binding.toolbar.isTitleCentered = true
        } else {
            binding.toolbar.title = ""
        }

        if (navigationMenu.root.isVisible) {
            // show arrow
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

        if (fragment is ProvidesCustomActions) {
            createCustomToolbarAction(fragment.getCustomActions())
        } else {
            binding.toolbar.menu.clear()
        }
    }

    private fun createCustomToolbarAction(actions: List<Action>) {
        binding.toolbar.menu.clear()

        for (action in actions) {
            if (action is MenuAction) {
                val iconDrawableMenu =
                    DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
                iconDrawableMenu.setTint(Color.WHITE)
                binding.toolbar.navigationIcon = iconDrawableMenu
                binding.toolbar.setNavigationContentDescription(action.textRes)
                binding.toolbar.setNavigationOnClickListener {
                    expandMenu()
                }

            } else {
                val iconDrawable =
                    DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
                iconDrawable.setTint(Color.WHITE)

                val menuItem = binding.toolbar.menu.add(action.textRes)
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                menuItem.icon = iconDrawable
                menuItem.setOnMenuItemClickListener {
                    action.onCustomAction?.run()
                    return@setOnMenuItemClickListener true
                }
            }
        }
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
            .replace(R.id.main_container, fragment, fragment.javaClass.name)
            .commit()
    }

    private fun clearBackStack() =
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    private fun expandMenu() {
        navigationMenu.root.isVisible = !navigationMenu.root.isVisible
        updateUI()
    }

    override fun openSignInRequest() {
        //openFragment()
    }

    override fun openSignUp() {
        //TODO("Not yet implemented")
    }

    override fun openHomeScreen(options: Options) {
        openFragment(HomeFragment())
    }

    override fun openSettings() {
        openFragment(SettingsFragment())
    }

    override fun openSong() {
        //TODO("Not yet implemented")
    }

    override fun openLyrics() {
        //TODO("Not yet implemented")
    }

    override fun openProfile() {
        openFragment(ProfileFragment())
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

    override fun onBackPressed() {
        if (navigationMenu.root.isVisible) {
            expandMenu()
        } else {
            super.onBackPressed()
        }
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