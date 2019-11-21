package com.p4u.parvarish.video.youtubeplayer.ui.menu;

import android.view.View;

public interface YouTubePlayerMenu {
    void show(View anchorView);
    void dismiss();

    void addItem(MenuItem menuItem);
    void removeItem(int itemIndex);
    int getItemCount();
}
