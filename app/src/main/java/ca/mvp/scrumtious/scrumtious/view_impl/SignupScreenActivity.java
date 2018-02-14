package ca.mvp.scrumtious.scrumtious.view_impl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ca.mvp.scrumtious.scrumtious.R;
import ca.mvp.scrumtious.scrumtious.interfaces.view_int.SignupScreenViewInt;
import ca.mvp.scrumtious.scrumtious.presenter_impl.SignupScreenPresenter;
import ca.mvp.scrumtious.scrumtious.utils.UserInputValidator;

public class SignupScreenActivity extends AppCompatActivity implements SignupScreenViewInt {

    private EditText emailField, passwordField, retypePasswordField;
    private TextInputLayout emailFieldLayout, passwordFieldLayout, retypePasswordFieldLayout;

    private ProgressDialog signingInProgressDialog;
    private SignupScreenPresenter signUpScreenPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Sign Up");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        signUpScreenPresenter = new SignupScreenPresenter(this);
        setupFormWatcher();
    }

    private void setupFormWatcher() {
        emailField = (EditText) findViewById(R.id.signupScreenEmailField);
        passwordField = (EditText) findViewById(R.id.signupScreenPasswordField);
        retypePasswordField = (EditText) findViewById(R.id.signupScreenRetypePasswordField);
        emailFieldLayout = (TextInputLayout) findViewById(R.id.signupScreenEmailFieldLayout);
        passwordFieldLayout = (TextInputLayout) findViewById(R.id.signupScreenPasswordFieldLayout);
        retypePasswordFieldLayout = (TextInputLayout)
                findViewById(R.id.signupScreenRetypePasswordFieldLayout);

        emailFieldLayout.setError(null);
        passwordFieldLayout.setError(null);
        retypePasswordFieldLayout.setError(null);
        emailFieldLayout.setErrorEnabled(true);
        passwordFieldLayout.setErrorEnabled(true);
        retypePasswordFieldLayout.setErrorEnabled(true);

        //create a watcher for emailField
        //create a listener for email field and validate it
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int isValidEmail = UserInputValidator.isValidEmail(emailField.getText().toString().trim());

                if (isValidEmail < 0) {
                    emailFieldLayout.setErrorEnabled(true);
                    if (isValidEmail == -1)
                        emailFieldLayout.setError("Please enter an e-mail address.");
                    else if (isValidEmail == -2)
                        emailFieldLayout.setError("Please enter a valid length e-mail address.");
                    else if (isValidEmail == -3)
                        emailFieldLayout.setError("Must provide a valid e-mail address.");
                } else {
                    emailFieldLayout.setError(null);
                    emailFieldLayout.setErrorEnabled(false);
                }
            }
        });
        //create a watcher for password
        //create a listener for password
        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Cannot have an empty password
                int isValidPassword = UserInputValidator.meetsPasswordCriteria(passwordField.getText().toString().trim());
                //-1 on null password, -2 on invalid password length, -3 for invalid password (including* length), 0 for valid password
                if(isValidPassword == -1){
                    passwordFieldLayout.setErrorEnabled(true);
                    passwordFieldLayout.setError("Please enter a password.");

                }
                else if(isValidPassword == -2){
                    passwordFieldLayout.setErrorEnabled(true);
                    passwordFieldLayout.setError("Password has to be at least 8 characters.");
                }
                else if(isValidPassword == -3){
                    passwordFieldLayout.setErrorEnabled(true);
                    passwordFieldLayout.setError("Password must contain atleast 1 letter and 1 digit.");
                }
                else{
                    passwordFieldLayout.setError(null);
                    passwordFieldLayout.setErrorEnabled(false);
                }
            }
        });
        //create a watcher for retypePassword
        //create a listener for retypePassword
        retypePasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!passwordField.getText().toString().trim().equals
                        (retypePasswordField.getText().toString().trim())){
                    retypePasswordFieldLayout.setErrorEnabled(true);
                    retypePasswordFieldLayout.setError("This does not match the password above.");
                }
                else{
                    retypePasswordFieldLayout.setError(null);
                    retypePasswordFieldLayout.setErrorEnabled(false);
                }
            }
        });

    }

    public void onClickSignUpSubmit(View view){

        String emailAddress = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        // If either error message is displaying, that means the form can't be submitted properly
        if(passwordFieldLayout.isErrorEnabled() || emailFieldLayout.isErrorEnabled() || retypePasswordFieldLayout.isErrorEnabled()) {
            Toast.makeText(this, "Cannot submit until the fields are filled out properly.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creates a dialog that appears to tell the user that the sign up is occurring
        signingInProgressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        signingInProgressDialog.setTitle("Sign up");
        signingInProgressDialog.setCancelable(false);
        signingInProgressDialog.setMessage("Attempting to sign you up...");
        signingInProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        signingInProgressDialog.show();

        // Proceed to sign up user with backend authentication
        signUpScreenPresenter.attemptSignUp(emailAddress, password);
    }

    @Override
    public void signUpExceptionMessage(String error) {
        signingInProgressDialog.dismiss();
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessfulSignUp() {
        Toast.makeText(this,"Verification e-mail sent!",Toast.LENGTH_SHORT).show();
        signingInProgressDialog.dismiss();
        Intent intent = new Intent(SignupScreenActivity.this, LoginScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){

        // Make sure user doesn't accidentally leave the screen with text filled in
        if(emailField.getText().toString().trim().length() > 0 ||
                passwordField.getText().toString().trim().length() > 0 ||
                retypePasswordField.getText().toString().trim().length() > 0){

            new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.LoginAlertDialog))
                    .setTitle("Leave the screen?")
                    .setMessage("Are you sure you want to go back? You will lose anything " +
                            "you have typed in on this page.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SignupScreenActivity.this, LoginScreenActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else{
            super.onBackPressed();
        }

    }

}
