package com.example.frequency.screen.authorization.sign_in

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
import com.example.frequency.screen.home.HomeFragment
import com.example.frequency.utils.observeEvent
import com.example.frequency.utils.showSnackbar
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

        initiateListeners()
        initiateObservers()
        return binding.root
    }

    private fun initiateListeners() {
        with(binding) {
            backIb.setOnClickListener {
                navigator().openWelcome()
            }
            toSignUpWelcomeBack.setOnClickListener {
                navigator().openSignUp()
            }
            logInWelcomeBack.setOnClickListener {
                // viewModel.login() TODO
            }

        }
    }

    private fun initiateObservers() {
        with(viewModel) {
            showPbLd.observeEvent(viewLifecycleOwner) {
                navigator().showProgress(it)
            }
            showSnackBar.observeEvent(viewLifecycleOwner) {
                showSnackbar(binding.root, getString(it.message), it.iconTag)
            }
            navigateToHome.observeEvent(viewLifecycleOwner) {
                with(navigator()) {
                    provideResult(it)
                    openFragment(
                        HomeFragment(),
                        clearBackstack = true,
                        addToBackStack = false
                    )
                }
            }
        }

    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) {
        binding.inputEmailAddressWelcomeBack.error =
            if (it.emptyEmailError) getString(R.string.field_is_empty) else null
        binding.inputPasswordAddressWelcomeBack.error =
            if (it.emptyPasswordError) getString(R.string.field_is_empty) else null

        navigator().showProgress(it.showProgress)
    }


    private fun provideDataFirebase(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                //showSnackbar(getString(R.string.auth_success))
                navigator().openFragment(
                    HomeFragment(),
                    clearBackstack = true,
                    addToBackStack = false
                )
            } else {
                //showSnackbar(getString(R.string.auth_fail))
            }
        }
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