package com.example.poc1;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.poc1.fragments.DisplayPostFragment;
import com.example.poc1.fragments.LoginFragment;
import com.example.poc1.fragments.PostDetailsFragment;
import com.example.poc1.models.Post;
import com.example.poc1.models.User;
import com.example.poc1.utilities.FragmentFactory;
import com.example.poc1.utilities.SharedPreferencesUtilities;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginCallback, DisplayPostFragment.PostDisplayCallbacks, DisplayPostFragment.OnPostItemClickCallback, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "MainActivity";
    private FrameLayout frmContainer;
    private Fragment loginDialogFragment;
    private DisplayPostFragment displayPostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        frmContainer = findViewById(R.id.frmContainer);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        checkLoginAndShowFragment();
    }

    private void checkLoginAndShowFragment() {
        if (SharedPreferencesUtilities.getInstance(this).isUserLogin()) {
            User user = SharedPreferencesUtilities.getInstance(this).loggedInUser();
            Toast.makeText(this, "Welcome back, " + user.getName(), Toast.LENGTH_SHORT).show();
            showDisplayPostFragment(user);
        } else {
            showLoginFragment();
        }
    }

    private void showLoginFragment() {
        loginDialogFragment = new LoginFragment();
        FragmentFactory.loadFragment(this, R.id.frmContainer, loginDialogFragment, false);
    }

    @Override
    public void onLoginSuccess(User user) {
        Log.d(TAG, "onLoginSuccess() called with: user = [" + user + "]");
        SharedPreferencesUtilities.getInstance(this).saveUserData(user);
        showDisplayPostFragment(user);
    }

    private void showDisplayPostFragment(User user) {
        displayPostFragment = new DisplayPostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userID", user.getId());
        bundle.putString("userName", user.getName());
        displayPostFragment.setArguments(bundle);
        displayPostFragment.setOnItemClickListener(this);

        FragmentFactory.loadFragment(this, R.id.frmContainer, displayPostFragment, false);
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
        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("postTitle", post.getTitle());
        bundle.putString("postBody", post.getBody());
        bundle.putInt("postID", post.getId());
        postDetailsFragment.setArguments(bundle);
        FragmentFactory.loadFragment(this, R.id.frmContainer, postDetailsFragment, true);
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
            } else {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
            }
        }
    }
}
