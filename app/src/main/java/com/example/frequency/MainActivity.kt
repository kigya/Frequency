package com.example.frequency

import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frequency.FrequencyApplication.Companion.FREQUENCY_CHANNEL
import com.example.frequency.databinding.ActivityMainBinding
import com.example.frequency.databinding.ActivityMainBinding.inflate
import com.example.frequency.foundation.contract.Navigator
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.ResultListener
import com.example.frequency.foundation.model.Action
import com.example.frequency.model.User
import com.example.frequency.model.actions.MenuAction
import com.example.frequency.model.actions.ProfileAction
import com.example.frequency.datasource.network.radio_browser.models.Station
import com.example.frequency.ui.screens.PreviewFragment
import com.example.frequency.ui.screens.WaitFragment
import com.example.frequency.ui.screens.authorization.sign_in.SignInFragment
import com.example.frequency.ui.screens.authorization.sign_up.SignUpFragment
import com.example.frequency.ui.screens.authorization.welcome.WelcomeFragment
import com.example.frequency.ui.screens.sation_lists.home.HomeFragment
import com.example.frequency.ui.screens.info.contact_us.ContactUsFragment
import com.example.frequency.ui.screens.info.profile.ProfileFragment
import com.example.frequency.ui.screens.lyrics.LyricsFragment
import com.example.frequency.ui.screens.settings.SettingsFragment
import com.example.frequency.ui.screens.station.StationFragment
import com.example.frequency.utils.SummaryUtils.showSnackbar
import com.example.frequency.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainVM>()

    private val currentFragment: Fragment get() = supportFragmentManager.findFragmentById(R.id.main_container)!!

    private val currentUser get() = viewModel.userLD.value!!

    private var userIconUri = Uri.EMPTY

    private val googleRegisterLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onUserDataReceived
        )

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
            openFragment(WaitFragment(), clearBackstack = true, addToBackStack = false)
            viewModel.signInWithFireBase()
        }
    }

    private fun onUserDataReceived(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            viewModel.getAccount(result)
        } else {
            openFragment(WelcomeFragment(), addToBackStack = true)
            showProgress(false)
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
            navLikedStationMb.setOnClickListener {
                sendNotification()
            }
            navContactUsMb.setOnClickListener {
                openContactUs()
            }
            navSettingsMb.setOnClickListener {
                openSettings()
            }
            navFaqsMb.setOnClickListener {
                val webIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.projectPage)))
                startActivity(webIntent)
            }
            listenResults(User::class.java, this@MainActivity) {
                viewModel.updateUser(it)
            }
            ll.setOnClickListener {
                changeNavigationStatusAndIcon()
            }
            navMenuBar.setOnClickListener {
                changeNavigationStatusAndIcon()
            }

        }

    }

    private fun setObservers() {
        with(viewModel) {
            userLD.observe(this@MainActivity) { user ->
                setUserInfo(user)
            }
            showSnackBar.observeEvent(this@MainActivity) {
                showSnackbar(binding.root, getString(it.message, it.additional ?: ""), it.iconTag)
            }
            navigateToHome.observeEvent(this@MainActivity) {
                openFragment(HomeFragment(), addToBackStack = true)
            }
            navigateToWelcome.observeEvent(this@MainActivity) {
                openFragment(WelcomeFragment(), addToBackStack = true)
            }
            viewModel.state.observe(this@MainActivity) {
                showProgress(it.showProgress)
            }
            viewModel.takeNewGToken.observeEvent(this@MainActivity) {
                googleRegisterLauncher.launch(it.signInIntent)
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

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val fragment = currentFragment
        titleFormatting(fragment)
        // ???????????????? ?????????????????? (???????????????????? ???? Profile)
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
            setToolbarUserIcon(this, menuItem, viewModel.userLD.value?.icon, 10)
            menuItem.actionView.setOnClickListener {
                openProfile()
            }
        }
    }

    private fun setToolbarUserIcon(
        requireContext: Context,
        item: MenuItem,
        uri: Uri?,
        timeout: Int = 0
    ) {
        val profileImage: CircleImageView =
            item.actionView.findViewById(R.id.toolbar_profile_image)
        Glide.with(requireContext)
            .load(uri)
            .timeout(timeout)
            .error(R.drawable.ic_unknown_user_photo)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(profileImage)
    }

    private fun setImageByGlide(
        requireContext: Context,
        view: ImageView,
        uri: Uri?,
        timeout: Int = 0
    ) {
        Glide.with(requireContext)
            .load(uri)
            .timeout(timeout)
            .error(R.drawable.ic_unknown_user_photo)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(view)
    }

    private fun updateUI() {
        val fragment = currentFragment
        titleFormatting(fragment)
        if (fragment is ProvidesCustomActions) {
            createCustomToolbarAction(fragment.getCustomActions())
        } else {
            binding.toolbar.menu.clear()
        }

        changeNavigationStatusAndIcon(hideNavigationOnly = true)
        viewModel.updateUser()
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
                    setImageByGlide(this, profileImage, viewModel.userLD.value?.icon, 100)
                    profileImage.setOnClickListener {
                        action.onCustomAction.run()
                    }
                }
            }
        }
    }

    private fun setUserInfo(user: User) {
        if (userIconUri != user.icon) {
            userIconUri = user.icon
            setImageByGlide(this@MainActivity, binding.navMenuUserIb, user.icon, 100)
            setImageAndVisibility(currentFragment)
        }
        if (binding.navMenuLoginButton.text != user.name) {
            binding.navMenuLoginButton.text = user.name
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
        Log.d(
            TAG,
            "OPEN ${fragment::class.java.signers} ${supportFragmentManager.backStackEntryCount}"
        )
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
        val navigationStatus = binding.navigationMenu.isVisible

        when {
            hideNavigationOnly && !hideNavAndSetMenuIcon -> {
                binding.navigationMenu.isVisible = false
            }
            hideNavAndSetMenuIcon -> {
                binding.navigationMenu.isVisible = false
                binding.toolbar.setNavigationIcon(R.drawable.ic_navigation_menu)
            }
            else -> {
                if (!navigationStatus) {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)
                    binding.toolbar.setNavigationIconTint(Color.WHITE)
                } else {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_navigation_menu)
                    disableSearchFocus(currentFragment)
                }
                binding.navigationMenu.isVisible = !navigationStatus
            }
        }

    }

    private fun homeCheck(fragment: Fragment) {
        if (currentFragment is HomeFragment || currentFragment is StationFragment ) {
            openFragment(fragment)
        } else {
            openFragment(fragment, addToBackStack = false)
        }
    }

    private fun welcomeCheck(fragment: Fragment) {
        if (currentFragment is WelcomeFragment) {
            openFragment(fragment)
        } else {
            openFragment(fragment, addToBackStack = false)
        }
    }

    override fun openWelcome() {
        if (currentFragment !is WelcomeFragment) {
            openFragment(WelcomeFragment.newInstance())
        }
    }

    override fun openSignIn() {
        welcomeCheck(SignInFragment.newInstance())
    }

    override fun openSignUp() {
        welcomeCheck(SignUpFragment.newInstance())
    }

    override fun openSettings() {
        if (currentFragment !is SettingsFragment) {
            homeCheck(SettingsFragment.newInstance())
        }
    }

    override fun openStation(station: Station) {
        homeCheck(StationFragment.newInstance(station))
    }

    override fun openLyrics() {
        homeCheck(LyricsFragment.newInstance())
    }

    override fun openProfile() {
        if (currentFragment !is ProfileFragment) {
            homeCheck(ProfileFragment.newInstance())
        }
    }

    override fun openContactUs() {
        if (currentFragment !is ContactUsFragment) {
            homeCheck(ContactUsFragment.newInstance())
        }
    }

    override fun openFaqs() {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.projectPage)))
        startActivity(webIntent)
    }

    override fun openPreview(uri: String) {
        if (currentFragment !is PreviewFragment) {
            openFragment(PreviewFragment.newInstance(uri), addToBackStack = false)
        }
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
        disableSearchFocus(currentFragment)

        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    private fun disableSearchFocus(fragment: Fragment) {
        if (fragment is HomeFragment) {
            val musicSearch = fragment.view?.findViewById<SearchView>(R.id.musicSearch)
            musicSearch?.let {
                if (musicSearch.isFocused) {
                    binding.root.requestFocus()
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun sendNotification() {
        val notyIntent = Intent()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this@MainActivity,
            0,
            notyIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder =
            NotificationCompat.Builder(this@MainActivity, FREQUENCY_CHANNEL)
                .setSmallIcon(R.drawable.ic_like_22)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.content_in_progress_notification))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(null)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    companion object {
        @JvmStatic
        private val KEY_RESULT = "KEY_RESULT"

        @JvmStatic
        val NOTIFICATION_ID = 235934624

        @JvmStatic
        private val TAG = MainActivity::class.java.simpleName

    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

}