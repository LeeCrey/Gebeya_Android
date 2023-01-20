package com.example.online_ethio_gebeya.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.callbacks.SingleProductCallBack;
import com.example.online_ethio_gebeya.databinding.LayoutProductBinding;
import com.example.online_ethio_gebeya.helpers.ProductItemCallBack;
import com.example.online_ethio_gebeya.models.Product;
import com.example.online_ethio_gebeya.viewholders.ProductViewHolder;

import java.util.List;

public class ProductAdapter extends ListAdapter<Product, ProductViewHolder> {
    private static final String TAG = "ProductAdapter";
    protected final LayoutInflater inflater;
    protected final Activity activity;
    private SingleProductCallBack callBack;
    private boolean calculateWidth;

    public ProductAdapter(Fragment activity) {
        super(new ProductItemCallBack());

        this.activity = activity.getActivity();
        inflater = LayoutInflater.from(this.activity);
        calculateWidth = false;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutProductBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_product, parent, false);

        View view = binding.getRoot();

        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        if (calculateWidth) {
            params.width = 350;
        }

        ProductViewHolder vh = new ProductViewHolder(binding);

        // on click listener...
        view.setOnClickListener(v -> {
            int pos = vh.getAdapterPosition();
            if (callBack != null) {
                callBack.onProductClick(getClickedItem(pos));
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bindView(activity, getItem(position));
    }

    public void setCalculateProductWidth(boolean b) {
        calculateWidth = b;
    }

    public void setCallBack(@NonNull SingleProductCallBack callBack) {
        this.callBack = callBack;
    }

    public void setProducts(final List<Product> list) {
        if (list == null) {
            return;
        }

        if (list.isEmpty()) {
            return;
        }

        submitList(list);
    }

    public Product getClickedItem(int position) {
        return getItem(position);
    }
}
