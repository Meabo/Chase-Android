package project.chasemvp.Model;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by Mehdi on 16/06/2017.
 */

public class MySharedPreferences
{
    private SharedPreferences mSharedPreferences;

    @Inject
    public MySharedPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void put(String key, String data) {
        mSharedPreferences.edit().putString(key, data).apply();
    }

    public void put(String key, int data) {
        mSharedPreferences.edit().putInt(key, data).apply();
    }


    public String get(String key) {
        return mSharedPreferences.getString(key, null);
    }


    public int get(String key, int a)
    {
        return mSharedPreferences.getInt(key, a);
    }



}
