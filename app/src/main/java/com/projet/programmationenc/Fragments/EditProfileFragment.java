package com.projet.programmationenc.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projet.programmationenc.ui.HomeActivity;
import com.projet.programmationenc.Moduls.Chat;
import com.projet.programmationenc.Moduls.Comment;
import com.projet.programmationenc.Moduls.Friend;
import com.projet.programmationenc.Moduls.LastChat;
import com.projet.programmationenc.Moduls.Post;
import com.projet.programmationenc.Moduls.Student;
import com.projet.programmationenc.R;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {
    //    private final int code = 1;
    private static final String TAG = "EditProfileFragment";
    private FirebaseUser user;
    private String firstnameedit, lastnameedit;
    private CircleImageView imgvavataredit;
    private TextInputLayout edtlastnameedit, edtfirstnameedit;
    private TextView txtvchangeavatar;
    private Button btnconfirmedit;
    private ImageButton btnremoveavatar;
    private DatabaseReference databaseReference;
    private Uri imgavataruri;
    private StorageReference storageReference;
    private Student S;
    private ProgressBar progressBareditprofile;
    private Post P;
    private Comment C;
    private Friend F;
    private Chat CT;
    private LastChat LC;
    private boolean avatarchosen = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editprofile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Modification du profil");
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        imgvavataredit = view.findViewById(R.id.imgvavataredit);
        edtlastnameedit = view.findViewById(R.id.edtlastnameedit);
        edtfirstnameedit = view.findViewById(R.id.edtfirstnameedit);
        btnconfirmedit = view.findViewById(R.id.btnconfirmedit);
        btnremoveavatar = view.findViewById(R.id.btnremoveavatar);
        txtvchangeavatar = view.findViewById(R.id.txtvchangeavatar);
        progressBareditprofile = view.findViewById(R.id.progressBareditprofile);

        edtfirstnameedit.getEditText().setText(((HomeActivity) getActivity()).retrievedFirstName);
        edtlastnameedit.getEditText().setText(((HomeActivity) getActivity()).retrievedLastName);

        imgavataruri = Uri.parse(((HomeActivity) getActivity()).retrievedAvatar);
        Glide.with(getActivity())
                .load(imgavataruri)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgvavataredit);


        imgvavataredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFile();
            }
        });

        txtvchangeavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFile();
            }
        });

        btnremoveavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgavataruri = Uri.parse("android.resource://com.projet.programmationenc/drawable/ic_baseline_person_black_110");
                Glide.with(getActivity())
                        .load(imgavataruri)
                        .apply(RequestOptions.fitCenterTransform())
                        .into(imgvavataredit);
            }
        });

        btnconfirmedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstnameedit = edtfirstnameedit.getEditText().getText().toString();
                lastnameedit = edtlastnameedit.getEditText().getText().toString();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-zA-Z]{2,6}";
                boolean flag = true;

                if (firstnameedit.isEmpty()) {
                    edtfirstnameedit.setError("Veuillez saisir le prénom.");
                    flag = false;
                }
                else {
                    edtfirstnameedit.setErrorEnabled(false);
                }

                if (lastnameedit.isEmpty()) {
                    edtlastnameedit.setError("Veuillez saisir le nom.");
                    flag = false;
                }
                else {
                    edtlastnameedit.setErrorEnabled(false);
                }

                if (!flag) {
                    return;
                } else {
                    progressBareditprofile.setVisibility(View.VISIBLE);
                    if (avatarchosen) {
                        StorageReference ref = storageReference.child("students_avatars/" + imgavataruri.getLastPathSegment());
                        ref.putFile(imgavataruri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.e(TAG, "onSuccess: image uploaded");
                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                EditStudentProfile(uri);
                                            }
                                        });
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: image not uploaded");
                            }
                        });
                    } else {
                        EditStudentProfile(imgavataruri);
                    }
                }
            }
        });

    }

    private void openImageFile() {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                imgavataruri = result.getUri();

                Glide.with(getActivity())
                        .load(imgavataruri)
                        .apply(RequestOptions.fitCenterTransform())
                        .into(imgvavataredit);

                avatarchosen = true;
            }
        }
    }

    public void EditStudentProfile(Uri uri) {
        databaseReference.child("Students").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    S = dataSnapshot.getValue(Student.class);
                    S.setFirstName(firstnameedit);
                    S.setLastName(lastnameedit);
                    S.setAvatar(uri.toString());
                    databaseReference.child("Students").child(user.getUid()).child("firstName").setValue(S.getFirstName());
                    databaseReference.child("Students").child(user.getUid()).child("lastName").setValue(S.getLastName());
                    databaseReference.child("Students").child(user.getUid()).child("avatar").setValue(S.getAvatar());

                    databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    P = dataSnapshot1.getValue(Post.class);
                                    P.setStudentFullName(firstnameedit + " " + lastnameedit);
                                    P.setStudentAvatar(uri.toString());
                                    if(P.getStudentID().equals(user.getUid())) {
                                        databaseReference.child("Posts").child(dataSnapshot1.getKey()).child("studentFullName").setValue(P.getStudentFullName());
                                        databaseReference.child("Posts").child(dataSnapshot1.getKey()).child("studentAvatar").setValue(P.getStudentAvatar());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                        }
                    });

                    databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    databaseReference.child("Posts").child(dataSnapshot1.getKey()).child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()) {
                                                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                                    C = dataSnapshot2.getValue(Comment.class);
                                                    C.setFullNameComment(firstnameedit + " " + lastnameedit);
                                                    C.setAvatarComment(uri.toString());
                                                    if(C.getCommentID().equals(user.getUid())) {
                                                        databaseReference.child("Posts").child(dataSnapshot1.getKey()).child("Comments").child(dataSnapshot2.getKey()).child("fullNameComment").setValue(C.getFullNameComment());
                                                        databaseReference.child("Posts").child(dataSnapshot1.getKey()).child("Comments").child(dataSnapshot2.getKey()).child("avatarComment").setValue(C.getAvatarComment());
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                        }
                    });

                    databaseReference.child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    if(dataSnapshot1.hasChild(user.getUid())) {
                                        F = dataSnapshot1.child(user.getUid()).getValue(Friend.class);
                                        F.setFriendFirstName(firstnameedit);
                                        F.setFriendLastName(lastnameedit);
                                        F.setFriendAvatar(uri.toString());
                                        databaseReference.child("Friends").child(dataSnapshot1.getKey()).child(user.getUid()).child("friendFirstName").setValue(F.getFriendFirstName());
                                        databaseReference.child("Friends").child(dataSnapshot1.getKey()).child(user.getUid()).child("friendLastName").setValue(F.getFriendLastName());
                                        databaseReference.child("Friends").child(dataSnapshot1.getKey()).child(user.getUid()).child("friendAvatar").setValue(F.getFriendAvatar());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    databaseReference.child("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                        for(DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                            if(dataSnapshot3.child("receiver").getValue(String.class).equals(user.getUid())) {
                                                CT = dataSnapshot3.getValue(Chat.class);
                                                CT.setAvatarReceiver(uri.toString());
                                                databaseReference.child("Chats").child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child(dataSnapshot3.getKey()).child("avatarReceiver").setValue(CT.getAvatarReceiver());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    databaseReference.child("Last Chats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                        if(dataSnapshot2.child("senderId").getValue(String.class).equals(user.getUid())) {
                                            LC = dataSnapshot2.getValue(LastChat.class);
                                            LC.setSenderFirstName(firstnameedit);
                                            LC.setSenderLastName(lastnameedit);
                                            LC.setSenderAvatar(uri.toString());
                                            databaseReference.child("Last Chats").child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("senderFirstName").setValue(LC.getSenderFirstName());
                                            databaseReference.child("Last Chats").child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("senderLastName").setValue(LC.getSenderLastName());
                                            databaseReference.child("Last Chats").child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("senderAvatar").setValue(LC.getSenderAvatar());
                                        }
                                        else if(dataSnapshot2.child("receiverId").getValue(String.class).equals(user.getUid())) {
                                            LC = dataSnapshot2.getValue(LastChat.class);
                                            LC.setReceiverFirstName(firstnameedit);
                                            LC.setReceiverLastName(lastnameedit);
                                            LC.setReceiverAvatar(uri.toString());
                                            databaseReference.child("Last Chats").child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("receiverFirstName").setValue(LC.getReceiverFirstName());
                                            databaseReference.child("Last Chats").child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("receiverLastName").setValue(LC.getReceiverLastName());
                                            databaseReference.child("Last Chats").child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("receiverAvatar").setValue(LC.getReceiverAvatar());
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    progressBareditprofile.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Modification réussie !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

//    public String BitMapToString() {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG ,100, baos);
//        byte[] b = baos.toByteArray();
//        return Base64.encodeToString(b, Base64.DEFAULT);
//    }

//    public void updateStudentProfile(String base_url) {
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        bitmap.compress(Bitmap.CompressFormat.JPEG ,100, baos);
////        byte[] b = baos.toByteArray();
////        String temp = Base64.encodeToString(b, Base64.DEFAULT);
//
////        Log.e(TAG, "onClick: temp : " + temp);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(base_url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
//        Call<Student> call = apiInterface.updateStudent(user.getUid(),firstnameedit,lastnameedit,downloadedUri.toString());
//
//        call.enqueue(new Callback<Student>() {
//            @Override
//            public void onResponse(Call<Student> call, Response<Student> response) {
//                if(!response.isSuccessful()) {
//                    Log.e(TAG, "onResponse: Code " + response.code());
//                    return;
//                }
//                Log.e(TAG, "onResponse: " + "Data updates");
//                Toast.makeText(getContext(),"Modification réussie !",Toast.LENGTH_SHORT).show();
//
//                getActivity().finish();
//                getActivity().overridePendingTransition(0, 0);
//                startActivity(getActivity().getIntent());
//                getActivity().overridePendingTransition(0, 0);
//
//            }
//
//            @Override
//            public void onFailure(Call<Student> call, Throwable t) {
//                Log.e(TAG, "onFailure: " + t.getMessage());
//            }
//        });
//    }

}


//                    storageReference.child("students_avatars/"+user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            downloadedUri = uri;
//                        }
//                    });


//                            if(taskSnapshot.getMetadata() != null) {
//                                if(taskSnapshot.getMetadata().getReference() != null) {
//                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            final Uri downloadUri = uri;
//                                            Log.e(TAG, "onSuccess: URI downloaded" + downloadUri);
//                                        }
//                                    });
//                                }
//                            }

