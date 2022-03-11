package com.example.frequency

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
import com.example.frequency.foundation.contract.Navigator
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.ResultListener
import com.example.frequency.foundation.model.Action
import com.example.frequency.foundation.views.AuthFragments
import com.example.frequency.model.User
import com.example.frequency.model.actions.MenuAction
import com.example.frequency.model.actions.ProfileAction
import com.example.frequency.screen.authorization.sign_in.SignInFragment
import com.example.frequency.screen.authorization.sign_up.SignUpFragment
import com.example.frequency.screen.authorization.welcome.WelcomeFragment
import com.example.frequency.screen.contact_us.ContactUsFragment
import com.example.frequency.screen.home.HomeFragment
import com.example.frequency.screen.lyrics.LyricsFragment
import com.example.frequency.screen.profile.ProfileFragment
import com.example.frequency.screen.settings.SettingsFragment
import com.example.frequency.screen.song.SongFragment
import com.example.frequency.utils.isValidEmail
import com.example.frequency.utils.setToolbarUserIcon
import com.example.frequency.utils.setUserImageByGlide
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainVM>()

    private val currentFragment: Fragment get() = supportFragmentManager.findFragmentById(R.id.main_container)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply { setKeepOnScreenCondition { viewModel.isLoading.value } }
        binding = inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        // set default night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        appStatusCheckAndStart(savedInstanceState)

        setListeners()
        setObservers()
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    private fun appStatusCheckAndStart(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val userEmail = viewModel.userLD.value?.email
            val autologin = viewModel.autologinLD.value

            if (isValidEmail(userEmail) && autologin == true) {
                openFragment(HomeFragment(), firstTime = true)
            } else {
                //openFragment(SettingsFragment(), firstTime = true)
                openFragment(WelcomeFragment(), firstTime = true)
            }
        }
    }

    private fun setListeners() {

        with(binding) {

            navMenuUserIb.setOnClickListener {
                openProfile()
            }
            navMenuLoginButton.setOnClickListener {
                openProfile()
            }
            navProfileMb.setOnClickListener {
                openProfile()
            }
            navLikedSongsMb.setOnClickListener {
                openSong()
            }
            navContactUsMb.setOnClickListener {
                openContactUs()
            }
            navSettingsMb.setOnClickListener {
                openSettings()
            }
            listenResults(User::class.java, this@MainActivity) {
                viewModel.updateUser(it)
            }


        }

    }

    private fun setObservers() {
        viewModel.userLD.observe(this) {
            setUserImageByGlide(this@MainActivity, binding.navMenuUserIb, it.icon, 20)
            setImageAndVisibility(currentFragment)
            binding.navMenuLoginButton.text = it.name
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
            changeNavigationStatusAndIcon(hideNavigationOnly = true)
            viewModel.updateUser()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val fragment = currentFragment
        titleFormatting(fragment)

        // проверка фрагмента (установлен ли Profile)
        setImageAndVisibility(fragment)

        return true
    }

    private fun setImageAndVisibility(fragment: Fragment) {
        val menuItem = binding.toolbar.menu.findItem(R.id.profile)

        if (menuItem !is MenuItem) return

        val visibilityStatus = fragment is ProvidesCustomActions && fragment.getCustomActions()
            .any { it is ProfileAction }

        binding.toolbar.menu.findItem(R.id.profile).isVisible = visibilityStatus

        if (visibilityStatus) {
            setToolbarUserIcon(this, menuItem, viewModel.userLD.value?.icon, 20)
            menuItem.actionView.setOnClickListener {
                openProfile()
            }
        }
    }

    private fun updateUI() {
        val fragment = currentFragment
        titleFormatting(fragment)
        if (fragment is ProvidesCustomActions) {
            createCustomToolbarAction(fragment.getCustomActions())
        } else {
            binding.toolbar.menu.clear()
        }
    }

    private fun titleFormatting(fragment: Fragment) {
        if (fragment is ProvidesCustomTitle) {
            binding.toolbar.title = getString(fragment.getTitleRes())
            binding.toolbar.isTitleCentered = true
        } else {
            binding.toolbar.title = ""
        }
    }

    private fun createCustomToolbarAction(actions: List<Action>) {
        binding.toolbar.menu.clear()

        for (action in actions) {
            when (action) {
                is MenuAction -> {
                    val iconDrawableMenu =
                        DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
                    iconDrawableMenu.setTint(Color.WHITE)

                    binding.toolbar.navigationIcon = iconDrawableMenu
                    binding.toolbar.setNavigationContentDescription(action.textRes)
                    binding.toolbar.setNavigationOnClickListener {
                        changeNavigationStatusAndIcon()
                    }
                }
                is ProfileAction -> {
                    binding.toolbar.inflateMenu(R.menu.menu)

                    val menuItem = binding.toolbar.menu.findItem(R.id.profile)
                    menuItem.isVisible = true
                    val profileImage: CircleImageView =
                        menuItem.actionView.findViewById(R.id.toolbar_profile_image)
                    setUserImageByGlide(this, profileImage, viewModel.userLD.value?.icon, 20)
                    profileImage.setOnClickListener {
                        action.onCustomAction.run()
                    }
                }
            }
        }
    }

    override fun openFragment(
        fragment: Fragment,
        firstTime: Boolean,
        addToBackStack: Boolean,
        clearBackstack: Boolean
    ) {
        if (clearBackstack) {
            clearBackStack()
        }
        when {
            firstTime -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.main_container, fragment)
                    .commit()
            }
            addToBackStack -> {
                supportFragmentManager
                    .beginTransaction() //TODO Animation
                    .setCustomAnimations(
                        R.anim.enter,
                        R.anim.exit,
                        R.anim.pop_enter,
                        R.anim.pop_exit
                    )
                    .addToBackStack(fragment.javaClass.name)
                    .replace(R.id.main_container, fragment, fragment.javaClass.name)
                    .commit()
            }
            else -> {
                supportFragmentManager
                    .beginTransaction() //TODO Animation
                    .setCustomAnimations(
                        R.anim.enter,
                        R.anim.exit,
                        R.anim.pop_enter,
                        R.anim.pop_exit
                    )
                    .replace(R.id.main_container, fragment, fragment.javaClass.name)
                    .commit()
            }
        }
    }

    override fun showProgress(state: Boolean) {
        binding.pbMain.isVisible = state
    }

    override fun clearBackStack() =
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    private fun changeNavigationStatusAndIcon(
        hideNavigationOnly: Boolean = false,
        hideNavAndSetMenuIcon: Boolean = false
    ) {
        val navigationStatus = !binding.navigationMenu.isVisible

        when {
            hideNavigationOnly && !hideNavAndSetMenuIcon -> {
                binding.navigationMenu.isVisible = false
            }
            hideNavAndSetMenuIcon -> {
                binding.navigationMenu.isVisible = false
                binding.toolbar.setNavigationIcon(R.drawable.ic_navigation_menu)
            }
            else -> {
                if (navigationStatus) {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)
                    binding.toolbar.setNavigationIconTint(Color.WHITE)
                } else {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_navigation_menu)
                }
                binding.navigationMenu.isVisible = navigationStatus
            }
        }

    }

    override fun openWelcome() {
        if (currentFragment !is WelcomeFragment) {
            openFragment(WelcomeFragment.newInstance())
        }
    }

    override fun openSignIn() {
        if (currentFragment !is SignInFragment) {
            openFragment(SignInFragment.newInstance())
        }
    }

    override fun openSignUp() {
        if (currentFragment !is SignUpFragment) {
            openFragment(SignUpFragment.newInstance())
        }
    }

    override fun openHome(
        fragment: Fragment,
        clear: Boolean,
        add: Boolean,
        user: User?
    ) {
        openFragment(HomeFragment.newInstance(), clearBackstack = clear, addToBackStack = add)
    }

    override fun openSettings() {
        if (currentFragment !is SettingsFragment) {
            openFragment(SettingsFragment.newInstance())
        }
    }

    override fun openSong() {
        if (currentFragment !is SongFragment) {
            openFragment(SongFragment.newInstance())
        }
    }

    override fun openLyrics() {
        if (currentFragment !is LyricsFragment) {
            openFragment(LyricsFragment.newInstance())
        }
    }

    override fun openProfile() {
        if (currentFragment !is ProfileFragment) {
            openFragment(ProfileFragment())
        }
    }

    override fun openContactUs() {
        if (currentFragment !is ContactUsFragment) {
            openFragment(ContactUsFragment())
        }
    }

    override fun openFaqs() {
        // TODO openFragment()
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
        Log.d(TAG, result.toString())
    }

    override fun <T : Parcelable> listenResults(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    ) {
        supportFragmentManager.setFragmentResultListener(
            clazz.name, // tracking received class instances by names
            owner // viewModelOwner from fragment
        ) { _, bundle -> listener.invoke(bundle.getParcelable(KEY_RESULT)!!) } // FragmentResultListener arguments
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (binding.navigationMenu.isVisible) {
            changeNavigationStatusAndIcon()
            return
        }
        when (currentFragment) {
            is AuthFragments -> {
                super.onBackPressed()
            }
            !is HomeFragment -> openFragment(
                HomeFragment.newInstance(),
                clearBackstack = true,
                addToBackStack = false
            )
            else -> super.onBackPressed()
        }

    }

    companion object {
        @JvmStatic
        private val KEY_RESULT = "KEY_RESULT"

        @JvmStatic
        private val TAG = this::class.java.simpleName

    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

}