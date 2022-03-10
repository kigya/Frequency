package com.example.frequency.screen.sign_in

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
import com.example.frequency.databinding.FragmentSignInBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.AuthFragments
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.screen.home.HomeFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment(), AuthFragments, ProvidesCustomTitle {

    private val launchGoogleRegister =
        registerForActivityResult(
            StartActivityForResult(),
            ::onUserDataReceived
        )
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

        /*binding.gogSignIn.setOnClickListener {
            launchGoogleRegister.launch(getClient().signInIntent)
        }*/

        initiateObservers()
        return binding.root
    }

    private fun initiateObservers() {

        /*viewModel.registerUserLD.observe(viewLifecycleOwner) {
            provideDataFirebase(it.gToken)
            binding.includedProfileInfo.userNameTv.text = it.name
            setUserImageByGlide(requireContext(), binding.includedProfileInfo.userIcon, it.icon)
            binding.includedProfileInfo.central.isVisible = true
        }*/


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
            } else {
                Log.d(TAG, "account == null")
            }
        } catch (e: ApiException) {
            showSnackbar("Error ${e.message.toString()} ApiException")
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
            if (it.isSuccessful) {
                showSnackbar(getString(R.string.auth_success))
                navigator().openFragment(HomeFragment(), clearBackstack = true, addToBackStack = false)
            } else {
                showSnackbar(getString(R.string.auth_fail))
            }
        }
    }

    private fun showSnackbar(message: String) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackBar.view.setOnClickListener {
            snackBar.dismiss()
        }
        snackBar.show()
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