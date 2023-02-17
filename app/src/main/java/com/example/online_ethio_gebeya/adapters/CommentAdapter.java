package com.example.online_ethio_gebeya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutCommentBinding;
import com.example.online_ethio_gebeya.helpers.diff_calc.CommentDiffCalc;
import com.example.online_ethio_gebeya.models.Comment;
import com.example.online_ethio_gebeya.viewholders.CommentViewHolder;

import java.util.List;

public class CommentAdapter extends ListAdapter<Comment, CommentViewHolder> {
    private LayoutInflater inflater;

    public CommentAdapter(@NonNull Context context) {
        super(new CommentDiffCalc());

        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCommentBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_comment, parent, false);

        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bindView(getItem(position));
    }

    public void setCommentList(List<Comment> list) {
        if (list == null) {
            return;
        }

        submitList(list);
    }
}
