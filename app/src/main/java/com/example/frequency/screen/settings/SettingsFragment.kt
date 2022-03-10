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
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.SettingTags.EMAIL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), ProvidesCustomTitle, ProvidesCustomActions {

    private val viewModel by viewModels<SettingsVM>()

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

        val emailField = findPreference<Preference>(EMAIL)
        emailField?.summary = viewModel.usersEmailLD.value

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