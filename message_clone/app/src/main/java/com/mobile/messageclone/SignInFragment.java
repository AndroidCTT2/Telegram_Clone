package com.mobile.messageclone;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

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
    }

    @Override
    public void onStart() {
        super.onStart();
        InitList();
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



        btnSignIn=viewRoot.findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputPhone.getText().toString().length()==0 || inputPhone.getText().toString()==null  )
                {
                    Toast.makeText(getContext(),"Chưa nhập số điện thoại",Toast.LENGTH_SHORT).show();
                }
                else if (editChooseCountryCode.getText().toString()=="Invalid country code")
                {
                    Toast.makeText(getContext(),"Chưa nhập đúng mã điện thoại",Toast.LENGTH_SHORT).show();
                }
                else {

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

    private void InitList()
    {
        list.add(new CountryToPhonePrefix("Afghanistan", "93"));
        list.add(new CountryToPhonePrefix("Albania", "355"));
        list.add(new CountryToPhonePrefix("Algeria", "213"));
        list.add(new CountryToPhonePrefix("American Samoa", "1684"));
        list.add(new CountryToPhonePrefix("Andorra ", "376"));
        list.add(new CountryToPhonePrefix("Angola ", "244"));
        list.add(new CountryToPhonePrefix("Anguilla ", "1264"));
        list.add(new CountryToPhonePrefix("Antarctica", "672"));
        list.add(new CountryToPhonePrefix("Antigua", "1268"));
        list.add(new CountryToPhonePrefix("Argentina", "54"));
        list.add(new CountryToPhonePrefix("Armenia ", "374"));
        list.add(new CountryToPhonePrefix("Aruba", "297"));
        list.add(new CountryToPhonePrefix("Ascension", "247"));
        list.add(new CountryToPhonePrefix("Australia ", "61"));
        list.add(new CountryToPhonePrefix("Australian External Territories", "672"));
        list.add(new CountryToPhonePrefix("Austria", "43"));
        list.add(new CountryToPhonePrefix("Azerbaijan", "994"));
        list.add(new CountryToPhonePrefix("Bahamas", "1242"));
        list.add(new CountryToPhonePrefix("Bahrain", "973"));
        list.add(new CountryToPhonePrefix("Bangladesh", "880"));
        list.add(new CountryToPhonePrefix("Barbados", "1246"));
        list.add(new CountryToPhonePrefix("Barbuda", "1268"));
        list.add(new CountryToPhonePrefix("Belarus", "375"));
        list.add(new CountryToPhonePrefix("Belgium", "32"));
        list.add(new CountryToPhonePrefix("Belize", "501"));
        list.add(new CountryToPhonePrefix("Benin", "229"));
        list.add(new CountryToPhonePrefix("Bermuda", "1441"));
        list.add(new CountryToPhonePrefix("Bhutan", "975"));
        list.add(new CountryToPhonePrefix("Bolivia ", "591"));
        list.add(new CountryToPhonePrefix("Bosnia & Herzegovina ", "387"));
        list.add(new CountryToPhonePrefix("Botswana ", "267"));
        list.add(new CountryToPhonePrefix("Brazil ", "55"));
        list.add(new CountryToPhonePrefix("British Virgin Islands", "1284"));
        list.add(new CountryToPhonePrefix("Brunei Darussalam", "673"));
        list.add(new CountryToPhonePrefix("Bulgaria", "359"));
        list.add(new CountryToPhonePrefix("Burkina Faso ", "226"));
        list.add(new CountryToPhonePrefix("Burundi", "257"));
        list.add(new CountryToPhonePrefix("Cambodia", "855"));
        list.add(new CountryToPhonePrefix("Cameroon", "237"));
        list.add(new CountryToPhonePrefix("Canada", "1"));
        list.add(new CountryToPhonePrefix("Cape Verde Islands", "238"));
        list.add(new CountryToPhonePrefix("Cayman Islands", "1345"));
        list.add(new CountryToPhonePrefix("Central African Republic", "236"));
        list.add(new CountryToPhonePrefix("Chad ", "235"));
        list.add(new CountryToPhonePrefix("Chatham Island (New Zealand)", "64"));
        list.add(new CountryToPhonePrefix("Chile ", "56"));
        list.add(new CountryToPhonePrefix("China", "86"));
        list.add(new CountryToPhonePrefix("Christmas Island", "61"));
        list.add(new CountryToPhonePrefix("Cocos-Keeling Islands", "61"));
        list.add(new CountryToPhonePrefix("Colombia ", "57"));
        list.add(new CountryToPhonePrefix("Comoros", "269"));
        list.add(new CountryToPhonePrefix("Congo", "242"));
        list.add(new CountryToPhonePrefix("Congo, Dem. Rep. of  (former Zaire) ", "243"));
        list.add(new CountryToPhonePrefix("Cook Islands", "682"));
        list.add(new CountryToPhonePrefix("Costa Rica", "506"));
        list.add(new CountryToPhonePrefix("Côte d'Ivoire (Ivory Coast)", "225"));
        list.add(new CountryToPhonePrefix("Croatia", "385"));
        list.add(new CountryToPhonePrefix("Cuba", "53"));
        list.add(new CountryToPhonePrefix("Cuba (Guantanamo Bay)", "5399"));
        list.add(new CountryToPhonePrefix("Curaçao", "5999"));
        list.add(new CountryToPhonePrefix("Cyprus", "357"));
        list.add(new CountryToPhonePrefix("Czech Republic", "420"));
        list.add(new CountryToPhonePrefix("Denmark", "45"));
        list.add(new CountryToPhonePrefix("Diego Garcia", "246"));
        list.add(new CountryToPhonePrefix("Djibouti", "253"));
        list.add(new CountryToPhonePrefix("Dominica", "1767"));
        list.add(new CountryToPhonePrefix("Dominican Republic ", "1809"));
        list.add(new CountryToPhonePrefix("East Timor", "670"));
        list.add(new CountryToPhonePrefix("Easter Island", "56"));
        list.add(new CountryToPhonePrefix("Ecuador ", "593 "));
        list.add(new CountryToPhonePrefix("Egypt", "20"));
        list.add(new CountryToPhonePrefix("El Salvador", "503"));
        list.add(new CountryToPhonePrefix("Equatorial Guinea", "240"));
        list.add(new CountryToPhonePrefix("Eritrea", "291"));
        list.add(new CountryToPhonePrefix("Estonia", "372"));
        list.add(new CountryToPhonePrefix("Ethiopia", "251"));
        list.add(new CountryToPhonePrefix("Falkland Islands (Malvinas)", "500"));
        list.add(new CountryToPhonePrefix("Faroe Islands", "298"));
        list.add(new CountryToPhonePrefix("Fiji Islands", "679"));
        list.add(new CountryToPhonePrefix("Finland", "358"));
        list.add(new CountryToPhonePrefix("France", "33"));
        list.add(new CountryToPhonePrefix("French Antilles", "596"));
        list.add(new CountryToPhonePrefix("French Guiana", "594"));
        list.add(new CountryToPhonePrefix("French Polynesia", "689"));
        list.add(new CountryToPhonePrefix("Gabonese Republic", "241"));
        list.add(new CountryToPhonePrefix("Gambia", "220"));
        list.add(new CountryToPhonePrefix("Georgia", "995"));
        list.add(new CountryToPhonePrefix("Germany", "49"));
        list.add(new CountryToPhonePrefix("Ghana ", "233"));
        list.add(new CountryToPhonePrefix("Gibraltar ", "350"));
        list.add(new CountryToPhonePrefix("Greece ", "30"));
        list.add(new CountryToPhonePrefix("Greenland ", "299"));
        list.add(new CountryToPhonePrefix("Grenada", "1473"));
        list.add(new CountryToPhonePrefix("Guadeloupe", "590"));
        list.add(new CountryToPhonePrefix("Guam", "1671"));
        list.add(new CountryToPhonePrefix("Guantanamo Bay", "5399"));
        list.add(new CountryToPhonePrefix("Guatemala ", "502"));
        list.add(new CountryToPhonePrefix("Guinea-Bissau ", "245"));
        list.add(new CountryToPhonePrefix("Guinea", "224"));
        list.add(new CountryToPhonePrefix("Guyana", "592"));
        list.add(new CountryToPhonePrefix("Haiti ", "509"));
        list.add(new CountryToPhonePrefix("Honduras", "504"));
        list.add(new CountryToPhonePrefix("Hong Kong", "852"));
        list.add(new CountryToPhonePrefix("Hungary ", "36"));
        list.add(new CountryToPhonePrefix("Iceland", "354"));
        list.add(new CountryToPhonePrefix("India", "91"));
        list.add(new CountryToPhonePrefix("Indonesia", "62"));
        list.add(new CountryToPhonePrefix("Iran", "98"));
        list.add(new CountryToPhonePrefix("Iraq", "964"));
        list.add(new CountryToPhonePrefix("Ireland", "353"));
        list.add(new CountryToPhonePrefix("Israel ", "972"));
        list.add(new CountryToPhonePrefix("Italy ", "39"));
        list.add(new CountryToPhonePrefix("Jamaica ", "1876"));
        list.add(new CountryToPhonePrefix("Japan ", "81"));
        list.add(new CountryToPhonePrefix("Jordan", "962"));
        list.add(new CountryToPhonePrefix("Kazakhstan", "7"));
        list.add(new CountryToPhonePrefix("Kenya", "254"));
        list.add(new CountryToPhonePrefix("Kiribati ", "686"));
        list.add(new CountryToPhonePrefix("Korea (North)", "850"));
        list.add(new CountryToPhonePrefix("Korea (South)", "82"));
        list.add(new CountryToPhonePrefix("Kuwait ", "965"));
        list.add(new CountryToPhonePrefix("Kyrgyz Republic", "996"));
        list.add(new CountryToPhonePrefix("Laos", "856"));
        list.add(new CountryToPhonePrefix("Latvia ", "371"));
        list.add(new CountryToPhonePrefix("Lebanon", "961"));
        list.add(new CountryToPhonePrefix("Lesotho", "266"));
        list.add(new CountryToPhonePrefix("Liberia", "231"));
        list.add(new CountryToPhonePrefix("Libya", "218"));
        list.add(new CountryToPhonePrefix("Liechtenstein", "423"));
        list.add(new CountryToPhonePrefix("Lithuania ", "370"));
        list.add(new CountryToPhonePrefix("Luxembourg", "352"));
        list.add(new CountryToPhonePrefix("Macao", "853"));
        list.add(new CountryToPhonePrefix("Macedonia (Former Yugoslav Rep of.)", "389"));
        list.add(new CountryToPhonePrefix("Madagascar", "261"));
        list.add(new CountryToPhonePrefix("Malawi ", "265"));
        list.add(new CountryToPhonePrefix("Malaysia", "60"));
        list.add(new CountryToPhonePrefix("Maldives", "960"));
        list.add(new CountryToPhonePrefix("Mali Republic", "223"));
        list.add(new CountryToPhonePrefix("Malta", "356"));
        list.add(new CountryToPhonePrefix("Marshall Islands", "692"));
        list.add(new CountryToPhonePrefix("Martinique", "596"));
        list.add(new CountryToPhonePrefix("Mauritania", "222"));
        list.add(new CountryToPhonePrefix("Mauritius", "230"));
        list.add(new CountryToPhonePrefix("Mayotte Island", "269"));
        list.add(new CountryToPhonePrefix("Mexico", "52"));
        list.add(new CountryToPhonePrefix("Micronesia, (Federal States of)", "691"));
        list.add(new CountryToPhonePrefix("Midway Island", "1808"));
        list.add(new CountryToPhonePrefix("Moldova ", "373"));
        list.add(new CountryToPhonePrefix("Monaco", "377"));
        list.add(new CountryToPhonePrefix("Mongolia ", "976"));
        list.add(new CountryToPhonePrefix("Montenegro", "382"));
        list.add(new CountryToPhonePrefix("Montserrat ", "1664"));
        list.add(new CountryToPhonePrefix("Morocco", "212"));
        list.add(new CountryToPhonePrefix("Mozambique", "258"));
        list.add(new CountryToPhonePrefix("Myanmar", "95"));
        list.add(new CountryToPhonePrefix("Namibia", "264"));
        list.add(new CountryToPhonePrefix("Nauru", "674"));
        list.add(new CountryToPhonePrefix("Nepal ", "977"));
        list.add(new CountryToPhonePrefix("Netherlands", "31"));
        list.add(new CountryToPhonePrefix("Netherlands Antilles", "599"));
        list.add(new CountryToPhonePrefix("Nevis", "1869"));
        list.add(new CountryToPhonePrefix("New Caledonia", "687"));
        list.add(new CountryToPhonePrefix("New Zealand", "64"));
        list.add(new CountryToPhonePrefix("Nicaragua", "505"));
        list.add(new CountryToPhonePrefix("Niger", "227"));
        list.add(new CountryToPhonePrefix("Nigeria", "234"));
        list.add(new CountryToPhonePrefix("Niue", "683"));
        list.add(new CountryToPhonePrefix("Norfolk Island", "672"));
        list.add(new CountryToPhonePrefix("Northern Marianas Islands (Saipan, Rota, & Tinian)", "1670"));
        list.add(new CountryToPhonePrefix("Norway","47"));
        list.add(new CountryToPhonePrefix("Oman", "968"));
        list.add(new CountryToPhonePrefix("Pakistan", "92"));
        list.add(new CountryToPhonePrefix("Palau", "680"));
        list.add(new CountryToPhonePrefix("Palestinian Settlements", "970"));
        list.add(new CountryToPhonePrefix("Panama", "507"));
        list.add(new CountryToPhonePrefix("Papua New Guinea", "675"));
        list.add(new CountryToPhonePrefix("Paraguay", "595"));
        list.add(new CountryToPhonePrefix("Peru", "51"));
        list.add(new CountryToPhonePrefix("Philippines", "63"));
        list.add(new CountryToPhonePrefix("Poland", "48"));
        list.add(new CountryToPhonePrefix("Portugal", "351"));
        list.add(new CountryToPhonePrefix("Puerto Rico", "1787"));
        list.add(new CountryToPhonePrefix("Qatar", "974"));
        list.add(new CountryToPhonePrefix("Réunion Island", "262"));
        list.add(new CountryToPhonePrefix("Romania", "40"));
        list.add(new CountryToPhonePrefix("Russia", "7"));
        list.add(new CountryToPhonePrefix("Rwandese Republic", "250"));
        list.add(new CountryToPhonePrefix("St. Helena", "290"));
        list.add(new CountryToPhonePrefix("St. Kitts/Nevis", "1869"));
        list.add(new CountryToPhonePrefix("St. Lucia", "1758"));
        list.add(new CountryToPhonePrefix("St. Pierre & Miquelon", "508"));
        list.add(new CountryToPhonePrefix("St. Vincent & Grenadines", "1784"));
        list.add(new CountryToPhonePrefix("Samoa", "685"));
        list.add(new CountryToPhonePrefix("San Marino", "378"));
        list.add(new CountryToPhonePrefix("São Tomé and Principe", "239"));
        list.add(new CountryToPhonePrefix("Saudi Arabia", "966"));
        list.add(new CountryToPhonePrefix("Senegal ", "221"));
        list.add(new CountryToPhonePrefix("Serbia", "381"));
        list.add(new CountryToPhonePrefix("Seychelles Republic", "248"));
        list.add(new CountryToPhonePrefix("Sierra Leone", "232"));
        list.add(new CountryToPhonePrefix("Singapore", "65"));
        list.add(new CountryToPhonePrefix("Slovak Republic", "421"));
        list.add(new CountryToPhonePrefix("Slovenia ", "386"));
        list.add(new CountryToPhonePrefix("Solomon Islands", "677"));
        list.add(new CountryToPhonePrefix("Somali Democratic Republic", "252"));
        list.add(new CountryToPhonePrefix("South Africa", "27"));
        list.add(new CountryToPhonePrefix("Spain", "34"));
        list.add(new CountryToPhonePrefix("Sri Lanka", "94"));
        list.add(new CountryToPhonePrefix("Sudan", "249"));
        list.add(new CountryToPhonePrefix("Suriname ", "597"));
        list.add(new CountryToPhonePrefix("Swaziland", "268"));
        list.add(new CountryToPhonePrefix("Sweden", "46"));
        list.add(new CountryToPhonePrefix("Switzerland", "41"));
        list.add(new CountryToPhonePrefix("Syria", "963"));
        list.add(new CountryToPhonePrefix("Taiwan", "886"));
        list.add(new CountryToPhonePrefix("Tajikistan", "992"));
        list.add(new CountryToPhonePrefix("Tanzania", "255"));
        list.add(new CountryToPhonePrefix("Thailand", "66"));
        list.add(new CountryToPhonePrefix("Timor Leste", "670"));
        list.add(new CountryToPhonePrefix("Togolese Republic", "228"));
        list.add(new CountryToPhonePrefix("Tokelau ", "690"));
        list.add(new CountryToPhonePrefix("Tonga Islands", "676"));
        list.add(new CountryToPhonePrefix("Trinidad & Tobago", "1868"));
        list.add(new CountryToPhonePrefix("Tunisia", "216"));
        list.add(new CountryToPhonePrefix("Turkey", "90 "));
        list.add(new CountryToPhonePrefix("Turkmenistan ", "993"));
        list.add(new CountryToPhonePrefix("Turks and Caicos Islands", "1649"));
        list.add(new CountryToPhonePrefix("Tuvalu", "688"));
        list.add(new CountryToPhonePrefix("Uganda", "256"));
        list.add(new CountryToPhonePrefix("Ukraine", "380"));
        list.add(new CountryToPhonePrefix("United Arab Emirates", "971"));
        list.add(new CountryToPhonePrefix("United Kingdom", "44"));
        list.add(new CountryToPhonePrefix("United States of America", "1"));
        list.add(new CountryToPhonePrefix("US Virgin Islands", "1340"));
        list.add(new CountryToPhonePrefix("Uruguay", "598"));
        list.add(new CountryToPhonePrefix("Uzbekistan", "998"));
        list.add(new CountryToPhonePrefix("Vanuatu", "678"));
        list.add(new CountryToPhonePrefix("Venezuela", "58"));
        list.add(new CountryToPhonePrefix("Vietnam", "84"));
        list.add(new CountryToPhonePrefix("Wake Island", "808"));
        list.add(new CountryToPhonePrefix("Wallis and Futuna Islands", "681"));
        list.add(new CountryToPhonePrefix("Yemen", "967"));
        list.add(new CountryToPhonePrefix("Zambia ", "260"));
        list.add(new CountryToPhonePrefix("Zanzibar", "255"));
        list.add(new CountryToPhonePrefix("Zimbabwe ", "263"));
    }
}