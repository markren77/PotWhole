package io.github.incplusplus.potwhole;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass. Use the {@link MyAccountFragment#newInstance} factory method
 * to create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment {

    private FirebaseAuth mAuth;

    private EditText editTextEmail, editTextPassword;

    private TextView createAccount;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (mAuth.getCurrentUser() == null) {
            return inflater.inflate(R.layout.fragment_my_account_not_signed_in, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_my_account_signed_in, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAuth.getCurrentUser() == null) {

            Button logInButton = getView().findViewById(R.id.log_in_button);

            createAccount = (TextView) getView().findViewById(R.id.createAccount);

            editTextEmail = getView().findViewById(R.id.editTextEmail);
            editTextPassword = getView().findViewById(R.id.editTextPassword);

            logInButton.setOnClickListener(v -> userLogin());

            createAccount.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), RegisterAccountPage.class);
                startActivity(intent);
            });

        } else {
            Button signOutButton = getView().findViewById(R.id.sign_out_button);

            signOutButton.setOnClickListener(v -> {

                mAuth.signOut();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            });

        }

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Please Enter an Email Address");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please Enter a Valid Email Address");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextEmail.setError("Please Enter a Password");
            editTextEmail.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //redirect
                System.out.println("User is Signed in");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
