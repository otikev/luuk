package com.luuk.common.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by Sudarshan on 02-04-2018
 * Modified by Isaac 9-2-2021.
 */
/**
 * Singleton which manages persistent object storage & retrieval using SharedPreferences.
 */
class SharedPrefManager private constructor() {
    var sharedPreferences: SharedPreferences? = null
    var gson: Gson? = null

    /**
     * Saves any object to SharedPreferences replacing its previous occurrence.
     * Immediate action on same thread.Recommended when we need to retrieve object immediately after we save it.
     * Saves object as a json string internally. Not recommended to put large objects into shared preferences,
     * as it may slow down the application if stored lot of large objects containing arraylists or lot of data as its members.
     * For large objects use object caching solutions like object cache libs.
     *
     * @param key    unique key string
     * @param object object to store
     */
    fun saveObject(key: String?, `object`: Any?) {
        if (`object` == null) removeObject(key) else sharedPreferences!!.edit()
            .putString(key, gson!!.toJson(`object`)).commit()
    }

    /**
     * Saves any object to SharedPreferences replacing its previous occurrence.
     * Not immediate action.Asynchronous.Better option if the object not needed be retrieved immediately after saving.
     * Saves object as a json string internally. Not recommended to put large objects into shared preferences,
     * as it may slow down the application if stored lot of large objects containing arraylists or lot of data as its members.
     * For large objects use object caching solutions like object cache libs.
     *
     * @param key    unique key string
     * @param object object to store
     */
    fun saveObjectAsynchronously(key: String?, `object`: Any?) {
        if (`object` == null) removeObject(key) else sharedPreferences!!.edit()
            .putString(key, gson!!.toJson(`object`)).apply()
    }

    /**
     * @param key  unique key string
     * @param type type of object, how to get : Type objectType = new TypeToken<YourObject>(){}.getType();
     * @return
    </YourObject> */
    fun getObject(key: String?, type: Type?): Any? {
        return gson!!.fromJson(sharedPreferences!!.getString(key, null), type)
    }

    fun removeObject(key: String?) {
        sharedPreferences!!.edit().remove(key).commit()
    }

    fun saveString(key: String?, value: String?) {
        sharedPreferences!!.edit().putString(key, value).commit()
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences!!.getString(key, defaultValue)
    }

    fun saveInt(key: String?, value: Int) {
        sharedPreferences!!.edit().putInt(key, value).commit()
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return sharedPreferences!!.getInt(key, defaultValue)
    }

    fun saveBoolean(key: String?, value: Boolean) {
        sharedPreferences!!.edit().putBoolean(key, value).commit()
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defaultValue)
    }

    fun saveLong(key: String?, value: Long) {
        sharedPreferences!!.edit().putLong(key, value).commit()
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return sharedPreferences!!.getLong(key, defaultValue)
    }

    companion object {
        private val sharedPrefManager = SharedPrefManager()
        fun init(context: Context?) {
            sharedPrefManager.sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            sharedPrefManager.gson = GsonBuilder().registerTypeAdapter(Float::class.java, object : TypeAdapter<Float?>() {
                    @Throws(IOException::class)
                    override fun read(reader: JsonReader): Float? {
                        if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull()
                            return null
                        }
                        val stringValue = reader.nextString()
                        return try {
                            stringValue.toFloat()
                        } catch (e: NumberFormatException) {
                            null
                        }
                    }

                    @Throws(IOException::class)
                    override fun write(writer: JsonWriter, value: Float?) {
                        if (value == null) {
                            writer.nullValue()
                            return
                        }
                        writer.value(value)
                    }
                }).create()
        }

        val instance: SharedPrefManager
            get() {
                if (sharedPrefManager.sharedPreferences == null) throw RuntimeException(
                    "SharedPrefManager members not instantiated, make sure you call" +
                            "init() through your Application subclass"
                )
                return sharedPrefManager
            }
    }
}