package com.gurpgork.countthis.core.data.repository
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.booleanPreferencesKey
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import kotlinx.coroutines.flow.first
//import javax.inject.Inject
//
//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
//
//class DataStoreRepository @Inject constructor(
//    private val context: Context
//){
//    suspend fun putString(key: String, value: String){
//        val preferenceKey = stringPreferencesKey(key)
//        context.dataStore.edit {
//            it[preferenceKey] = value
//        }
//    }
//    suspend fun putBoolean(key: String, value: Boolean){
//        val preferenceKey = booleanPreferencesKey(key)
//        context.dataStore.edit {
//            it[preferenceKey] = value
//        }
//    }
//
//    suspend fun getString(key: String):String?{
//        return try {
//            val preferenceKey = stringPreferencesKey(key)
//            val preference = context.dataStore.data.first()
//            preference[preferenceKey]
//        }catch (e:Exception){
//            e.printStackTrace()
//            null
//        }
//    }
//
//    suspend fun getBoolean(key: String):Boolean?{
//        return try {
//            val preferenceKey = booleanPreferencesKey(key)
//            val preference = context.dataStore.data.first()
//            preference[preferenceKey]
//        }catch (e:Exception){
//            e.printStackTrace()
//            null
//        }
//    }
//
//    suspend fun clearStringPreference(key: String){
//        val preferenceKey = stringPreferencesKey(key)
//        context.dataStore.edit {
//            if(it.contains(preferenceKey))
//                it.remove(preferenceKey)
//        }
//    }
//    suspend fun clearBooleanPreference(key: String){
//        val preferenceKey = booleanPreferencesKey(key)
//        context.dataStore.edit {
//            if(it.contains(preferenceKey))
//                it.remove(preferenceKey)
//        }
//    }
//
//}