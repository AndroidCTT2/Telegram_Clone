package com.mobile.messageclone.Chat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.R;
import com.mobile.messageclone.SignIn.CountryCodeDialogFragment;
import com.mobile.messageclone.SignIn.CountryToPhonePrefix;
import com.mobile.messageclone.SignIn.SignInViewModel;
import com.mobile.messageclone.SignIn.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;


public class fragment_find_contact extends Fragment {

    private TextInputEditText editChooseCountryCode;

    private TextInputEditText inputCountryCode;
    private TextInputEditText inputPhone;

    private SignInViewModel signInViewModel;

    private ImageView ProfileImage;

    private TextInputEditText inputFirstName;
    private TextInputEditText inputLastName;

    ArrayList<CountryToPhonePrefix> list=new ArrayList<>();


    private FirebaseDatabase firebaseDatabase;

    public fragment_find_contact() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        InitList();
        firebaseDatabase=FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        signInViewModel=new ViewModelProvider(getActivity()).get(SignInViewModel.class);


        inputFirstName=view.findViewById(R.id.inputNickFirstName);
        inputLastName=view.findViewById(R.id.inputNickLastName);

        ProfileImage=view.findViewById(R.id.ProfilePicture);

        editChooseCountryCode=view.findViewById(R.id.editChoseCountryCode);

        editChooseCountryCode.setClickable(true);

        editChooseCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountryCodeDialogFragment countryCodeDialogFragment=new CountryCodeDialogFragment();
                countryCodeDialogFragment.show(getParentFragmentManager(),null);


            }
        });

        TextDrawable Pic;
        Pic =TextDrawable.builder().beginConfig().fontSize(65).bold().toUpperCase().endConfig().buildRound("",getResources().getColor(R.color.colorPrimary,null));
        ProfileImage.setImageDrawable(Pic);

        inputCountryCode=view.findViewById(R.id.textInputCountryCode);
        inputPhone=view.findViewById(R.id.textInputPhone);

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
                            break;
                        }



                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        inputFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    TextDrawable Pic;
                    String FirstLetter;
                    String SecondLetter;
                    if (s.toString().isEmpty()==true)
                    {
                        FirstLetter="";
                    }
                    else
                    {
                        FirstLetter=String.valueOf(s.charAt(0));
                    }
                    if (inputLastName.getText().toString().isEmpty()==true)
                    {
                        SecondLetter="";
                    }
                    else
                    {
                        SecondLetter=String.valueOf(inputLastName.getText().toString().charAt(0));
                    }

                    Pic =TextDrawable.builder().beginConfig().fontSize(55).bold().toUpperCase().endConfig().buildRound(FirstLetter+SecondLetter,getResources().getColor(R.color.colorPrimary,null));
                    ProfileImage.setImageDrawable(Pic);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextDrawable Pic;
                String FirstLetter;
                String SecondLetter;
                if (inputFirstName.toString().isEmpty()==true)
                {
                    FirstLetter="";
                }
                else
                {
                    FirstLetter=String.valueOf(inputFirstName.getText().toString().charAt(0));
                }
                if (inputLastName.getText().toString().isEmpty()==true)
                {
                    SecondLetter="";
                }
                else
                {
                    SecondLetter=String.valueOf(inputLastName.getText().toString().charAt(0));
                }

                Pic =TextDrawable.builder().beginConfig().fontSize(55).bold().toUpperCase().endConfig().buildRound(FirstLetter+SecondLetter,getResources().getColor(R.color.colorPrimary,null));
                ProfileImage.setImageDrawable(Pic);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.add_contact_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.btnDone)
        {
            if (inputPhone.getText().toString().isEmpty()==false)
            {
                firebaseDatabase.getReference().child("USER").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user;
                        for (DataSnapshot childSnapshot: snapshot.getChildren())
                        {
                            User date=childSnapshot.getValue(User.class);
                           // date.ConvertToLocalDate();
                            //listDay.add(date);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }





    private void InitList()
    {
        Set<String> set = PhoneNumberUtil.createInstance(getContext()).getSupportedRegions();

        String[] arr = set.toArray(new String[set.size()]);

        for (int i = 0; i < arr.length; i++) {
            Locale locale = new Locale("en", arr[i]);
            String countryCode=String.valueOf(PhoneNumberUtil.createInstance(getContext()).getCountryCodeForRegion(arr[i]));
            list.add(new CountryToPhonePrefix(locale.getDisplayCountry(),countryCode));

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