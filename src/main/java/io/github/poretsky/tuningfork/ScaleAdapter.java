/*
 * Copyright (c) 2024 Igor B. Poretsky
 *
 * SPDX-License-Identifier: MIT
 */

package io.github.poretsky.tuningfork;

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

    private int currentItem;


    ScaleAdapter(MainActivity activity, String[] items, double[] freqs, int[] descResIds) {
        super(activity, android.R.layout.simple_list_item_1, items);
        host = activity;
        notes = freqs;
        descriptionResIds = descResIds;
        currentItem = -1;
    }

    int getCurrentItem() {
        return currentItem;
    }

    protected void decorateItemView(TextView textView, int position, boolean active) {
        if (active) {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_music_note_black, 0);
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
        }
        textView.setContentDescription(desc.toString());
        decorateItemView(textView, position, active);
        return itemView;
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
