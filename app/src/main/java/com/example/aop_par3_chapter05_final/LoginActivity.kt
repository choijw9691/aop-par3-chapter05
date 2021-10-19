package com.example.aop_par3_chapter05_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aop_par3_chapter05_final.databinding.ActivityLoginBinding
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var callbackManager: CallbackManager;
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
        binding.facebookLoginButton.setPermissions("email", "public_profile");
        binding.facebookLoginButton.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    result?.let { handleFacebookAccessToken(it.accessToken) }
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException?) {
                    TODO("Not yet implemented")
                }

            })


    }

    fun init() {
        callbackManager = CallbackManager.Factory.create();
        auth = Firebase.auth
        database = Firebase.database.reference
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        //    updateUI(currentUser)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        //    Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("JIWOUNG", "LoginSucess: " + (user?.email ?: "null"))
                    //      updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    //    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //  updateUI(null)
                }
            }
    }
    //Firebase.auth.signOut()
}