package com.example.gsb.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gsb.R;
import com.example.gsb.data.model.User;
import com.example.gsb.databinding.FragmentUsersListBinding;
import com.example.gsb.ui.home.HomeFragment;
import com.example.gsb.ui.profile.ProfileViewModel;
import com.example.gsb.utils.SharedPrefsHelper;

import java.util.List;

public class UserListFragment extends Fragment {
    private UserListViewModel userListViewModel;
    private UserListAdapter adapter;

    private FragmentUsersListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentUsersListBinding.inflate(inflater, container, false);

        // Set up RecyclerView and Adapter
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserListAdapter();
        binding.recyclerViewUsers.setAdapter(adapter);

        // Initialize ViewModel
        userListViewModel = new ViewModelProvider(this).get(UserListViewModel.class);

        // Retrieve token from SharedPreferences
        String token = SharedPrefsHelper.getToken(requireContext());
        if (token != null) {
            userListViewModel.fetchUsers(token);
        } else {
            binding.errorTextView.setText("Aucun token trouvé. Veuillez vous connecter.");
            binding.errorTextView.setVisibility(View.VISIBLE);
        }

        observeViewModel();

        return binding.getRoot();
    }

    private void observeViewModel() {
        // Observe the users list
        userListViewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            if (users != null) {
                adapter.setUsers(users);
                binding.recyclerViewUsers.setVisibility(View.VISIBLE);
                binding.errorTextView.setVisibility(View.GONE);
            }
        });

        // Observe loading state
        userListViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        // Observe error messages
        userListViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.errorTextView.setText(error);
                binding.errorTextView.setVisibility(View.VISIBLE);
                binding.recyclerViewUsers.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonBack.setOnClickListener(v -> navigateToHomeFragment());
    }

    private void navigateToHomeFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.addToBackStack(null); // Ajoute la transaction à la pile de retour
        fragmentTransaction.commit();
    }
}

