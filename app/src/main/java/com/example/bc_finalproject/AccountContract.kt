package com.example.bc_finalproject

import android.provider.BaseColumns


object AccountContract {

    /* Inner class that defines the table contents */
    class AccountEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "Accounts"
            val COLUMN_ACCOUNTID = "accountid"
            val COLUMN_USERNAME = "username"
            val COLUMN_PASSWORD = "password"
            val COLUMN_WEBSITE = "website"
            val COLUMN_NOTES = "notes"
        }
    }
}