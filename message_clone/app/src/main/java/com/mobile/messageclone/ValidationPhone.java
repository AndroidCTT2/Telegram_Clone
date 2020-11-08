package com.mobile.messageclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.TaskExecutor;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ValidationPhone#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ValidationPhone extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextInputEditText editOTP;
    private MaterialButton btnValidatePhone;
    private ProgressBar progressBar;

    private String verifycodeBySystem;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ValidationPhone() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ValidationPhone.
     */
    // TODO: Rename and change types and number of parameters
    public static ValidationPhone newInstance(String param1, String param2) {
        ValidationPhone fragment = new ValidationPhone();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String Phone = "";
        if (getArguments() != null) {

            Phone=getArguments().getString("Phone");
            SentSms(Phone);

        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_validation_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnValidatePhone=view.findViewById(R.id.btnValidatePhone);
        editOTP=view.findViewById(R.id.editOTP);
        progressBar=view.findViewById(R.id.progress_bar);

        btnValidatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(editOTP.getEditableText().toString());
            }
        });

        progressBar.setVisibility(View.INVISIBLE);





    }

    private void SentSms(String Phone)
    {
        String phoneNumber=Phone;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder()
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verifycodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code= phoneAuthCredential.getSmsCode();
                if (code!=null)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    verifyCode(code);
                }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    private void verifyCode(String UserInputCode)
    {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verifycodeBySystem,UserInputCode);
        CompleteRegisterAndSignIn(credential);
    }

    private void CompleteRegisterAndSignIn(PhoneAuthCredential credential)
    {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()==true)
                {
                    NavController navController= Navigation.findNavController(getView());
                    navController.navigate(R.id.action_validationPhone_to_mainScreen);
                }
            }
        });
    }
}