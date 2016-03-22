package com.example.capucho.fileupload;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

/**
 * Created by Geen on 16/02/2016.
 */
public class BaseAsyncTask<Params, T> extends AsyncTask<Params, Void, T> {
    public Exception exception;
    protected final static String CHAVE_PREFERENCIA_COMPARTILHADA = "CHAVE_PREFERENCIA_COMPARTILHADA";
    protected SharedPreferences sharedPref;
    protected SharedPreferences.Editor editorSharedPref;
    protected Context context;

    @Override
    protected T doInBackground(Params... params) {
        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
    }
}
