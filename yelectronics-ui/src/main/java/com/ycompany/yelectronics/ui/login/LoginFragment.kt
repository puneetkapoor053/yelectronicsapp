package com.ycompany.yelectronics.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.ycompany.yelectronics.ui.R
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.LoginFragmentBinding
import com.ycompany.yelectronics.ui.home.HomeActivity
import com.ycompany.yelectronics.injections.CustomViewModelFactory
import com.ycompany.yelectronics.utils.Constants
import com.ycompany.yelectronics.utils.Extensions.toast
import com.ycompany.yelectronics.utils.ProgressLoadingDialog
import com.ycompany.yelectronics.viewmodel.StateData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding>() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory

    private lateinit var loginViewModel: LoginViewModel

    private val progressLoadingDialog: ProgressLoadingDialog by lazy {
        ProgressLoadingDialog(
            requireActivity()
        )
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Factory needed if we need constructor injection.
        loginViewModel = ViewModelProvider(this, customViewModelFactory)[LoginViewModel::class.java]

        loginViewModel.singInLiveDataObservable().observe(
            viewLifecycleOwner, processSignInData()
        )

        loginViewModel.signInAnonymouslyObservable().observe(
            viewLifecycleOwner, processSignInAnonymouslyData()
        )

        binding?.apply {
            loginButton.setOnClickListener {
                validateInfo()
            }
            signUpButton.setOnClickListener {
                val intent = Intent(context, SignUpActivity::class.java)
                startActivity(intent)
            }
            forgottenPasswordEditText.setOnClickListener {
                activity?.applicationContext?.let { toast("Feature In Progress!!", it) }
            }
            guestLogin.setOnClickListener {
                loginAsGuest()
            }
            emailEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (emailEditText.text?.isEmpty() == true) {
                        emailEditText.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            null,
                            null)
                    } else if (!TextUtils.isEmpty(emailEditText.text)
                        && loginViewModel.isEmailValid(emailEditText.text.toString())) {
                        emailEditText.setCompoundDrawablesWithIntrinsicBounds(
                            null, null, ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_check
                            ), null
                        )
                        emailEditTextError.visibility = View.GONE
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    emailEditText.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (!TextUtils.isEmpty(emailEditText.text)
                        && loginViewModel.isEmailValid(emailEditText.text.toString())) {
                        emailEditText.setCompoundDrawablesWithIntrinsicBounds(
                                null, null, ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_check
                                ), null
                            )
                            emailEditTextError.visibility = View.GONE
                        }
                }
            })
        }
    }

    private fun validateInfo() {
        binding?.apply {
            if (TextUtils.isEmpty(emailEditText.text)) {
                emailEditTextError.visibility = View.VISIBLE
                emailEditTextError.text = "Email Can't be Empty"
                return
            }
            if (loginViewModel.isEmailValid(emailEditText.text.toString())) {
                emailEditTextError.visibility = View.VISIBLE
                emailEditTextError.text = "Enter Valid Email"
                return
            }
            signIn()
        }
    }

    private fun signIn() {
        loginViewModel.signInUser(
            binding?.emailEditText?.text.toString(),
            binding?.passwordEditText?.text.toString()
        )
    }

    private fun processSignInData(): (t: StateData<Boolean>?) -> Unit = {
        if (it != null) {
            when (it.getStatus()) {
                StateData.DataStatus.LOADING -> {
                    progressLoadingDialog.startLoadingDialog("Please wait while we sign you in!!")
                }

                StateData.DataStatus.SUCCESS -> {
                    sharedPreferences.edit().putString(Constants.PREF_USERNAME, binding?.emailEditText?.text.toString()).apply()
                    progressLoadingDialog.dismissDialog()
                    activity?.applicationContext?.let { toast("Signed in successfully", it) }

                    val intent = Intent(context, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

                StateData.DataStatus.ERROR -> {
                    progressLoadingDialog.dismissDialog()
                    activity?.applicationContext?.let { toast("Sign in failed", it) }
                    sharedPreferences.edit().remove(Constants.PREF_USERNAME).apply()
                }

                else -> {
                    progressLoadingDialog.dismissDialog()
                    sharedPreferences.edit().remove(Constants.PREF_USERNAME).apply()
                    activity?.applicationContext?.let { toast("Sign in failed", it) }
                }
            }
        }
    }

    private fun processSignInAnonymouslyData(): (t: StateData<Boolean>?) -> Unit = {
        if (it != null) {
            when (it.getStatus()) {
                StateData.DataStatus.LOADING -> {
                    progressLoadingDialog.startLoadingDialog("Please wait!!")
                }

                StateData.DataStatus.SUCCESS -> {
                    sharedPreferences.edit().putString(Constants.PREF_USERNAME, Constants.PREF_GUEST_USERNAME).apply()
                    progressLoadingDialog.dismissDialog()
                    activity?.applicationContext?.let { toast("Signed in as guest successfully", it) }

                    val intent = Intent(context, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

                StateData.DataStatus.ERROR -> {
                    progressLoadingDialog.dismissDialog()
                    activity?.applicationContext?.let { toast("Sign in as guest failed", it) }
                    sharedPreferences.edit().remove(Constants.PREF_USERNAME).apply()
                }

                else -> {
                    progressLoadingDialog.dismissDialog()
                    sharedPreferences.edit().remove(Constants.PREF_USERNAME).apply()
                    activity?.applicationContext?.let { toast("Sign in failed", it) }
                }
            }
        }
    }

    private fun loginAsGuest() {
        loginViewModel.signInUserAnonymously()
    }

    companion object {
        fun getInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}