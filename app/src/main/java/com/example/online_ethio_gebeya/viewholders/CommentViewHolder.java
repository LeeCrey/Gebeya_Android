package com.example.online_ethio_gebeya.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutCommentBinding;
import com.example.online_ethio_gebeya.models.Comment;
import com.squareup.picasso.Picasso;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    private LayoutCommentBinding binding;

    public CommentViewHolder(@NonNull LayoutCommentBinding _binding) {
        super(_binding.getRoot());

        binding = _binding;
    }

    public void bindView(@NonNull Comment comment) {
        binding.setComment(comment);
    }

    @BindingAdapter("productImage")
    public static void setDataWithFormat(@NonNull TextView view, @NonNull String url) {
        view.setText("3/4/20");
    }
}
