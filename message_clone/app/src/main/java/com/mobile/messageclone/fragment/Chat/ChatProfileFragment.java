package com.mobile.messageclone.fragment.Chat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.messageclone.Model.Contact;
import com.mobile.messageclone.Model.User;
import com.mobile.messageclone.R;
import com.mobile.messageclone.RecycerViewAdapater.ContactListHomeAdapter;
import com.mobile.messageclone.Ulti.ActivityUlti;
import com.mobile.messageclone.Ulti.DrawProfilePicture;
import com.mobile.messageclone.ViewModel.ChatViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ChatProfileFragment extends Fragment {

    private TextView displayName;
    private TextView displayBio;
    private TextView displayPhone;


    private TextInputEditText editFirstName;
    private TextInputEditText editLastName;

    private Boolean EditModeOn;

    private ImageView profilePicture;

    private String ContactID;
    private int ChatType;
    private String UserID;
    private String ContactName;
    private LinearLayout bioLinearLayout;

    private LinearLayout EditMode;
    private LinearLayout ViewMode;


    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    private FirebaseDatabase firebaseDatabase;

    private ChatViewModel chatViewModel;

    private ValueEventListener contactListener;

    private ExtendedFloatingActionButton btnEditContact;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null)
        {
            ContactID=getArguments().getString("ContactID");
            ChatType=getArguments().getInt("ChatType");
            UserID=getArguments().getString("UserID");
            ContactName=getArguments().getString("ContactName");
        }

        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);

        chatViewModel.IsHideAppBar.setValue(true);
        chatViewModel.IsHideNavBar.setValue(true);

        firebaseDatabase=FirebaseDatabase.getInstance();

        EditModeOn=false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root=null;

        if (ChatType==ContactListHomeAdapter.CHAT_PERSONAL) {

            root = inflater.inflate(R.layout.chat_profile_fragment, container, false);
        }


        toolbar=root.findViewById(R.id.toolbar);
        appBarLayout=root.findViewById(R.id.appBarLayout);

        bioLinearLayout=root.findViewById(R.id.BioLayout);

        EditMode=root.findViewById(R.id.EditMode);
        ViewMode=root.findViewById(R.id.ViewMode);

        EditMode.setVisibility(View.GONE);

        btnEditContact=root.findViewById(R.id.editContactExtended);



        editFirstName=root.findViewById(R.id.editFirstName);
        editLastName=root.findViewById(R.id.editLastName);




        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (isAdded()) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        }

        collapsingToolbarLayout=view.findViewById(R.id.collapsing);

        profilePicture=view.findViewById(R.id.ProfilePicture);


        displayBio=view.findViewById(R.id.displayBio);
        displayPhone=view.findViewById(R.id.displayPhoneNumber);


        firebaseDatabase.getReference().child("USER").child(ContactID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists() == true)
                {
                    User user = snapshot.getValue(User.class);
                    displayPhone.setText(user.getPhoneNum());
                    displayBio.setText(user.getBio());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());

                Log.d("Percent",String.valueOf(percentage));
                profilePicture.setScaleX(1-percentage);
                profilePicture.setScaleY(1-percentage);


            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    btnEditContact.shrink();


                }
                else
                {
                    //Expanded
                    btnEditContact.extend();

                }
            }
        });

        btnEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (EditModeOn==false) {
                    EditModeOn = true;
                    EditMode.setVisibility(View.VISIBLE);
                    ViewMode.setVisibility(View.GONE);
                    if (EditModeOn == true) {
                        collapsingToolbarLayout.setTitle("Edit info");

                    }
                    btnEditContact.setText("Done");
                    btnEditContact.setIcon(getActivity().getDrawable(R.drawable.ic_baseline_check_24));
                }
                else
                {

                    if (editFirstName.getText().toString().isEmpty()==true)
                    {
                        Toast.makeText(getContext(),"First name required",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("firstNickName",editFirstName.getText().toString().trim());
                        ContactName=editFirstName.getText().toString().trim();
                        if (editLastName.getText().toString().isEmpty()==true)
                        {
                            hashMap.put("lastNickName","");
                            ContactName=ContactName+"";
                        }
                        else
                        {
                            hashMap.put("lastNickName",editLastName.getText().toString().trim());
                            ContactName=ContactName+" "+editLastName.getText().toString().trim();
                        }

                        firebaseDatabase.getReference().child("CONTACT").child(UserID).child(ContactID).updateChildren(hashMap);


                        collapsingToolbarLayout.setTitle(ContactName);



                        EditModeOn = false;
                        btnEditContact.setText("Edit");
                        btnEditContact.setIcon(getActivity().getDrawable(R.drawable.ic_baseline_edit_24));
                        ViewMode.setVisibility(View.VISIBLE);
                        EditMode.setVisibility(View.GONE);
                    }

                }

            }
        });

        contactListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()==true)
                {
                    Contact contact=snapshot.getValue(Contact.class);
                    collapsingToolbarLayout.setTitle(contact.getFirstNickName()+" "+contact.getLastNickName());


                    editFirstName.setText(contact.getFirstNickName());
                    editLastName.setText(contact.getLastNickName());

                    String displayforprofileImg=String.valueOf(contact.getFirstNickName().charAt(0)).toUpperCase()+String.valueOf(contact.getLastNickName().charAt(0)).toUpperCase();
                    firebaseDatabase.getReference().child("USER").child(ContactID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()==true)
                            {
                                String profileUrl=snapshot.child("ProfileImg").getValue(String.class);
                                if (profileUrl!=null)
                                {
                                    Glide.with(getContext()).load(profileUrl).into(profilePicture);
                                }
                                else
                                {
                                    profilePicture.setScaleType(ImageView.ScaleType.CENTER);
                                    Bitmap bitmap= DrawProfilePicture.textAsBitmap(displayforprofileImg,140, Color.WHITE);
                                    profilePicture.setImageBitmap(bitmap);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        if (ChatType== ContactListHomeAdapter.CHAT_PERSONAL) {
            firebaseDatabase.getReference().child("CONTACT").child(UserID).child(ContactID).addValueEventListener(contactListener);
        }



    }

    @Override
    public void onDetach() {
        super.onDetach();
        firebaseDatabase.getReference().removeEventListener(contactListener);
        ((ActivityUlti)getActivity()).ReattachToolbar(ContactName);
    }
}
