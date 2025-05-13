package com.wealthwise.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import com.wealthwise.R;
import com.wealthwise.adapters.ConsultantAdapter;
import com.wealthwise.models.Consultant;

import java.util.ArrayList;
import java.util.List;

public class ViewConsultantsFragment extends Fragment {

    private static final String TAG = "ViewConsultantsFrag";

    private RecyclerView recyclerViewConsultants;
    private ConsultantAdapter consultantAdapter;
    private ProgressBar progressBarConsultants;
    private TextView textViewNoConsultants;

    private FirebaseFirestore db;
    private ListenerRegistration consultantsListenerRegistration;
    private List<Consultant> consultantList = new ArrayList<>();

    public ViewConsultantsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_consultants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewConsultants = view.findViewById(R.id.recyclerViewConsultants);
        progressBarConsultants = view.findViewById(R.id.progressBarConsultants);
        textViewNoConsultants = view.findViewById(R.id.textViewNoConsultants);

        recyclerViewConsultants.setLayoutManager(new LinearLayoutManager(getContext()));
        consultantAdapter = new ConsultantAdapter(getContext(), consultantList);
        recyclerViewConsultants.setAdapter(consultantAdapter);

        listenForConsultants();
    }

    private void listenForConsultants() {
        progressBarConsultants.setVisibility(View.VISIBLE);
        textViewNoConsultants.setVisibility(View.GONE);
        recyclerViewConsultants.setVisibility(View.GONE);

        Query query = db.collection("consultants").orderBy("rating", Query.Direction.DESCENDING);

        consultantsListenerRegistration = query.addSnapshotListener((snapshots, e) -> {
            progressBarConsultants.setVisibility(View.GONE);

            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                showError("Failed to load consultants: " + e.getMessage());
                return;
            }

            List<Consultant> newConsultants = new ArrayList<>();
            if (snapshots != null) {
                for (QueryDocumentSnapshot doc : snapshots) {
                    Consultant consultant = doc.toObject(Consultant.class);
                    newConsultants.add(consultant);
                }
            }

            consultantList.clear();
            consultantList.addAll(newConsultants);
            consultantAdapter.updateData(consultantList);

            if (consultantList.isEmpty()) {
                textViewNoConsultants.setVisibility(View.VISIBLE);
                recyclerViewConsultants.setVisibility(View.GONE);
            } else {
                textViewNoConsultants.setVisibility(View.GONE);
                recyclerViewConsultants.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showError(String message) {
        Log.e(TAG, message);
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (consultantsListenerRegistration != null) {
            consultantsListenerRegistration.remove();
        }
    }
}
