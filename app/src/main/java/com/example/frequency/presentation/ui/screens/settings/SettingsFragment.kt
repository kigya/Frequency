package com.example.frequency.presentation.ui.screens.settings

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
import com.example.frequency.presentation.ui.MainActivity
import com.example.frequency.R
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.common.utils.ActionStore.menuAction
import com.example.frequency.common.utils.ActionStore.provideProfileAction
import com.example.frequency.common.utils.PreferenceTags.EMAIL
import com.example.frequency.common.utils.PreferenceTags.USERNAME
import com.example.frequency.common.utils.dialog_fragment.SimpleDialogFragment
import com.example.frequency.common.utils.observeEvent
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
                icon = R.drawable.ic_baseline_logout_24,
                title = R.string.logout_q,
                message = R.string.sure_q,
                posBut = R.string.logout_tb,
                neuBut = R.string.close_tb
            )
            return@setOnPreferenceClickListener true
        }
    }

    private fun initiateObservers() {
        viewModel.launchReset.observeEvent(viewLifecycleOwner) {
            triggerRebirth(requireContext())
        }
        viewModel.showPbLd.observeEvent(viewLifecycleOwner) {
            navigator().showProgress(it)
        }
    }

    private fun setSimpleDialogFragmentListener() {
        SimpleDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) { response ->
            when (response) {
                SimpleDialogFragment.POSITIVE_FRAG_RESPONSE -> {
                    viewModel.showPB(true)
                    viewModel.clearUserRootPreferences()
                }
                SimpleDialogFragment.NEGATIVE_FRAG_RESPONSE -> {}
                SimpleDialogFragment.NEUTRAL_FRAG_RESPONSE -> {}
            }
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

    override fun getCustomActions() = listOf(menuAction, provideProfileAction {
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