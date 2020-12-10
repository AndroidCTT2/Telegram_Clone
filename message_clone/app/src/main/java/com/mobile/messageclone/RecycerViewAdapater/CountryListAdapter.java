package com.mobile.messageclone.RecycerViewAdapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.messageclone.R;
import com.mobile.messageclone.Model.CountryToPhonePrefix;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.viewHolder> {
    public ArrayList<CountryToPhonePrefix>list=new ArrayList<>();
    public ArrayList<CountryToPhonePrefix>originalList=new ArrayList<>();
    private Context context;

    private RecyclerViewClickInterface recyclerViewClickInterface;

    public CountryListAdapter(Context inputcontext,RecyclerViewClickInterface recyclerViewClickInterface)
    {
        context=inputcontext;
        this.recyclerViewClickInterface=recyclerViewClickInterface;



            Set<String> set = PhoneNumberUtil.createInstance(context).getSupportedRegions();

            String[] arr = set.toArray(new String[set.size()]);

            for (int i = 0; i < arr.length; i++) {
                Locale locale = new Locale("en", arr[i]);
                String countryCode=String.valueOf(PhoneNumberUtil.createInstance(context).getCountryCodeForRegion(arr[i]));
                CountryToPhonePrefix countryToPhonePrefix=new CountryToPhonePrefix(locale.getDisplayCountry(),countryCode,arr[i]);
                countryToPhonePrefix.ISOCountry=arr[i];
                list.add(countryToPhonePrefix);

            }

            Comparator<CountryToPhonePrefix> countryToPhonePrefixComparator=new Comparator<CountryToPhonePrefix>() {
                @Override
                public int compare(CountryToPhonePrefix o1, CountryToPhonePrefix o2) {
                    return o1.Countryname.compareToIgnoreCase(o2.Countryname);
                }
            };
            list.sort(countryToPhonePrefixComparator);


        originalList=list;
    }

    @NonNull
    @Override
    public CountryListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View root=layoutInflater.inflate(R.layout.row_country,parent,false);

        return new viewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryListAdapter.viewHolder holder, int position) {
            holder.countryName.setText(list.get(position).Countryname);
            holder.code.setText(list.get(position).Code);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder  {

        private TextView countryName;
        private TextView code;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            countryName=itemView.findViewById(R.id.textCountry);
            code=itemView.findViewById(R.id.textCode);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });

        }


    }

    public void filterList(ArrayList<CountryToPhonePrefix>filterList)
    {
        list=filterList;
        notifyDataSetChanged();

    }
}


