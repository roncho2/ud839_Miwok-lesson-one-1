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

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

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


        //Create a list of words.
       final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinne oyaase`ne", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michekses?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I'm feeling good.","kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "eenes'aa?", R.raw.phrase_yes_im_coming));
        words.add(new Word("Yes, I'm coming.", "hee`eenem", R.raw.phrase_yes_im_coming));
        words.add(new Word("I'm coming.", "eenem", R.raw.phrase_im_coming));
        words.add(new Word("Let's go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "enni'nem", R.raw.phrase_come_here));


        //Create {@link ArrayAdapter} whose data source is a list of Strings.
        //Adapter knows how to create layout for each items in the list, using the
        //simple_list_item_1.xml resource layout defined in the android framework.
        //This list item layout contains a single {@link TextView}, which the adapter will set
        //to display a single word.
        WordAdapter adapter =  new WordAdapter (this, words, R.color.category_phrases);


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


                //create and set up the {@link media player} for the audio resource associated with
                //the current word.
                mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getmAudioResourceId());
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


