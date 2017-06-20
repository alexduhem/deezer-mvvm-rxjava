package com.bc.alex.view.itemview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bc.alex.R;
import com.bc.alex.model.rest.Playlist;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 19/06/17.
 */

public class PlaylistItemView extends RelativeLayout {

    @BindView(R.id.imageView)
    SimpleDraweeView imageView;
    @BindView(R.id.titleTextView)
    TextView textViewTitle;

    Playlist playlist;

    public PlaylistItemView(Context context) {
        super(context);
    }

    public PlaylistItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaylistItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        textViewTitle.setText(playlist.getTitle());
        imageView.setImageURI(playlist.getPictureMediumUrl());
    }
}
