/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

public interface ShowTrackDrawable {

    /**
     * Get whether this drawable is showing a track. The default is true.
     *
     * @return Whether this drawable is showing a track.
     */
    boolean getShowTrack();

    /**
     * Set whether this drawable should show a track. The default is true.
     *
     * @param showTrack Whether track should be shown.
     */
    void setShowTrack(boolean showTrack);
}
