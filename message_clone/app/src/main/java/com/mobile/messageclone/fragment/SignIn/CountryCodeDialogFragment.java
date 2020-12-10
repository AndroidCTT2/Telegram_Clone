package com.mobile.messageclone.fragment.SignIn;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mobile.messageclone.R;
import com.mobile.messageclone.RecycerViewAdapater.CountryListAdapter;
import com.mobile.messageclone.Model.CountryToPhonePrefix;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;
import com.mobile.messageclone.ViewModel.SignInViewModel;

import java.util.ArrayList;

public class CountryCodeDialogFragment extends DialogFragment implements RecyclerViewClickInterface {

    private RecyclerView countrycodeList;
    private TextInputEditText searchBar;
    private TextView NoResult;

    private CountryListAdapter countryListAdapter;
    private ArrayList<CountryToPhonePrefix> CoutryList=new ArrayList<>();

    private SignInViewModel signInViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_country_code,container,false);

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NoResult=view.findViewById(R.id.CountryNoResult);
        NoResult.setVisibility(View.INVISIBLE);
        searchBar=view.findViewById(R.id.SeachCountry);
        countrycodeList=view.findViewById(R.id.listCountryCode);
        countryListAdapter=new CountryListAdapter(getContext(),this);
        countrycodeList.setAdapter(countryListAdapter);
        countrycodeList.setLayoutManager(new LinearLayoutManager(getContext()));
        CoutryList=countryListAdapter.originalList;


        signInViewModel=new ViewModelProvider(getActivity()).get(SignInViewModel.class);



        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s==null || s.toString().length()==0)
                {
                    countryListAdapter.filterList(countryListAdapter.originalList);
                }
                else {

                    ArrayList<CountryToPhonePrefix> filterList = new ArrayList<>();
                    for (CountryToPhonePrefix item : countryListAdapter.originalList) {
                        if (item.Countryname.toLowerCase().charAt(0)==s.toString().toLowerCase().trim().charAt(0)) {
                            if (item.Countryname.toLowerCase().contains(s.toString().trim().toLowerCase())) {


                                filterList.add(item);
                            }
                        }
                    }

                    if (filterList.size()==0)
                    {
                        NoResult.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        NoResult.setVisibility(View.INVISIBLE);
                    }
                    countryListAdapter.filterList(filterList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        signInViewModel.countryToPhonePrefixMutableLiveData.setValue(countryListAdapter.list.get(position));
        Toast.makeText(getContext(),countryListAdapter.list.get(position).ISOCountry,Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void onLongItemClick(int position) {

    }
}
