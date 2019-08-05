package com.example.poc1.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.poc1.database.DatabaseProvider;
import com.example.poc1.models.Comment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GetComments extends AsyncTask<Integer, Integer, List<Comment>> {

    private static final String TAG = "GetComments";
    private final WeakReference<Context> weakReference;
    private LoadCommentCallback callback;

    public GetComments(Context context) {
        weakReference = new WeakReference<>(context);
    }

    public void setCommentLoadCallback(LoadCommentCallback loadCallback) {
        this.callback = loadCallback;
    }

    @Override
    protected List<Comment> doInBackground(Integer... integers) {
        List<Comment> posts = new ArrayList<>();

        Context context = weakReference.get();

        if (context != null) {
            for (int i = 0; i < integers.length; i++) {
                List<Comment> loadedPost = DatabaseProvider.getDatabaseInstance(context).commentDao().loadAllByPostId(integers[i]);
                posts.addAll(loadedPost);
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
    protected void onPostExecute(List<Comment> comments) {
        super.onPostExecute(comments);
        Log.d(TAG, "onPostExecute() called with: posts = [" + comments + "]");
        if (callback != null) {
            if (comments.size() > 0) {
                callback.onPostLoadSuccessful(comments);
            } else {
                callback.onPostLoadFail();
            }
        }
    }

    public interface LoadCommentCallback {
        void onPostLoadSuccessful(List<Comment> comments);

        void onPostLoadFail();
    }
}

