package com.example.poc1.utilities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.poc1.fragments.DisplayPostFragment;
import com.example.poc1.fragments.LoginFragment;
import com.example.poc1.fragments.PostDetailsFragment;

public class FragmentFactory {

    private static Fragment loginFragment;
    private static Fragment postDetailFragment;
    private static Fragment postListFragment;

    public static Fragment loadFragment(FragmentActivity activity, int containerId, Screens applicationState, boolean addToBackStack, Bundle bundle) {
        Fragment fragment = getFragmentFromState(applicationState, bundle);
        try {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(containerId, fragment, fragment.getClass().getName());
            if (addToBackStack)
                transaction.addToBackStack(fragment.getClass().getName());
            transaction.commit();
        } catch (Exception e) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(containerId, fragment, fragment.getClass().getName());
            if (addToBackStack)
                transaction.addToBackStack(fragment.getClass().getName());
            transaction.commitAllowingStateLoss();
        }
        return fragment;
    }

    private static Fragment getFragmentFromState(Screens applicationState, Bundle bundle) {
        switch (applicationState) {
            case LOGIN:
                if (loginFragment == null) {
                    loginFragment = new LoginFragment();
                }
                loginFragment.setArguments(bundle);
                return loginFragment;
            case DISPLAY_POST:
                if (postListFragment == null) {
                    postListFragment = new DisplayPostFragment();
                }
                postListFragment.setArguments(bundle);
                return postListFragment;
            case POST_DETAIL:
                if (postDetailFragment == null) {
                    postDetailFragment = new PostDetailsFragment();
                }
                postDetailFragment.setArguments(bundle);
                return postDetailFragment;
            default:
                if (loginFragment == null) {
                    loginFragment = new LoginFragment();
                }
                loginFragment.setArguments(bundle);
                return loginFragment;
        }
    }

    public enum Screens {
        LOGIN, DISPLAY_POST, POST_DETAIL
    }
}
