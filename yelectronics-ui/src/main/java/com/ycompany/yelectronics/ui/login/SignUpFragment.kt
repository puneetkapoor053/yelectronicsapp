package com.ycompany.yelectronics.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.SignUpFragmentBinding
import com.ycompany.yelectronics.utils.Extensions.toast
import com.ycompany.yelectronics.utils.ProgressLoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
        binding = SignUpFragmentBinding.inflate(inflater, container, false)

        binding?.signUpBtn?.setOnClickListener {
            signUp()
        }
        binding?.signInButton?.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun signUp() {
        if (binding?.passwordSignUpEditText?.text.toString() != binding?.confirmPasswordEditText?.text.toString()) {
            activity?.applicationContext?.let { toast("Password not Match", it) }
            return
        }
        progressLoadingDialog.startLoadingDialog("Please wait while we sign you up!!")
        firebaseAuth.createUserWithEmailAndPassword(
            binding?.emailSignUpEditText?.text.toString(),
            binding?.passwordSignUpEditText?.text.toString()
        )

            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    activity?.applicationContext?.let { toast("Sign Up Successfully", it) }
                    progressLoadingDialog.dismissDialog()


                    //val user = User(fullname,"",firebaseAuth.uid.toString(),emailV,"","")

                    //storeUserData(user)

//                    val intent = Intent(this, HomeActivity::class.java)
//                    startActivity(intent)
//                    finish()
                } else {
                    progressLoadingDialog.dismissDialog()
                    activity?.applicationContext?.let { toast("failed to Authenticate !", it) }
                }
            }
    }

    companion object {
        fun getInstance(): SignUpFragment {
            return SignUpFragment()
        }
    }
}