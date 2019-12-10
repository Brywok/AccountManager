package com.example.bc_finalproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bc_finalproject.R
import java.util.*

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.R.menu
import android.app.SearchManager
import androidx.core.content.ContextCompat
import android.R.id
import android.content.Intent
import android.widget.EditText
import androidx.core.view.MenuItemCompat
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import android.widget.LinearLayout

import net.sqlcipher.database.SQLiteConstraintException
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteException
import net.sqlcipher.database.SQLiteOpenHelper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bc_finalproject.Account
import com.example.bc_finalproject.AccountAdapter
import com.example.bc_finalproject.DBHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private fun getPassword() = intent.extras?.get("password").toString()

    companion object {
        lateinit var dbHandler: DBHandler
    }

    var accountslist = ArrayList<Account>()
    lateinit var adapter :RecyclerView.Adapter<*>
    lateinit var rv : RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        net.sqlcipher.database.SQLiteDatabase.loadLibs(this)
        //net.sqlcipher.database.SQLiteDatabase.openDatabase(this, "password")
        dbHandler = DBHandler(this, null, null, 1)


        viewAccounts()

        fab.setOnClickListener {
            val i = Intent(this, AddAccountActivity::class.java)
            startActivity(i)
        }

    }

    private fun viewAccounts(){
        /*
        val accountslist : ArrayList<Account> = dbHandler.getAccounts(this)
        val adapter = AccountAdapter(this, accountslist)
        val rv : RecyclerView = findViewById(R.id.rv) */

        accountslist = dbHandler.getAccounts(this)
        adapter = AccountAdapter(this, accountslist)
        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    override fun onResume() {
        viewAccounts()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)

        return true
    }

    // Click actions on settings_menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_search -> {
            msgShow("Search")
            true
        }

        R.id.action_settings -> {
            msgShow("Settings")
            true
        }

        R.id.action_help -> {
            msgShow("Help")
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
