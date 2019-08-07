package com.example.poc1.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.poc1.R;
import com.example.poc1.api.WebAPI;
import com.example.poc1.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";
    private Button btnLogin;
    private ProgressBar progress_horizontal;
    private Call<List<User>> networkCall;

    public LoginFragment() {
        // Required empty public constructor
    }


    private LoginCallback loginCallback;
    private TextView textView;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            view.setClickable(false);
            progress_horizontal.setVisibility(View.VISIBLE);
            String emailID = textView.getText().toString();
            performLogin(emailID);
        }
    }

    private void performLogin(final String emailID) {
        networkCall = WebAPI.getClient().getUsers();
        networkCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                User loginUser = null;
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    if (users != null) {
                        for (User user : users) {
                            if (user.getEmail().toLowerCase().equals(emailID.toLowerCase())) {
                                loginUser = user;
                            }
                        }
                    }
                }
                if (loginCallback != null) {
                    if (loginUser != null) {
                        loginCallback.onLoginSuccess(loginUser);
                    } else {
                        loginCallback.onLoginFail(emailID);
                    }
                }
                btnLogin.setClickable(true);
                progress_horizontal.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                if (loginCallback != null) {
                    loginCallback.onLoginFail(emailID);
                }
                btnLogin.setClickable(true);
                progress_horizontal.setVisibility(View.INVISIBLE);
            }
        });
    }

    public interface LoginCallback{
        void onLoginSuccess(User user);
        void onLoginFail(String email);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() called with: context = [" + context + "]");
        if (!(context instanceof LoginFragment.LoginCallback)) {
            throw new IllegalStateException("Activity must have to implement LoginCallback");
        }
        loginCallback = (LoginFragment.LoginCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach() called");
        if (networkCall != null) {
            networkCall.cancel();
        }
        loginCallback = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView() called");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        textView = view.findViewById(R.id.tvEmailID);
        btnLogin = view.findViewById(R.id.btnLogin);
        progress_horizontal = view.findViewById(R.id.progress_horizontal);
        btnLogin.setOnClickListener(this);

        return view;
    }

}
