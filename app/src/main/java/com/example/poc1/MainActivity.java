package com.example.poc1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.poc1.fragments.LoginFragment;
import com.example.poc1.fragments.DisplayPostFragment;
import com.example.poc1.fragments.PostDetailsFragment;
import com.example.poc1.models.Post;
import com.example.poc1.models.User;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginCallback, DisplayPostFragment.PostDisplayCallbacks, DisplayPostFragment.OnPostItemClickCallback {

    private static final String TAG = "MainActivity";
    private FrameLayout frmContainer;
    private Fragment loginDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);


        frmContainer = findViewById(R.id.frmContainer);

        frmContainer.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {
                Log.d(TAG, "onChildViewAdded() called with: view = [" + view + "], view1 = [" + view1 + "]");
                Toast.makeText(MainActivity.this, "Childs : " + frmContainer.getChildCount(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildViewRemoved(View view, View view1) {
                Log.d(TAG, "onChildViewRemoved() called with: view = [" + view + "], view1 = [" + view1 + "]");
                Toast.makeText(MainActivity.this, "Childs : " + frmContainer.getChildCount(), Toast.LENGTH_SHORT).show();
            }
        });

        showLoginFragment();

        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginSuccess(null);
            }
        });
    }

    private void showLoginFragment() {
        addFragment();
    }

    private void addFragment() {
        loginDialogFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frmContainer,loginDialogFragment,"login");
        transaction.commit();
    }

    @Override
    public void onLoginSuccess(User user) {
        Log.d(TAG, "onLoginSuccess() called with: user = [" + user + "]");

        loginDialogFragment = getSupportFragmentManager().findFragmentByTag("login");
        if(loginDialogFragment!=null){
            getSupportFragmentManager().beginTransaction().remove(loginDialogFragment).commit();
        }

//
////        frmContainer.removeAllViews();
//        getSupportActionBar().show();
//        getSupportActionBar().setTitle(user.getName());
//
//        DisplayPostFragment displayPostFragment = new DisplayPostFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("user",user);
//        displayPostFragment.setArguments(bundle);
//        displayPostFragment.setOnItemClickListener(this);
//
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
////        FragmentTransaction trnc = fragmentManager.beginTransaction();
////        LoginFragment loginDialogFragment = (LoginFragment) fragmentManager.findFragmentByTag("login");
////        if (loginDialogFragment != null) {
////            trnc.remove(loginDialogFragment);
////        }
////        trnc.commit();
//
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.frmContainer,displayPostFragment,"DisplayPost");
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        transaction.addToBackStack(null);
//        transaction.commit();
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
        Toast.makeText(this, post.getTitle(), Toast.LENGTH_SHORT).show();
        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("post",post);

    }
}
