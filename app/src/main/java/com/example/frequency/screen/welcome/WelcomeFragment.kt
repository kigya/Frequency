package com.example.frequency.screen.welcome

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.viewModels
import com.example.frequency.R
import com.example.frequency.databinding.FragmnetWelcomeBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.AuthFragments
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.model.User
import com.example.frequency.screen.home.HomeFragment
import com.example.frequency.screen.sign_in.SignInFragment
import com.example.frequency.utils.ERROR
import com.example.frequency.utils.FAILURE
import com.example.frequency.utils.SUCCESS
import com.example.frequency.utils.showSnackbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BaseFragment(), AuthFragments, ProvidesCustomTitle {

    private val launchGoogleRegister =
        registerForActivityResult(
            StartActivityForResult(),
            ::onUserDataReceived
        )
    private lateinit var auth: FirebaseAuth

    override val viewModel by viewModels<WelcomeVM>()

    private var _binding: FragmnetWelcomeBinding? = null
    private val binding get() = _binding!!

    private val currentUser: User? get() = viewModel.registerUserLD.value

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
        _binding = FragmnetWelcomeBinding.inflate(inflater, container, false)
        // initialise listeners

        initiateListeners()
        initiateObservers()
        return binding.root
    }

    private fun initiateListeners() {
        with(binding) {
            continueGoogle.setOnClickListener {
                launchGoogleRegister.launch(getClient().signInIntent)
            }
            continueEmailButton.setOnClickListener {
                navigator().openSignIn()
            }
            clickSignUpWelcome.setOnClickListener {
                navigator().openSignUp()
            }


        }


    }

    private fun initiateObservers() {
        viewModel.registerUserLD.observe(viewLifecycleOwner) {
            provideDataFirebase(it.gToken)
        }


    }

    private fun onUserDataReceived(result: ActivityResult) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                viewModel.registerUser(
                    account.displayName.toString(),
                    account.email.toString(),
                    account.photoUrl ?: Uri.EMPTY,
                    account.idToken.toString(),
                )
                showSnackbar(binding.root, "Google authorization success!", SUCCESS)
            } else {
                Log.d(TAG, "account == null")
            }
        } catch (e: ApiException) {
            showSnackbar(binding.root, "Error ${e.message.toString()} ApiException", ERROR)
        }

    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestProfile()
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun provideDataFirebase(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            val user = currentUser
            if (it.isSuccessful) {
                if (user != null) {
                    viewModel.addUserToShearedPrefs(user.name, user.email, user.icon, user.gToken)
                    navigator().provideResult(user)
                }
                showSnackbar(binding.root, getString(R.string.auth_success), SUCCESS)
                navigator().openFragment(
                    HomeFragment(),
                    clearBackstack = true,
                    addToBackStack = false
                )
            } else {
                showSnackbar(binding.root, getString(R.string.auth_fail), FAILURE)

            }
        }
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