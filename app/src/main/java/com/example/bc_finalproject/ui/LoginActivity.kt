package com.example.bc_finalproject.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.bc_finalproject.R
import kotlinx.android.synthetic.main.activity_login.*

import android.text.method.PasswordTransformationMethod
import android.text.method.HideReturnsTransformationMethod

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var userPass : String = ""

        // Activates the login text box receives whatever the user inputs into the text field for login username
        loginPasswordField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.apply { loginButton.isEnabled = length > 0 }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //unused
            }
            override fun afterTextChanged(p0: Editable?) {
                //unused
            }

        } )

        showHideBtn.setOnClickListener {
            if(showHideBtn.text.toString().equals("Show")){
                loginPasswordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showHideBtn.text = "Hide"
            } else{
                loginPasswordField.transformationMethod = PasswordTransformationMethod.getInstance()
                showHideBtn.text = "Show"
            }
        }

        //
        loginButton.setOnClickListener {
            if (loginPasswordField.text.toString().length == 0) {
                loginPasswordField.setError("Password has not been entered")
                loginPasswordField.requestFocus()
            }

            else {
                if (userPass == ""){
                    userPass = loginPasswordField.text.toString()
                }
                else if (loginPasswordField.text.toString() != userPass){
                    loginPasswordField.setError("Incorrect Password")
                    loginPasswordField.requestFocus()
                }
                else {
                    // passes the username from login screen to wherever we decide to pull it from
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        putExtra(
                            "password",
                            loginPasswordField.text
                        )
                    })
                }

            }
        }
    }
}