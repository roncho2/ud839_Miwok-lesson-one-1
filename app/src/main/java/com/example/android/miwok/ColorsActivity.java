/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;



    //This listener gets triggered when the {@link MediaPlayer} has completed playing the
    //Audio file.
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);


        //Creating an Arrays of Words
        final ArrayList<Word> words = new ArrayList<Word>();


        words.add(new Word("red", "wetetti", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("mustard yellow", "chiwiite",R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        words.add(new Word("dusty yellow","topiise", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "takaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "topoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));


        //Create {@link ArrayAdapter} whose data source is a list of Strings.
        //Adapter knows how to create layout for each items in the list, using the
        //simple_list_item_1.xml resource layout defined in the android framework.
        //This list item layout contains a single {@link TextView}, which the adapter will set
        //to display a single word.
        WordAdapter adapter =  new WordAdapter (this, words, R.color.category_colors);


        //Find the {@link listView in the view hierarchy of the {@link Activity}.
        //There should be {@link listView} with the view ID called list, which is declared in
        //activity_number.xml layout file.
        ListView listView = (ListView) findViewById (R.id.list);


        //Make the {@link listView} use the {@link ArrayAdapter}, we create above so that
        //{@link listView} will display list items for each words in the list of words.
        //Do this by calling the setAdapter method on the {@link ListView} object and
        //pass in one argument which is the {@link ArrayAdapter} with the variable
        //Name itemsAdapter.
        listView.setAdapter(adapter);

        //Setting a clickListener to the TextView to play a media player.
        //Setting the click listener to play the audio when the list item is click on.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the {@link word} object at the given position the user click on.
                Word word = words.get(position);


                //Release the Media Player if its currently exist, because
                //we are about to play a different sound file.
                releaseMediaPlayer();


                //create and set up the {@link media player} for the audio resource associated with
                //the current word.
                mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getmAudioResourceId());
                //Start the Audio file.
                mMediaPlayer.start();


                //Setting up a listener on the mediaPlayer so we can stop and start
                //the MediaPlayer once the sound has finished playing.
                mMediaPlayer.setOnCompletionListener (mCompletionListener);


            }
        });

    }


    //Clean up the mediaPlayer by releasing its resources.
    private void releaseMediaPlayer(){
        //if the mediaPlayer is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            //Regardless of the current state of the mediaPlayer release its resources
            //Because we no longer need it.
            mMediaPlayer.release();


            //Set the mediaPlay back to null for our code we have decided that
            //Setting the mediaPlayer to null is an easy way to tell that the media player
            //Is not configured to play and Audio file at the moment.
            mMediaPlayer = null;
        }
    }
}
