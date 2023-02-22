package com.example.online_ethio_gebeya.helpers.diff_calc;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.online_ethio_gebeya.models.Comment;

import java.util.Objects;

public class CommentDiffCalc extends DiffUtil.ItemCallback<Comment> {
    @Override
    public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
        return oldItem.areSimilar(newItem);
    }
}
