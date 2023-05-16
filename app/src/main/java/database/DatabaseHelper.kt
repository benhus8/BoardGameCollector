package database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "boardgames.db"
        private const val TABLE_NAME = "boardgames"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_BOARDGAME = "CREATE TABLE IF NOT EXISTS boardgame " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "original_title TEXT, " +
                "year_published INTEGER, " +
                "bgg_id INTEGER) "
        db?.execSQL(CREATE_TABLE_BOARDGAME)

        val CREATE_TABLE_EXPANSION = "CREATE TABLE IF NOT EXISTS expansion " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "original_title TEXT, " +
                "year_published INTEGER, " +
                "bgg_id INTEGER) "
        db?.execSQL(CREATE_TABLE_EXPANSION)

        val CREATE_TABLE_IMAGE = "CREATE TABLE IF NOT EXISTS image " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "game_id INTEGER, " +
                "expansion_id INTEGER, " +
                "thumbnail TEXT," +
                "image_path TEXT, " +
                "FOREIGN KEY (game_id) REFERENCES boardgame(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (expansion_id) REFERENCES expansion(id) ON DELETE CASCADE) "
        db?.execSQL(CREATE_TABLE_IMAGE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}