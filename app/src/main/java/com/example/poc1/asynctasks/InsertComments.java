package com.example.poc1.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.poc1.database.DatabaseProvider;
import com.example.poc1.models.Comment;

import java.lang.ref.WeakReference;
import java.util.List;

public class InsertComments extends AsyncTask<List<Comment>, Integer, Boolean> {

    private static final String TAG = "InsertComments";
    private final WeakReference<Context> weakReference;

    public InsertComments(Context context) {
        weakReference = new WeakReference<>(context);
    }

    @Override
    protected Boolean doInBackground(List<Comment>... comments) {
        Context context = weakReference.get();
        long[] result = null;
        if (context != null) {
            for (List<Comment> comment : comments) {
                result = DatabaseProvider.getDatabaseInstance(context).commentDao().insertAll(comment);
            }
        }
        return result != null;
    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute() called with: result = [" + result + "]");
        Context context = weakReference.get();
        if (result && context != null) {
            Toast.makeText(context, "Comments Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Fail to save comments", Toast.LENGTH_SHORT).show();
        }
    }
}

