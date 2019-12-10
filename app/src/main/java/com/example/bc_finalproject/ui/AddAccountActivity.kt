package com.example.bc_finalproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bc_finalproject.Account
import com.example.bc_finalproject.R
import kotlinx.android.synthetic.main.activity_add_account.*

class AddAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        btnSave.setOnClickListener {
            if (edit_text_accountid.text.isEmpty()){
                Toast.makeText(this, "Please enter Account name", Toast.LENGTH_SHORT).show()
                edit_text_accountid.requestFocus()
            }
            else {
                val account = Account()
                account.accountid = edit_text_accountid.text.toString()
                if (edit_text_username.text.isEmpty()){
                    account.username = ""
                } else
                    account.username = edit_text_username.text.toString()
                if (edit_text_password.text.isEmpty()){
                    account.password = ""
                } else
                    account.password = edit_text_password.text.toString()

                if (edit_text_site.text.isEmpty()){
                    account.website = ""
                } else
                    account.website = edit_text_site.text.toString()
                if (edit_text_notes.text.isEmpty()){
                    account.notes = ""
                } else
                    account.notes = edit_text_notes.text.toString()

                MainActivity.dbHandler.addAccount(this, account)
                clearEdits()
                edit_text_accountid.requestFocus()
                finish()
            }

        }

        btnCancel.setOnClickListener {
            clearEdits()
            finish()
        }
    }

    private fun clearEdits() {
        edit_text_accountid.text.clear()
        edit_text_username.text.clear()
        edit_text_password.text.clear()
        edit_text_site.text.clear()
        edit_text_notes.text.clear()
    }
}
