package com.example.frequency.ui.screens.authorization.sign_up

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.frequency.R
import com.example.frequency.databinding.FragmentSignUpBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.AuthFragments
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.datasource.network.sign_up.validation.SignUpData
import com.example.frequency.ui.screens.sation_lists.home.HomeFragment
import com.example.frequency.utils.SummaryUtils.showSnackbar
import com.example.frequency.utils.observeEvent
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment(), AuthFragments, ProvidesCustomTitle {

    override val viewModel by viewModels<SignUpVM>()

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialise all data
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        // initialise listeners

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        initiateListeners()
        initiateObservers()
    }

    private fun initiateListeners() {
        with(binding) {
            logInCreate.setOnClickListener {
                onCreateAccountButtonPressed()
                clearAllFieldError()
            }
            clickSignInCreateAccount.setOnClickListener {
                navigator().openSignIn()
            }
            backIb.setOnClickListener {
                navigator().goBack()
            }

        }

        doOnUseInput()
    }

    private fun initiateObservers() {
        with(viewModel) {
            showSnackBar.observeEvent(viewLifecycleOwner) {
                showSnackbar(binding.root, getString(it.message), it.iconTag)
            }
            navigateToHome.observeEvent(viewLifecycleOwner) {
                with(navigator()) {
                    provideResult(it)
                    openFragment(
                        HomeFragment(),
                        clearBackstack = true
                    )
                }
            }
        }

    }

    private fun onCreateAccountButtonPressed() {
        val signUpData = SignUpData(
            username = binding.inputUsernameCreate.editText?.text.toString().trim(),
            email = binding.inputEmailAddressCreateAccount.editText?.text.toString().trim(),
            password = binding.inputPasswordAddressCreateAccount.editText?.text.toString().trim(),
            repeatPassword = binding.inputConfirmPasswordAddressCreateAccount.editText?.text.toString()
                .trim(),
        )
        viewModel.createAccount(signUpData)
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) { state ->
        with(binding) {
            with(state) {
                inputUsernameCreate.isEnabled = enableViews
                inputEmailAddressCreateAccount.isEnabled = enableViews
                inputPasswordAddressCreateAccount.isEnabled = enableViews
                inputConfirmPasswordAddressCreateAccount.isEnabled = enableViews
                backIb.isEnabled = enableViews
                clickSignInCreateAccount.isEnabled = enableViews
                logInCreate.isEnabled = enableViews

                fillError(inputUsernameCreate, usernameErrorMessageRes)
                fillError(inputEmailAddressCreateAccount, emailErrorMessageRes)
                fillError(inputPasswordAddressCreateAccount, passwordErrorMessageRes)
                fillError(inputConfirmPasswordAddressCreateAccount, repeatPasswordErrorMessageRes)
            }
        }
        navigator().showProgress(state.showProgress)
        Log.d(TAG, "${state.showProgress}")
    }

    private fun fillError(input: TextInputLayout, @StringRes stringRes: Int) {
        if (stringRes == SignUpVM.NO_ERROR_MESSAGE) {
            input.error = null
            input.isErrorEnabled = false
        } else {
            input.error = getString(stringRes)
            input.isErrorEnabled = true
        }
    }

    private fun doOnUseInput() {
        with(binding) {
            inputUsernameCreate.editText?.doOnTextChanged { _, _, _, _ ->
                inputUsernameCreate.error = null
            }
            inputEmailAddressCreateAccount.editText?.doOnTextChanged { _, _, _, _ ->
                inputEmailAddressCreateAccount.error = null
            }
            inputPasswordAddressCreateAccount.editText?.doOnTextChanged { _, _, _, _ ->
                inputPasswordAddressCreateAccount.error = null
            }
            inputConfirmPasswordAddressCreateAccount.editText?.doOnTextChanged { _, _, _, _ ->
                inputConfirmPasswordAddressCreateAccount.error = null
            }
        }
    }

    private fun clearAllFieldError() {
        with(binding) {
            inputUsernameCreate.error = null
            inputEmailAddressCreateAccount.error = null
            inputPasswordAddressCreateAccount.error = null
            inputConfirmPasswordAddressCreateAccount.error = null
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTitleRes() = R.string.sign_up

    companion object {

        @JvmStatic
        private val TAG = SignUpFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = SignUpFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}