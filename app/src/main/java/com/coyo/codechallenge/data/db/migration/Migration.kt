package com.coyo.codechallenge.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("alter table posts add column favorite INTEGER default 0")
    }
}


// Add other migrations to this list
val ALL_MIGRATION = arrayOf(MIGRATION_1_2)

