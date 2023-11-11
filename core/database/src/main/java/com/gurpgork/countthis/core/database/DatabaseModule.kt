package com.gurpgork.countthis.core.database

import android.content.Context
import android.os.Debug
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gurpgork.countthis.core.database.model.TRIGGER_UPDATE_COUNT_ON_DELETE_INCREMENT
import com.gurpgork.countthis.core.database.model.TRIGGER_UPDATE_COUNT_ON_INSERT_INCREMENT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesCtDatabase(
        @ApplicationContext context: Context
    ): CtDatabase {

        val builder = Room.databaseBuilder(
            context,
            CtDatabase::class.java,
            "ct-database"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                // Called when the database is created for the first time.
                // This is called after all the tables are created.
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                }
                // called every time the database is opened in the app
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)

                    db.execSQL(TRIGGER_UPDATE_COUNT_ON_INSERT_INCREMENT)
                    db.execSQL(TRIGGER_UPDATE_COUNT_ON_DELETE_INCREMENT)
                }
            })

        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }
}
