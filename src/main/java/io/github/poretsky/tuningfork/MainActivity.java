/*
 * Copyright (c) 2024 Igor B. Poretsky
 *
 * SPDX-License-Identifier: MIT
 */

package io.github.poretsky.tuningfork;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.ListFragment;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String PLAYING_ITEM = "tuning_fork_playing_item";
    private static final String SCALE_MODE = "tuning_fork_scale_mode";

    private int mode;
    private int playingItem;

    ToneGenerator tone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tone = new ToneGenerator();
        mode = 0;
        playingItem = -1;
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    tone.mute();
                    finish();
                }
            });
        setContentView(R.layout.main_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spinner = (Spinner) findViewById(R.id.mode_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.mode_names, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        if (savedInstanceState != null) {
            playingItem = savedInstanceState.getInt(PLAYING_ITEM, -1);
            mode = savedInstanceState.getInt(SCALE_MODE);
        } else if (getIntent().hasExtra(WidgetProvider.TONE)) {
            playingItem = getIntent().getIntExtra(WidgetProvider.TONE, 2);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ScaleAdapter scaleAdapter = (ScaleAdapter) getListFragment().getListAdapter();
        playingItem = scaleAdapter.getCurrentItem();
        outState.putInt(PLAYING_ITEM, playingItem);
        outState.putInt(SCALE_MODE, mode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        redisplay(mode);
        if (playingItem >= 0) {
            ScaleAdapter scaleAdapter = (ScaleAdapter) getListFragment().getListAdapter();
            int n = (playingItem < scaleAdapter.getCount()) ?
                playingItem :
                0;
            scaleAdapter.onItemClick(null, null, n, 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        tone.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean expose = tone.isPlaying();
        menu.findItem(R.id.action_stop).setEnabled(expose).setVisible(expose);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.action_stop) {
            tone.mute();
            invalidateOptionsMenu();
            ((ScaleAdapter) getListFragment().getListAdapter()).notifyDataSetChanged();
        } else if (itemId == android.R.id.home) {
            tone.mute();
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (mode != pos) {
            tone.mute();
            mode = pos;
            redisplay(pos);
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        tone.mute();
        invalidateOptionsMenu();
        ((ScaleAdapter) getListFragment().getListAdapter()).notifyDataSetChanged();
        mode = -1;
    }


    private ListFragment getListFragment() {
        return (ListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
    }

    private void redisplay(int mode) {
        ListFragment listFragment = getListFragment();
        ScaleAdapter adapter;
        switch (mode) {
        case 1:
            adapter = new ClassicGuitar(this);
            break;
        case 2:
            adapter = new RussianGuitar(this);
            break;
        default:
            adapter = new ChromaticScale(this);
            break;
        }
        listFragment.setListAdapter(adapter);
        listFragment.getListView().setOnItemClickListener(adapter);
    }

}
