package com.example.poc1.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.poc1.database.DatabaseProvider;
import com.example.poc1.models.Post;

import java.lang.ref.WeakReference;
import java.util.List;

public class InsertPosts extends AsyncTask<List<Post>, Integer, Boolean> {

    private static final String TAG = "InsertPosts";
    private final WeakReference<Context> weakReference;

    public InsertPosts(Context context) {
        weakReference = new WeakReference<>(context);
    }

    @SafeVarargs
    @Override
    protected final Boolean doInBackground(List<Post>... lists) {
        Context context = weakReference.get();
        long[] result = null;
        if (context != null) {
            for (List<Post> post : lists) {
                result = DatabaseProvider.getDatabaseInstance(context).postDao().insertAll(post);
            }
        }
        return result != null;
    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute() called with: result = [" + result + "]");
    }
}

