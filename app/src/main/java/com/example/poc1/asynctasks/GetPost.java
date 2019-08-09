package com.example.poc1.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.poc1.database.DatabaseProvider;
import com.example.poc1.models.Post;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GetPost extends AsyncTask<Integer, Integer, List<Post>> {

    private static final String TAG = "GetPostsByUserID";
    private final WeakReference<Context> weakReference;
    private LoadPostCallback callback;

    public GetPost(Context context) {
        weakReference = new WeakReference<>(context);
    }

    public void setPostLoadCallback(LoadPostCallback loadCallback) {
        this.callback = loadCallback;
    }

    @Override
    protected List<Post> doInBackground(Integer... integers) {
        List<Post> posts = new ArrayList<>();

        Context context = weakReference.get();

        if (context != null) {
            for (int i = 0; i < integers.length; i++) {
                Post loadedPost = DatabaseProvider.getDatabaseInstance(context).postDao().getPost(integers[i]);
                posts.add(loadedPost);
                publishProgress(integers.length / (i + 1));
            }
        }

        return posts;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, "onProgressUpdate() called with: values = [" + values + "]");
    }

    @Override
    protected void onPostExecute(List<Post> posts) {
        super.onPostExecute(posts);
        Log.d(TAG, "onPostExecute() called with: posts = [" + posts + "]");
        if (callback != null) {
            if (posts.size() > 0) {
                callback.onPostLoadSuccessful(posts);
            } else {
                callback.onPostLoadFail();
            }
        }
    }

    public interface LoadPostCallback {
        void onPostLoadSuccessful(List<Post> posts);
        void onPostLoadFail();
    }
}

