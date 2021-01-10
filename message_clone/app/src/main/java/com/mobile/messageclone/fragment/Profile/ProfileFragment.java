package com.mobile.messageclone.fragment.Profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.messageclone.Ulti.DrawProfilePicture;
import com.mobile.messageclone.R;
import com.mobile.messageclone.Activity.MainActivity;
import com.mobile.messageclone.Activity.ChatActivity;
import com.mobile.messageclone.ViewModel.ChatViewModel;
import com.mobile.messageclone.Ulti.ActivityUlti;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private NavController navController;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ChatViewModel chatViewModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView displayPhoneNum;
    private TextView displayName;
    private TextView displayBio;
    private CircularImageView imgBio;
    private FirebaseStorage firebaseStorage;

    private FloatingActionButton btnAddImage;
    private String UserId;
    private LinearLayout ChangeName;
    private LinearLayout ChangeBio;
    private AppBarLayout appBarLayout;
    private Fragment fragment=this;

    private ProgressBar progressBar;


    private Uri mCropImageUri;

    private Toolbar toolbar;

    private ValueEventListener InfoListener;
    private ValueEventListener changeImageListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bio.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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


        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);

        chatViewModel.IsHideAppBar.setValue(true);
        chatViewModel.IsHideNavBar.setValue(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        UserId=firebaseAuth.getCurrentUser().getUid();


        setHasOptionsMenu(true);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_profile,container,false);
         toolbar=root.findViewById(R.id.toolbar);
        appBarLayout=root.findViewById(R.id.appBarLayout);
        btnAddImage=root.findViewById(R.id.btnChoosePicture);

        if (isAdded()) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           // ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        }

        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Your profile");

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());


                Log.d("Percent",String.valueOf(percentage));
                imgBio.setScaleX(1-percentage);
                imgBio.setScaleY(1-percentage);
                btnAddImage.setScaleX(1-percentage);
                btnAddImage.setScaleY(1-percentage);


            }
        });


        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CropImage.isExplicitCameraPermissionRequired(getContext())) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                } else {
                    CropImage.startPickImageActivity(getContext(),fragment);
                }

            }
        });



        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("SignOut","có nhấn");
        inflater.inflate(R.menu.profile_menu,menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController=Navigation.findNavController(view);
        collapsingToolbarLayout=view.findViewById(R.id.collapsing);
        displayName=view.findViewById(R.id.displayUserName);
        displayBio=view.findViewById(R.id.displayBio);
        displayPhoneNum=view.findViewById(R.id.displayPhoneNumber);
        ChangeName=view.findViewById(R.id.ChangeName);
        ChangeBio=view.findViewById(R.id.ChangeBio);
        imgBio = view.findViewById(R.id.imgBio);

        collapsingToolbarLayout.setTitle(firebaseAuth.getCurrentUser().getDisplayName());

        progressBar=view.findViewById(R.id.progressbarImage);




        InfoListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (isAdded()==true) {
                        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


                        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    }
                // ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
                    String firstName=snapshot.child("firstName").getValue(String.class);
                    String lastName=snapshot.child("lastName").getValue(String.class);
                    String bio=snapshot.child("bio").getValue(String.class);
                    String phone=snapshot.child("phoneNum").getValue(String.class);
                    displayName.setText(firstName+" "+lastName);
                    displayPhoneNum.setText(phone);
                    collapsingToolbarLayout.setTitle(firstName+" "+lastName);
                    Log.d("Phone","number: " + phone);
                    if (bio.isEmpty()==false) {
                        displayBio.setText(bio);
                    }
                    else
                    {
                        displayBio.setText("Bio");
                    }
                    changeImageListener=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()==true)
                        {
                            if (isAdded()==true) {
                                progressBar.setVisibility(View.VISIBLE);
                                imgBio.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                String imgurl = snapshot.getValue(String.class);
                                Glide.with(fragment).load(imgurl).addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        return false;
                                    }
                                }).into(imgBio);
                            }
                        }
                        else
                        {
                            imgBio.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            String text=String.valueOf(firstName.charAt(0))+String.valueOf(lastName.charAt(0));
                            Bitmap bitmap=DrawProfilePicture.textAsBitmap(text,100, Color.WHITE);
                            imgBio.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                    firebaseDatabase.getReference().child("USER").child(UserId).child("ProfileImg").addValueEventListener(changeImageListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(InfoListener);



        ChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fragment_change_name fragment_change_name1=new fragment_change_name();
                    fragment_change_name1.show(getParentFragmentManager(),null);
            }
        });
        ChangeBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BioChangeDialogFragment bioChangeDialogFragment=new BioChangeDialogFragment();
                bioChangeDialogFragment.show(getParentFragmentManager(),null);
            }
        });






    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.btnLogOut)
        {
            ((ActivityUlti)getActivity()).UpdateStatus(ChatActivity.STATUS_OFFLINE);
            Log.d("SignOut","Vo day");
            firebaseAuth.signOut();
            firebaseDatabase.getReference().removeEventListener(InfoListener);
            Intent intent=new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();

           return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((ActivityUlti)getActivity()).ReattachToolbar();
        firebaseDatabase.getReference().removeEventListener(InfoListener);
       //Toast.makeText(getContext(),"Co click",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d("Hinh","da nhan duoc");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(this).load(resultUri).into(imgBio);
                UploadImge(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
               // Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri).setCropShape(CropImageView.CropShape.OVAL).setFixAspectRatio(true)
                .start(getContext(),fragment);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseDatabase.getReference().removeEventListener(changeImageListener);
        firebaseDatabase.getReference().removeEventListener(InfoListener);
    }

    public void UploadImge(Uri imageUri)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4; // shrink it down otherwise we will use stupid amounts of memory
        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();


        UploadTask uploadtask=firebaseStorage.getReference().child("USER").child(UserId).child("UserProfileImage").child("ProfileImage").putBytes(bytes);
        uploadtask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                firebaseStorage.getReference().child("USER").child(UserId).child("UserProfileImage").child("ProfileImage").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.getResult()!=null) {
                            Log.d("Url", task.getResult().toString());
                            firebaseDatabase.getReference().child("USER").child(UserId).child("ProfileImg").setValue(task.getResult().toString());
                            chatViewModel.UserProfileImageUrl.setValue(task.getResult().toString());
                        }
                    }
                });
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Image upload unseccesful",Toast.LENGTH_SHORT).show();
            }
        });
    }


}