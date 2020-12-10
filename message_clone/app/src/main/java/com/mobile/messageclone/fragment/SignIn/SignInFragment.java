package com.mobile.messageclone.fragment.SignIn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mobile.messageclone.R;
import com.mobile.messageclone.Model.CountryToPhonePrefix;
import com.mobile.messageclone.ViewModel.SignInViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton btnSignIn;
    private TextInputEditText editChooseCountryCode;

    private TextInputEditText inputCountryCode;
    private TextInputEditText inputPhone;

    private SignInViewModel signInViewModel;
    ArrayList<CountryToPhonePrefix>list=new ArrayList<>();

    private View viewRoot;

    private NavController navController;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignIn.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        InitList();
    }



    @Override
    public void onStart() {
        super.onStart();





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewRoot=inflater.inflate(R.layout.fragment_sign_in,container,false);




        return viewRoot;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        signInViewModel=new ViewModelProvider(getActivity()).get(SignInViewModel.class);

        navController=Navigation.findNavController(view);

        editChooseCountryCode=view.findViewById(R.id.editChoseCountryCode);

        editChooseCountryCode.setClickable(true);

        editChooseCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountryCodeDialogFragment countryCodeDialogFragment=new CountryCodeDialogFragment();
                countryCodeDialogFragment.show(getParentFragmentManager(),null);


            }
        });

        inputCountryCode=view.findViewById(R.id.textInputCountryCode);
        inputPhone=view.findViewById(R.id.textInputPhone);




        inputPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    btnSignIn.callOnClick();
                    return true;
                }
                return false;
            }
        });



        btnSignIn=viewRoot.findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PhoneNumberUtil phoneNumberUtil=PhoneNumberUtil.createInstance(getContext());
                String usephone="+"+inputCountryCode.getText().toString() + inputPhone.getText().toString();
                 Phonenumber.PhoneNumber phonenumber = new Phonenumber.PhoneNumber();
               // String RegionCode=phoneNumberUtil.getRegionCodeForCountryCode(Integer.valueOf(inputCountryCode.getText().toString()));
                String RegionCode=signInViewModel.ISOCNameMutableLiveData.getValue();
                try {
                   phoneNumberUtil.parse(usephone,RegionCode,phonenumber);
                    } catch (NumberParseException e) {
                    e.printStackTrace();
                    }
                if (inputPhone.getText().toString().length()==0 || inputPhone.getText().toString()==null )
                {

                }
                else if (inputCountryCode.getText().toString().isEmpty()==true)
                {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Warning")
                            .setMessage("Please choose country")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .show();
                }
                else if (editChooseCountryCode.getText().toString()=="Invalid country code")
                {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Warning")
                            .setMessage("Invalid country code. Please check country code and try again")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .show();
                }
                else if (phoneNumberUtil.isValidNumber(phonenumber)==false)
                {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Warning")
                                .setMessage("Invalid phone number. Please check the number and try again")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .show();


                }
                else
                {

                    signInViewModel.countryCodeMutableLiveData.setValue(inputCountryCode.getText().toString());
                    signInViewModel.phoneStringMutableLiveData.setValue(inputPhone.getText().toString());
                    signInViewModel.countryNameMutableLiveData.setValue(editChooseCountryCode.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putString("Phone", "+" + (inputCountryCode.getText().toString() + inputPhone.getText().toString()));
                    navController.navigate(R.id.action_signIn_to_validationPhone, bundle);
                }

            }
        });

        inputCountryCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length()==0)
                {
                    editChooseCountryCode.setText("Choose a country");
                }
                else {
                    editChooseCountryCode.setText("Invalid country code");
                    for (int i = 0; i < list.size(); i++) {
                        if (s.toString().trim().equals(list.get(i).Code.substring(1))) {
                            editChooseCountryCode.setText(list.get(i).Countryname);
                            signInViewModel.ISOCNameMutableLiveData.setValue(list.get(i).ISOCountry);
                            break;
                        }



                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signInViewModel.countryToPhonePrefixMutableLiveData.observe(getViewLifecycleOwner(), new Observer<CountryToPhonePrefix>() {
            @Override
            public void onChanged(CountryToPhonePrefix countryToPhonePrefix) {
                inputCountryCode.setText(countryToPhonePrefix.Code.substring(1));
                editChooseCountryCode.setText(countryToPhonePrefix.Countryname);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (signInViewModel.phoneStringMutableLiveData.getValue().isEmpty()==false)
        {
            inputPhone.setText(signInViewModel.phoneStringMutableLiveData.getValue());
            inputCountryCode.setText(signInViewModel.countryCodeMutableLiveData.getValue());
            signInViewModel.countryToPhonePrefixMutableLiveData.setValue(new CountryToPhonePrefix(signInViewModel.countryNameMutableLiveData.getValue(),inputCountryCode.getText().toString(),signInViewModel.ISOCNameMutableLiveData.getValue()));

        }
    }

    private void InitList()
    {
        Set<String> set = PhoneNumberUtil.createInstance(getContext()).getSupportedRegions();

        String[] arr = set.toArray(new String[set.size()]);

        for (int i = 0; i < arr.length; i++) {
            Locale locale = new Locale("en", arr[i]);
            String countryCode=String.valueOf(PhoneNumberUtil.createInstance(getContext()).getCountryCodeForRegion(arr[i]));
            CountryToPhonePrefix countryToPhonePrefix=new CountryToPhonePrefix(locale.getDisplayCountry(),countryCode,arr[i]);
            list.add(countryToPhonePrefix);

        }

        Comparator<CountryToPhonePrefix> countryToPhonePrefixComparator=new Comparator<CountryToPhonePrefix>() {
            @Override
            public int compare(CountryToPhonePrefix o1, CountryToPhonePrefix o2) {
                return o1.Countryname.compareToIgnoreCase(o2.Countryname);
            }
        };
        list.sort(countryToPhonePrefixComparator);
    }
}