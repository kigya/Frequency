package com.example.frequency.screen.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.frequency.MainActivity
import com.example.frequency.R
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.ActionStore.provideProfileAction
import com.example.frequency.utils.PreferenceTags.EMAIL
import com.example.frequency.utils.PreferenceTags.USERNAME
import com.example.frequency.utils.dialog_fragment.SimpleDialogFragment
import com.example.frequency.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), ProvidesCustomTitle, ProvidesCustomActions {

    private val viewModel by viewModels<SettingsVM>()

    private val currentUser get() = viewModel.userLD.value!!

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rootView.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.ebony_clay
            )
        )

        val emailPref = findPreference<Preference>(EMAIL)
        emailPref?.summary = viewModel.usersEmailLD.value


        initiateListeners()
        initiateObservers()
        setSimpleDialogFragmentListener()
    }

    private fun initiateListeners() {
        findPreference<Preference>(USERNAME)?.setOnPreferenceChangeListener { _, newValue ->
            val user = currentUser
            navigator().provideResult(user.copy(name = newValue as String))
            return@setOnPreferenceChangeListener true
        }
        val logout = findPreference<Preference>(LOGOUT)
        logout?.setOnPreferenceClickListener {
            SimpleDialogFragment.show(
                parentFragmentManager,
                getString(R.string.logout_q),
                getString(R.string.sure_q),
                "Logout", "Close"
            )
            return@setOnPreferenceClickListener true
        }
    }

    private fun initiateObservers() {
        viewModel.launchReset.observeEvent(viewLifecycleOwner) {
            triggerRebirth(requireContext())
        }
        viewModel.showPbLd.observeEvent(viewLifecycleOwner){
            navigator().showProgress(it)
        }
    }

    private fun setSimpleDialogFragmentListener() {
        SimpleDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) {
            viewModel.showPB(true)
            viewModel.clearUserRootPreferences()
        }
    }

    private fun triggerRebirth(context: Context) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
        Runtime.getRuntime().exit(0)
        viewModel.showPB(false)
    }


    override fun getTitleRes() = R.string.settings

    override fun getCustomActions() = listOf(menuAction, provideProfileAction() {
        navigator().openProfile()
    })

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment().apply {
            arguments = Bundle().apply {

            }
        }

        @JvmStatic
        private val LOGOUT = "LOGOUT"

    }

}

const val USER_INFO = "USER_INFO"