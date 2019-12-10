package com.example.bc_finalproject

import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import android.widget.Toast
import com.example.bc_finalproject.AccountContract.AccountEntry.Companion.TABLE_NAME

import net.sqlcipher.database.SQLiteConstraintException
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteException
import net.sqlcipher.database.SQLiteOpenHelper

import java.util.ArrayList



class DBHandler(context: Context, name : String?, factory: SQLiteDatabase.CursorFactory?, version : Int) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    private val userPass = "password"


    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "MyData.db"

        val TABLE_NAME = "Accounts"
        val COLUMN_ACCOUNTID = "accountid"
        val COLUMN_USERNAME = "username"
        val COLUMN_PASSWORD = "password"
        val COLUMN_WEBSITE = "website"
        val COLUMN_NOTES = "notes"
    }

    private val CREATE_ACCOUNT_TABLE : String = ("CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ACCOUNTID ACCOUNT ID NAME," +
            "$COLUMN_USERNAME TEXT," +
            "$COLUMN_PASSWORD TEXT," +
            "$COLUMN_WEBSITE TEXT," +
            "$COLUMN_NOTES TEXT)")

    private val DELETE_ACCOUNT_TABLE : String = "DROP TABLE IF EXISTS " + AccountContract.AccountEntry.TABLE_NAME


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_ACCOUNT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DELETE_ACCOUNT_TABLE)
        onCreate(db)
    }

    fun getAccounts(mCtx :Context) : ArrayList<Account>{
        val qry = "Select * From $TABLE_NAME"
        val db = this.getReadableDatabase(userPass)
        val cursor = db.rawQuery(qry, null)
        val accounts = ArrayList<Account>()

        if (cursor.count == 0)
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show()
        else
        {
            cursor.moveToFirst()
            while(!cursor.isAfterLast()){
                val account = Account()
                account.accountid = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNTID))
                account.username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
                account.password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
                account.website = cursor.getString(cursor.getColumnIndex(COLUMN_WEBSITE))
                account.notes = cursor.getString(cursor.getColumnIndex(COLUMN_NOTES))
                accounts.add(account)
                cursor.moveToNext()
            }
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()

        return accounts
    }

    fun addAccount(mCtx: Context, account: Account) {
        val values = ContentValues()
        values.put(COLUMN_ACCOUNTID, account.accountid)
        values.put(COLUMN_USERNAME, account.username)
        values.put(COLUMN_PASSWORD, account.password)
        values.put(COLUMN_WEBSITE, account.website)
        values.put(COLUMN_NOTES, account.notes)
        val db = this.getWritableDatabase(userPass)

        try {
            db.insert(TABLE_NAME, null, values)
            // or
            //db.rawQuery("Insert Into $TABLE_NAME ($COLUMN_ACCOUNTID, $COLUMN_USERNAME, $COLUMN_PASSWORD, $COLUMN_WEBSITE, $COLUMN_NOTES) Values(?,?)")
            Toast.makeText(mCtx, "Account Added", Toast.LENGTH_SHORT).show()
        } catch (e : Exception) {
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteAccount(accountID : String) : Boolean {
        // Method 1
        // val qry = "Delete From $TABLE_NAME where $COLUMN_ACCOUNTID = $accountID"
        val db = this.getWritableDatabase(userPass)
        var result : Boolean = false
        try {
            // Method 2 - Comment out method 1 if using method 2
            val cursor = db.delete(TABLE_NAME, "$COLUMN_ACCOUNTID = ?", arrayOf(accountID.toString()))

            // Method 1
            // val cursor = db.execSQL(qry)
            result = true
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        db.close()
        return result
    }

    fun editAccount(id : String, accountID : String, username : String, password : String, website : String, notes : String) : Boolean {
        val db = this.getWritableDatabase(userPass)
        val contentValues = ContentValues()
        var result : Boolean = false
        contentValues.put(COLUMN_ACCOUNTID, accountID)
        contentValues.put(COLUMN_USERNAME, username)
        contentValues.put(COLUMN_PASSWORD, password)
        contentValues.put(COLUMN_WEBSITE, website)
        contentValues.put(COLUMN_NOTES, notes)
        try {
            db.update(TABLE_NAME, contentValues, "$COLUMN_ACCOUNTID = ?", arrayOf(id))
            result = true
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Error Editing")
            result = false
        }
        return true
    }



    /*
    @Throws(SQLiteConstraintException::class)
    fun insertAccount(account: AccountModel): Boolean {
        // Gets the data repository in write mode
        val db = getWritableDatabase(userPass)

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(AccountContract.AccountEntry.COLUMN_ACCOUNT, account.accountid)
        values.put(AccountContract.AccountEntry.COLUMN_USERNAME, account.username)
        values.put(AccountContract.AccountEntry.COLUMN_PASSWORD, account.password)
        values.put(AccountContract.AccountEntry.COLUMN_WEBSITE, account.website)
        values.put(AccountContract.AccountEntry.COLUMN_NOTES, account.notes)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(AccountContract.AccountEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteAccount(accountid: String): Boolean {
        // Gets the data repository in write mode
        val db = getWritableDatabase(userPass)

        // Define 'where' part of query.
        val selection = AccountContract.AccountEntry.COLUMN_ACCOUNT + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(accountid)
        // Issue SQL statement.
        db.delete(AccountContract.AccountEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readAccount(accountid: String): ArrayList<AccountModel> {
        val account = ArrayList<AccountModel>()
        val db = getWritableDatabase(userPass)

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + AccountContract.AccountEntry.TABLE_NAME + " WHERE " + AccountContract.AccountEntry.COLUMN_ACCOUNT + "='" + accountid + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var accountid: String
        var username: String
        var password: String
        var website: String
        var notes: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                accountid = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_ACCOUNTID))
                username = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_USERNAME))
                password = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_PASSWORD))
                website = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_WEBSITE))
                notes = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_NOTES))

                account.add(AccountModel(accountid, username, password, website, notes))
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()

        return account
    }

    fun readAllAccounts(): ArrayList<AccountModel> {
        val accounts = ArrayList<AccountModel>()
        // CHANGE
        val db = getWritableDatabase(userPass)

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + AccountContract.AccountEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var accountid: String
        var username: String
        var password: String
        var website: String
        var notes: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                accountid = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_ACCOUNTID))
                username = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_USERNAME))
                password = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_PASSWORD))
                website = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_WEBSITE))
                notes = cursor.getString(cursor.getColumnIndex(AccountContract.AccountEntry.COLUMN_NOTES))

                accounts.add(AccountModel(accountid, username, password, website, notes))
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()

        return accounts
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "MyData.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AccountContract.AccountEntry.TABLE_NAME + " (" +
                    AccountContract.AccountEntry.COLUMN_ACCOUNTID + " TEXT PRIMARY KEY," +
                    AccountContract.AccountEntry.COLUMN_USERNAME + " TEXT," +
                    AccountContract.AccountEntry.COLUMN_PASSWORD + " TEXT," +
                    AccountContract.AccountEntry.COLUMN_WEBSITE + " TEXT," +
                    AccountContract.AccountEntry.COLUMN_NOTES + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + AccountContract.AccountEntry.TABLE_NAME
    }*/
}