package com.example.online_ethio_gebeya.ui.fragments.temporary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.WalletApi;
import com.example.online_ethio_gebeya.databinding.FragmentWalletBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.models.Customer;
import com.facebook.shimmer.ShimmerFrameLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// @Author Solomon Boloshe    Feb 28, 2023
// For the sake of simulation
// I didn't like it
// just don't hesitate to delete
public class FragmentWallet extends Fragment {
    private FragmentWalletBinding binding;
    private Call<Customer> call;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ShimmerFrameLayout balanceLayout = binding.balanceShimmer;
        balanceLayout.startShimmer();

        String token = PreferenceHelper.getAuthToken(requireContext());
        WalletApi api = RetrofitConnectionUtil.getRetrofitInstance(requireActivity().getApplication()).create(WalletApi.class);
        call = api.balance(token);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer c = response.body();
                    if (c != null) {
                        TextView textView = binding.balance;
                        balanceLayout.stopShimmer();
                        textView.setBackground(null);
                        textView.setText(c.getBalance());
                    }
                } else {
                    if (response.code() == 401) {
                        PreferenceHelper.clearPref(requireContext());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        call.cancel();
        binding = null;
    }
}
