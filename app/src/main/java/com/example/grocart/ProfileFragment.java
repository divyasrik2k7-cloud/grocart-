package com.example.grocart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private TextView profileEmail, profilePassword;
    private Button btnLogout;
    private ImageView backBtn;
    private static final String DB_URL = "https://grocart-a0cab-default-rtdb.firebaseio.com/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        profileEmail = v.findViewById(R.id.profileEmail);
        profilePassword = v.findViewById(R.id.profilePassword);
        btnLogout = v.findViewById(R.id.btnLogout);
        backBtn = v.findViewById(R.id.backBtn);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            
            dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("users").child(uid);
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String email = snapshot.child("email").getValue(String.class);
                        String password = snapshot.child("password").getValue(String.class);
                        
                        profileEmail.setText(email);
                        profilePassword.setText(password);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        backBtn.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        btnLogout.setOnClickListener(view -> {
            auth.signOut();
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Signed out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }
}
