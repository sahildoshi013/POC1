package com.example.poc1.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    public LoginFragment() {
        // Required empty public constructor
    }


    private LoginCallback loginCallback;
    private TextView textView;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            String emailID = textView.getText().toString();
            performLogin(emailID);
        }
    }

    private void performLogin(final String emailID) {
        WebAPI.getClient().getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                User loginUser = null;
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    for (User user : users) {
                        if (user.getEmail().toLowerCase().equals(emailID.toLowerCase())) {
                            loginUser = user;
                        }
                    }
                }
                if (loginUser != null) {
                    loginCallback.onLoginSuccess(loginUser);
                } else {
                    loginCallback.onLoginFail(emailID);
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                loginCallback.onLoginFail(emailID);
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
        if(!(getActivity() instanceof LoginFragment.LoginCallback)){
            throw new IllegalStateException("Activity must have to implement LoginCallback");
        }
        loginCallback = (LoginFragment.LoginCallback) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach() called");
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
        View view = inflater.inflate(R.layout.fragment_login,container);

        textView = view.findViewById(R.id.tvEmailID);
        Button btnLogin = view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
