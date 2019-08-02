package com.example.poc1.utilities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentFactory {

    public static void loadFragment(FragmentActivity activity, int containerId, Fragment fragment, boolean addToBackStack) {
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
    }

}
