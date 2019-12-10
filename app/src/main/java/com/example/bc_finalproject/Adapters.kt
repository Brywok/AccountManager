package com.example.bc_finalproject

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.TextViewCompat
import kotlinx.android.synthetic.main.lo_account.view.*
import com.example.bc_finalproject.ui.MainActivity
import kotlinx.android.synthetic.main.lo_account_edit.*
import kotlinx.android.synthetic.main.lo_account_edit.view.*
import org.w3c.dom.Text

class AccountAdapter(mCtx : Context, val accounts : ArrayList<Account>) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    val mCtx = mCtx

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtAccountName = itemView.txtAccountName
        val txtUsername = itemView.txtUsername
        val txtPassword = itemView.txtPassword
        val txtSite = itemView.txtSite
        val txtNotes = itemView.txtNotes
        val btnShow = itemView.btnShow
        val btnEdit = itemView.btnEdit
        val btnDelete = itemView.btnDelete
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AccountAdapter.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.lo_account,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onBindViewHolder(p0: AccountAdapter.ViewHolder, p1: Int) {
        val account : Account = accounts[p1]
        p0.txtAccountName.text = account.accountid
        p0.txtUsername.text = account.username
        p0.txtPassword.text = account.password
        p0.txtSite.text = account.website
        p0.txtNotes.text = account.notes

        p0.btnDelete.setOnClickListener{
            val accountID = account.accountid

            var alertDialog = AlertDialog.Builder(mCtx)
                .setTitle("Warning")
                .setMessage("Are you sure you want to delete: $accountID ?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (MainActivity.dbHandler.deleteAccount(account.accountid)) {
                        // removes the account from the list
                        accounts.removeAt(p1)
                            notifyItemRemoved(p1)
                            notifyItemRangeChanged(p1, accounts.size)
                        Toast.makeText(mCtx, "Account $accountID Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mCtx, "Error Deleting Account $accountID", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener{dialog, which -> })
                .setIcon(R.drawable.ic_warning_yellow_24dp)
                .show()
        }

        p0.btnEdit.setOnClickListener {
            val inflater = LayoutInflater.from(mCtx)
            val view = inflater.inflate(R.layout.lo_account_edit, null)

            val txtAccountID : TextView = view.findViewById(R.id.edit_text_accountidEdit)
            val txtUsername : TextView = view.findViewById(R.id.edit_text_usernameEdit)
            val txtPassword : TextView = view.findViewById(R.id.edit_text_passwordEdit)
            val txtSite : TextView = view.findViewById(R.id.edit_text_siteEdit)
            val txtNotes : TextView = view.findViewById(R.id.edit_text_notesEdit)

            txtAccountID.text = account.accountid
            txtUsername.text = account.username
            txtPassword.text = account.password
            txtSite.text = account.website
            txtNotes.text = account.notes

            val builder : AlertDialog.Builder = AlertDialog.Builder(mCtx)
                .setTitle("Update Account Info.")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener {dialog, which ->
                    val isUpdate : Boolean = MainActivity.dbHandler.editAccount(
                        account.accountid.toString(),
                        view.edit_text_accountidEdit.text.toString(),
                        view.edit_text_usernameEdit.text.toString(),
                        view.edit_text_passwordEdit.text.toString(),
                        view.edit_text_siteEdit.text.toString(),
                        view.edit_text_notesEdit.text.toString())
                    if(isUpdate==true) {
                        accounts[p1].accountid = view.edit_text_accountidEdit.text.toString()
                        accounts[p1].username = view.edit_text_usernameEdit.text.toString()
                        accounts[p1].password = view.edit_text_passwordEdit.text.toString()
                        accounts[p1].website = view.edit_text_siteEdit.text.toString()
                        accounts[p1].notes = view.edit_text_notesEdit.text.toString()
                        notifyDataSetChanged()
                        Toast.makeText(mCtx, "Edit Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mCtx, "Edit Error", Toast.LENGTH_SHORT).show()

                    }
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

                })
            val alert = builder.create()
            alert.show()
        }
    }

}