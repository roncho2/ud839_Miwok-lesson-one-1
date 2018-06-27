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
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;


    //Handles Audio player when playing a sound file.
    private AudioManager mAudioManager;


    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {

                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        //The AUDIOFOCUS_LOSS_TRANSIENT means we have lost Audio Focus.
                        //at short amount of time.
                        // AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK means that our app is allowed to
                        //Continue playing sound but on a low volume. with
                        //Both cases because oud app is playing short sound files.

                        //pause playBack and reset plack and reset to play and the start of the
                        //file. That way we play , We play the word from the beginning when we
                        //resume playBack.
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                        //pause PlayBack.

                    } else if ( focusChange == AudioManager.AUDIOFOCUS_GAIN){
                        //AUDIOFOCUS_GAIN Means we have regain focus and we can start playBack.
                        mMediaPlayer.start();

                    } else if ( focusChange == AudioManager.AUDIOFOCUS_LOSS){
                        //AUDIOFOCUS_LOSS Means we have lost Audio focus and
                        //Stop playBack and also release Resources.
                        releaseMediaPlayer();

                    }

                }

            };



    //This listener gets triggered when the {@link MediaPlayer} has completed playing the
    //Audio file.
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();

        }

    };


    @Override
    protected void onStop() {
        super.onStop();
        //When the Activity is stopped Release the media player , because we won't be
        //playing any more sound.
        releaseMediaPlayer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //Create and setup the {@link AudioManager} to Request Audio Focus.
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);



        //Creating an Arrays of Words
       final ArrayList<Word> words = new ArrayList<Word>();


        words.add(new Word("father", "epe", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "eta", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son","angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_younger_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "tete", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "klliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother" , "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather" , "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        //Create {@link ArrayAdapter} whose data source is a list of Strings.
        //Adapter knows how to create layout for each items in the list, using the
        //simple_list_item_1.xml resource layout defined in the android framework.
        //This list item layout contains a single {@link TextView}, which the adapter will set
        //to display a single word.
        WordAdapter adapter =  new WordAdapter (this, words, R.color.category_family);


        //Find the {@link listView in the view hierarchy of the {@link Actvity}.
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


                //Request Audio Focus for Play back.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //Use the music Stream.
                        AudioManager.STREAM_MUSIC,
                        //Request Permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                    //create and set up the {@link media player} for the audio resource associated with
                    //the current word.
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getmAudioResourceId());
                    //Start the Audio file.
                    mMediaPlayer.start();


                    //Setting up a listener on the mediaPlayer so we can stop and start
                    //the MediaPlayer once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);

                }

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

            //Regardless weather or not we were granted Audio focus, abandon it,
            //This also unregister the AudioFocusChangeListener so we don't get any
            //More call back.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
