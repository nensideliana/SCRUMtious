package ca.mvp.scrumtious.scrumtious.presenter_impl;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ca.mvp.scrumtious.scrumtious.interfaces.presenter_int.LoginScreenPresenterInt;

public class LoginScreenPresenter implements LoginScreenPresenterInt{

    private FirebaseAuth firebaseAuth;
    private LoginScreenView loginScreenView;

    public LoginScreenPresenter(LoginScreenView loginScreenView) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.loginScreenView = loginScreenView;
    }

    @Override
    public void attemptLogin(Activity context, String emailAddress, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        // Attempt the sign in
        firebaseAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(context,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) loginScreenView.onSuccessfulLogin();
                        else loginScreenView.loginExceptionMessage("Invalid login credentials");
                    }
                });
    }

    @Override
    public void signOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }
    }
}
