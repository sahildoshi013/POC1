package com.example.poc1;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.poc1.fragments.LoginFragment;
import com.example.poc1.fragments.PostsListFragment;
import com.example.poc1.models.Post;
import com.example.poc1.models.User;
import com.example.poc1.utilities.FragmentFactory;
import com.example.poc1.utilities.SharedPreferencesUtilities;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginCallback, PostsListFragment.PostDisplayCallbacks, PostsListFragment.OnPostItemClickCallback, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "MainActivity";
    private Bundle postDetail;
    private boolean mIsDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        View articleView = findViewById(R.id.frmContainerDetailPost);

        mIsDualPane = articleView != null &&
                articleView.getVisibility() == View.VISIBLE;

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        checkLoginAndShowFragment();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        popBackAllFragments();
        checkLoginAndShowFragment();
        if (savedInstanceState.getBoolean("isDetailPost", false)) {
            Post post = new Post();
            post.setId(savedInstanceState.getInt("postID"));
            post.setTitle(savedInstanceState.getString("postTitle"));
            post.setBody(savedInstanceState.getString("postBody"));
            showPostDetails(post);
        }
    }

    private void popBackAllFragments() {
        Log.d(TAG, "popBackAllFragments() called " + getSupportFragmentManager().getBackStackEntryCount());
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Log.d(TAG, "popBackAllFragments() called " + getSupportFragmentManager().getBackStackEntryCount());
    }

    private void checkLoginAndShowFragment() {
        if (SharedPreferencesUtilities.getInstance(this).isUserLogin()) {
            showPostsListFragment();
        } else {
            showLoginFragment();
        }
    }

    private void showLoginFragment() {
        FragmentFactory.loadFragment(this, R.id.frmContainer, FragmentFactory.Screens.LOGIN, false, null);
    }

    @Override
    public void onLoginSuccess(User user) {
        Log.d(TAG, "onLoginSuccess() called with: user = [" + user + "]");
        SharedPreferencesUtilities.getInstance(this).saveUserData(user);
        showPostsListFragment();
    }

    private void showPostsListFragment() {
        User user = SharedPreferencesUtilities.getInstance(this).loggedInUser();
        Bundle bundle = new Bundle();
        bundle.putInt("userID", user.getId());
        bundle.putString("userName", user.getName());
        bundle.putBoolean("mIsDualPane", mIsDualPane);
        PostsListFragment postsListFragment = (PostsListFragment) FragmentFactory.loadFragment(this, R.id.frmContainer, FragmentFactory.Screens.DISPLAY_POST, false, bundle);
        postsListFragment.setOnItemClickListener(this);
    }

    @Override
    public void onLoginFail(String email) {
        Log.d(TAG, "onLoginFail() called with: email = [" + email + "]");
        Toast.makeText(this, "Login Fail!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisplayPost(List<Post> posts) {
        Log.d(TAG, "onDisplayPost() called with: posts = [" + posts + "]");
    }

    @Override
    public void onDisplayPostFail() {
        Log.d(TAG, "onDisplayPostFail() called");
        Toast.makeText(this, "Fail to display post", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Post post, int position) {
        Log.d(TAG, "onItemClick() called with: post = [" + post + "], position = [" + position + "]");
        showPostDetails(post);
    }

    private void showPostDetails(Post post) {
        Bundle bundle = new Bundle();
        bundle.putString("postTitle", post.getTitle());
        bundle.putString("postBody", post.getBody());
        bundle.putInt("postID", post.getId());
        int container = (mIsDualPane) ? R.id.frmContainerDetailPost : R.id.frmContainer;
        FragmentFactory.loadFragment(this, container, FragmentFactory.Screens.POST_DETAIL, !mIsDualPane, bundle);
        postDetail = bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");
        if (item.getItemId() == R.id.btnLogout) {
            SharedPreferencesUtilities.getInstance(this).logoutUser();
            int count = getSupportFragmentManager().getBackStackEntryCount();
            Log.d(TAG, "onOptionsItemSelected: " + count);
            getSupportFragmentManager().popBackStack();
            showLoginFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
                postDetail = null;
            } else {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDetailPost", false);
        if (postDetail != null) {
            outState.putBoolean("isDetailPost", true);
            outState.putAll(postDetail);
        }
    }
}
