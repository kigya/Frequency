package com.example.frequency.screen.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.frequency.R
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.SettingTags.EMAIL
import com.example.frequency.utils.SettingTags.USERNAME
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

        findPreference<Preference>(USERNAME)?.setOnPreferenceChangeListener { _, newValue ->
            val user = currentUser
            navigator().provideResult(user.copy(name = newValue as String))
            return@setOnPreferenceChangeListener true
        }

        val emailPref = findPreference<Preference>(EMAIL)
        emailPref?.summary = viewModel.usersEmailLD.value
    }

    override fun getTitleRes() = R.string.settings

    override fun getCustomActions() = listOf(menuAction)

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

}

const val USER_INFO = "USER_INFO"