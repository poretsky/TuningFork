/*
 * Copyright (c) 2024 Igor B. Poretsky
 *
 * SPDX-License-Identifier: MIT
 */

package io.github.poretsky.tuningfork;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

class ScaleAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

    private final MainActivity host;
    private final double[] notes;
    private final int[] descriptionResIds;

    private final boolean isNightMode;

    private int currentItem;


    ScaleAdapter(MainActivity activity, String[] items, double[] freqs, int[] descResIds) {
        super(activity, android.R.layout.simple_list_item_1, items);
        host = activity;
        notes = freqs;
        descriptionResIds = descResIds;
        isNightMode = (host.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        currentItem = -1;
    }

    int getCurrentItem() {
        return currentItem;
    }

    protected void decorateItemView(TextView textView, int position, boolean active) {
        if (active) {
            textView.setTextColor(ContextCompat.getColor(host, android.R.color.holo_green_dark));
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, isNightMode ? R.drawable.ic_music_note_white : R.drawable.ic_music_note_black, 0);
        } else {
            textView.setTextColor(ContextCompat.getColor(host, isNightMode ? android.R.color.white : android.R.color.black));
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = super.getView(position, convertView, parent);
        TextView textView = (TextView) itemView.findViewById(android.R.id.text1);
        StringBuilder desc = new StringBuilder(getContext().getString(descriptionResIds[position]));
        boolean active = host.tone.isPlaying() && (position == currentItem);
        if (active) {
            desc.append(' ')
                .append(getContext().getString(R.string.sounding));
        } else {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        }
        textView.setContentDescription(desc.toString());
        decorateItemView(textView, position, active);
        return itemView;
    }

    @Override
    public void notifyDataSetChanged() {
        if (!host.tone.isPlaying())
            currentItem = -1;
        super.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        if (host.tone.isPlaying() && (position == currentItem)) {
            host.tone.mute();
        } else {
            host.tone.start(notes[position]);
            currentItem = position;
        }
        host.invalidateOptionsMenu();
        notifyDataSetChanged();
    }

}
