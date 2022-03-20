package com.example.frequency.ui.screens.authorization.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.frequency.R
import com.example.frequency.databinding.FragmentSignInBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.AuthFragments
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.ui.screens.home.HomeFragment
import com.example.frequency.utils.observeEvent
import com.example.frequency.utils.SummaryUtils.showSnackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment(), AuthFragments, ProvidesCustomTitle {

    private lateinit var auth: FirebaseAuth

    override val viewModel by viewModels<SignInVM>()

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialise all data
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        // initialise listeners

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        observeClearPasswordEvent()
        initiateListeners()
        initiateObservers()
    }

    private fun initiateListeners() {
        with(binding) {
            logInWelcomeBack.setOnClickListener {
                onSignInButtonPressed()
            }
            backIb.setOnClickListener {
                navigator().goBack()
            }
            toSignUpWelcomeBack.setOnClickListener {
                navigator().openSignUp()
            }


        }
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

    private fun onSignInButtonPressed() {
        viewModel.login(
            email = binding.inputEmailAddressWelcomeBack.editText?.text.toString().trim(),
            password = binding.inputPasswordAddressWelcomeBack.editText?.text.toString().trim()
        )
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) {
        binding.inputEmailAddressWelcomeBack.error =
            if (it.emptyEmailError) getString(R.string.field_is_empty) else null
        binding.inputPasswordAddressWelcomeBack.error =
            if (it.emptyPasswordError) getString(R.string.field_is_empty) else null

        with(binding) {
            inputEmailAddressWelcomeBack.isEnabled = it.enableViews
            inputPasswordAddressWelcomeBack.isEnabled = it.enableViews
            logInWelcomeBack.isEnabled = it.enableViews
            forgotPasswordText.isEnabled = it.enableViews
            backIb.isEnabled = it.enableViews
            toSignUpWelcomeBack.isEnabled = it.enableViews
        }

        navigator().showProgress(it.showProgress)
    }

    private fun observeClearPasswordEvent() =
        viewModel.clearPasswordEvent.observeEvent(viewLifecycleOwner) {
            binding.inputPasswordAddressWelcomeBack.editText?.text?.clear()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private val TAG = SignInFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = SignInFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun getTitleRes() = R.string.sign_in

}