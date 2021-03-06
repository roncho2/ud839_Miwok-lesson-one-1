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

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RemoteController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {


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


        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekasu", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo`e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na`aacha", R.drawable.number_ten, R.raw.number_ten));

        //Create {@link ArrayAdapter} whose data source is a list of Strings.
        //Adapter knows how to create layout for each items in the list, using the
        //simple_list_item_1.xml resource layout defined in the android framework.
        //This list item layout contains a single {@link TextView}, which the adapter will set
        //to display a single word.
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);


        //Find the {@link listView in the view hierarchy of the {@link Actvity}.
        //There should be {@link listView} with the view ID called list, which is declared in
        //activity_number.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);


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

                if ( result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {



                        //create and set up the {@link media player} for the audio resource associated with
                       //the current word.
                       mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getmAudioResourceId());
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