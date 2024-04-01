package com.cs4520.assignment5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cs4520.assignment5.databinding.LoginViewBinding

class LoginFragment:Fragment(R.layout.login_view) {
    private var _login_view_binding: LoginViewBinding? = null
    private val login_view_binding get() = _login_view_binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _login_view_binding = LoginViewBinding.inflate(inflater, container, false)

        val loginButton = _login_view_binding!!.loginButton
        val usernameText = _login_view_binding!!.usernameInput
        val passwordText = _login_view_binding!!.passwordInput

        val navController = findNavController()
        loginButton.setOnClickListener {
            val username = usernameText.text.toString()
            val password = passwordText.text.toString()

            if (username == "admin" && password == "admin"){
                navController.navigate(R.id.action_loginFragment_to_productListFragment)
                usernameText.setText("")
                passwordText.setText("")
            }
            else {
                Toast.makeText(login_view_binding.root.context, getString(R.string.login_error),Toast.LENGTH_SHORT).show()
            }
        }
        return login_view_binding.root
    }
}