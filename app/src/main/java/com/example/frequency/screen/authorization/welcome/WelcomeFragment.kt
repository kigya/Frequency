package com.example.frequency.screen.authorization.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.viewModels
import com.example.frequency.R
import com.example.frequency.databinding.FragmnetWelcomeBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.AuthFragments
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.screen.authorization.sign_in.SignInFragment
import com.example.frequency.screen.home.HomeFragment
import com.example.frequency.utils.SummaryUtils.showSnackbar
import com.example.frequency.utils.observeEvent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint


/**
 * Start screen
 * */
@AndroidEntryPoint
class WelcomeFragment : BaseFragment(), AuthFragments, ProvidesCustomTitle {

    private val googleRegisterLauncher =
        registerForActivityResult(
            StartActivityForResult(),
            ::onUserDataReceived
        )

    override val viewModel by viewModels<WelcomeVM>()

    private var _binding: FragmnetWelcomeBinding? = null
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
        _binding = FragmnetWelcomeBinding.inflate(inflater, container, false)
        // initialise listeners

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateListeners()
        initiateObservers()
    }

    private fun initiateListeners() {
        with(binding) {
            continueGoogle.setOnClickListener {
                viewModel.setGoogleSignInClient()
            }
            continueEmailButton.setOnClickListener {
                navigator().openSignIn()
            }
            clickSignUpWelcome.setOnClickListener {
                navigator().openSignUp()
            }
            logo.setOnClickListener {
                viewModel.increaseTapsCount()
            }

        }


    }

    private fun initiateObservers() {
        with(viewModel) {
            showPbLd.observeEvent(viewLifecycleOwner) {
                navigator().showProgress(it)
            }
            showSnackBar.observeEvent(viewLifecycleOwner) {
                showSnackbar(binding.root, getString(it.message, it.additional), it.iconTag)
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
            launchGoogleRegister.observeEvent(viewLifecycleOwner){
                googleRegisterLauncher.launch(it.signInIntent)
            }

            secretCurrentTaps.observe(viewLifecycleOwner) {
                if (it > 4) {
                    val webIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.projectPage)))
                    startActivity(webIntent)
                    Toast.makeText(requireContext(), "You got it.", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    /*private fun getClientOptions() = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestProfile()
        .requestEmail()
        .build()

    private fun getClient() = GoogleSignIn.getClient(
        requireActivity(),
        getClientOptions()
    )
*/
    private fun onUserDataReceived(result: ActivityResult) {
        viewModel.getAccount(result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private val TAG = SignInFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = WelcomeFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun getTitleRes() = R.string.sign_in

}