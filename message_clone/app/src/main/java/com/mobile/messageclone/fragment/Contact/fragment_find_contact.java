package com.mobile.messageclone.fragment.Contact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.messageclone.Ulti.DrawProfilePicture;
import com.mobile.messageclone.R;
import com.mobile.messageclone.fragment.SignIn.CountryCodeDialogFragment;
import com.mobile.messageclone.Model.CountryToPhonePrefix;
import com.mobile.messageclone.ViewModel.SignInViewModel;
import com.mobile.messageclone.Model.User;
import com.mobile.messageclone.Model.Contact;
import com.mobile.messageclone.ViewModel.ChatViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;


public class fragment_find_contact extends Fragment {

    private TextInputEditText editChooseCountryCode;

    private TextInputEditText inputCountryCode;
    private TextInputEditText inputPhone;

    private SignInViewModel signInViewModel;

    private CircularImageView ProfileImage;

    private TextInputEditText inputFirstName;
    private TextInputEditText inputLastName;

    private TextView demo;

    private String UserFirstName;
    private String UserLastName;
    private ChatViewModel chatViewModel;



    ArrayList<CountryToPhonePrefix> list=new ArrayList<>();

    private FirebaseAuth firebaseAuth;
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
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseDatabase.getReference().child("USER").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=new User();
                for (DataSnapshot childData:snapshot.getChildren())
                {
                        user=childData.getValue(User.class);
                }
                UserFirstName=user.getFirstName();
                UserLastName=user.getLastName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("New contact");
        return inflater.inflate(R.layout.fragment_find_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        signInViewModel=new ViewModelProvider(getActivity()).get(SignInViewModel.class);


        inputFirstName=view.findViewById(R.id.inputNickFirstName);
        inputLastName=view.findViewById(R.id.inputNickLastName);

        ProfileImage=view.findViewById(R.id.ProfilePicture);
        ProfileImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        editChooseCountryCode=view.findViewById(R.id.editChoseCountryCode);

        editChooseCountryCode.setClickable(true);

        editChooseCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountryCodeDialogFragment countryCodeDialogFragment=new CountryCodeDialogFragment();
                countryCodeDialogFragment.show(getParentFragmentManager(),null);


            }
        });


//        ProfileImage.setImageBitmap(DrawProfilePicture.textAsBitmap("",60,Color.WHITE));

        inputCountryCode=view.findViewById(R.id.textInputCountryCode);
        inputPhone=view.findViewById(R.id.textInputPhone);

        demo=view.findViewById(R.id.demo);


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

                    if (FirstLetter.isEmpty()==false || SecondLetter.isEmpty()==false) {

                        ProfileImage.setImageBitmap(DrawProfilePicture.textAsBitmap(FirstLetter.toUpperCase() + SecondLetter.toUpperCase(), 70, Color.WHITE));
                    }
                    else
                    {
                        ProfileImage.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_360_24));
                    }
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
                if (FirstLetter.isEmpty()==false || SecondLetter.isEmpty()==false) {

                    ProfileImage.setImageBitmap(DrawProfilePicture.textAsBitmap(FirstLetter.toUpperCase() + SecondLetter.toUpperCase(), 70, Color.WHITE));
                }
                else
                {
                    ProfileImage.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_360_24));
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

        inputPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Complete();
                    return true;
                }
                return false;
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
           Complete();
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

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(250);
        shake.setInterpolator(new CycleInterpolator(4));
        return shake;
    }


    private void Complete()
    {
        if (inputFirstName.getText().toString().isEmpty()==true) {
            inputFirstName.startAnimation(shakeError());
        }



        if (inputPhone.getText().toString().isEmpty()==false && inputFirstName.getText().toString().isEmpty()==false) {


            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(getContext());
            String UserInputPhone = "+" + inputCountryCode.getText().toString() + inputPhone.getText().toString();
            Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber();
            try {
                phoneNumberUtil.parse(UserInputPhone, signInViewModel.ISOCNameMutableLiveData.getValue(), phoneNumber);
            } catch (NumberParseException e) {
                e.printStackTrace();
            }
            String phoneParse = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
            Log.d("Phone",phoneParse);

            if (phoneParse.equals(firebaseAuth.getCurrentUser().getPhoneNumber())) {

            } else {
                firebaseDatabase.getReference().child("USER").orderByChild("phoneNum").equalTo(phoneParse).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() == true) {

                            User user = new User();
                            String userId = "";
                            for (DataSnapshot childData : snapshot.getChildren()) {
                                userId = childData.getKey();
                                user = childData.getValue(User.class);
                            }
                            //demo.setText(text);
                            Contact contact = new Contact();
                            contact.setUserIdContact(userId);
                            contact.setFirstNickName(inputFirstName.getText().toString().trim());
                            if (inputLastName.getText().toString().isEmpty() == false) {
                                contact.setLastNickName(inputLastName.getText().toString().trim());
                            } else {
                                contact.setLastNickName("");
                            }
                            contact.setContactStatus(Contact.IN_CONTACT);
                            firebaseDatabase.getReference().child("CONTACT").child(firebaseAuth.getCurrentUser().getUid()).child(contact.getUserIdContact()).setValue(contact);

                            contact = new Contact();
                            contact.setUserIdContact(firebaseAuth.getCurrentUser().getUid());
                            contact.setLastNickName(UserLastName);
                            contact.setFirstNickName(UserFirstName);
                            contact.setContactStatus(Contact.NOT_IN_CONTACT);
                            firebaseDatabase.getReference().child("CONTACT").child(userId).child(contact.getUserIdContact()).setValue(contact);
                            chatViewModel.IsDeleteListContactSeenTimeList.setValue(false);
                            getActivity().onBackPressed();

                        } else {
                            new AlertDialog.Builder(getContext()).setTitle("Contact not found").setMessage(inputFirstName.getText().toString() + " is not on our app yet. We hope you can invite him/her to using our app")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }
}