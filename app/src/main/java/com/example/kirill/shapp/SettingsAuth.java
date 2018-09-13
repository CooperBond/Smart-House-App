package com.example.kirill.shapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsAuth extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private DatabaseReference my_ref;
    private TextView m_status_text_view;
    private TextView m_detail_text_view;
    private EditText m_email_field;
    private EditText m_password_field;
    public static String user_id;
    // [START declare_auth]
    private FirebaseAuth m_auth;
    private ArrayList current_user_data;
    // [END declare_auth]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_auth);

        // Views
        m_status_text_view = findViewById(R.id.status);
        m_detail_text_view = findViewById(R.id.detail);
        m_email_field = findViewById(R.id.field_email);
        m_password_field = findViewById(R.id.field_password);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.verify_email_button).setOnClickListener(this);
        findViewById(R.id.startButton).setOnClickListener(this);
        // [START initialize_auth]
        m_auth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        //START READING DATABASE

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = m_auth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        m_auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = m_auth.getCurrentUser();
                            updateUI(user);
                            createDataBase();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SettingsAuth.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void createDataBase() {
        my_ref = FirebaseDatabase.getInstance().getReference();
        Data data = new Data();
        HashMap<String, Object> map = new HashMap<>();
        map.put(user_id, data.getDataList());
        try {
            my_ref.updateChildren(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        m_auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SettingsAuth.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = m_auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SettingsAuth.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            m_status_text_view.setText(R.string.auth_failed);
//                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(SettingsAuth.this, "Pass failed.",
                            Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    Toast.makeText(SettingsAuth.this, "Email failed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    e.printStackTrace();
                    Toast.makeText(SettingsAuth.this, "Unkn failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // [END sign_in_with_email]
    }

    private void signOut() {
        m_auth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = m_auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsAuth.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SettingsAuth.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = m_email_field.getText().toString();
        if (TextUtils.isEmpty(email)) {
            m_email_field.setError("Required.");
            valid = false;
        } else {
            m_email_field.setError(null);
        }

        String password = m_password_field.getText().toString();
        if (TextUtils.isEmpty(password)) {
            m_password_field.setError("Required.");
            valid = false;
        } else {
            m_password_field.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            m_status_text_view.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            if (user.isEmailVerified()) {
                findViewById(R.id.startButton).setVisibility(View.VISIBLE);
            }
            m_detail_text_view.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
            user_id = getString(R.string.firebase_status_fmt, user.getUid());
            try {
                my_ref = FirebaseDatabase.getInstance().getReference();
            } catch (Exception e) {
                e.printStackTrace();
            }
            my_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    current_user_data = (ArrayList) dataSnapshot.child(SettingsAuth.user_id).getValue();

                    assert current_user_data != null;
                    Parameters.temperature_top = Integer.parseInt(String.valueOf(current_user_data.get(0)));
                    Parameters.temperature_mid = Integer.parseInt(String.valueOf(current_user_data.get(1)));
                    Parameters.temperature_bot = Integer.parseInt(String.valueOf(current_user_data.get(2)));
                    Parameters.temperature_4th = Integer.parseInt(String.valueOf(current_user_data.get(3)));
                    Parameters.temperature_5th = Integer.parseInt(String.valueOf(current_user_data.get(4)));

                    Parameters.status_fire_top = Integer.parseInt(String.valueOf(current_user_data.get(5)));
                    Parameters.status_fire_mid = Integer.parseInt(String.valueOf(current_user_data.get(6)));
                    Parameters.status_fire_bot = Integer.parseInt(String.valueOf(current_user_data.get(7)));
                    Parameters.status_fire_4th = Integer.parseInt(String.valueOf(current_user_data.get(8)));
                    Parameters.status_fire_5th = Integer.parseInt(String.valueOf(current_user_data.get(9)));
                    Parameters.status_snow_top = Integer.parseInt(String.valueOf(current_user_data.get(10)));
                    Parameters.status_snow_mid = Integer.parseInt(String.valueOf(current_user_data.get(11)));
                    Parameters.status_snow_bot = Integer.parseInt(String.valueOf(current_user_data.get(12)));
                    Parameters.status_snow_4th = Integer.parseInt(String.valueOf(current_user_data.get(13)));
                    Parameters.status_snow_5th = Integer.parseInt(String.valueOf(current_user_data.get(14)));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });
        } else {
            m_status_text_view.setText(R.string.signed_out);
            m_detail_text_view.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.startButton).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        hideKeyboard(v);
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(m_email_field.getText().toString(), m_password_field.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(m_email_field.getText().toString(), m_password_field.getText().toString());
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        } else if (i == R.id.startButton) {
            goNext();
        }
    }

    private void goNext() {
        Intent intent = new Intent(SettingsAuth.this, DashScreenActivity.class);
        startActivity(intent);
    }
}
