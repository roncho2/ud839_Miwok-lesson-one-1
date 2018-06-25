package com.example.android.miwok;

public class Word {


    //DefaultTranslation for the word.
    private String mDfaultTranslation;


    //MiwokTranslation for the word.
    private String mMiwokTranslation;


    //Adding private resource ID for the word
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    private static final int NO_IMAGE_PROVIDED = - 1;



    //Audio Resource ID for the word.
    private int mAudioResourceId;


    //Create a new word object
    //@param defaultTranslation is the word that user understands
    //English.
    //@param miwokTranslation is the word that the user understands
    //such as miwok language
    public Word(String defaultTranslation, String miwokTranslation, int audioResourceId ){
        mDfaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }

    //@param the imageResourceID is the Drawable resource ID for the image.
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId , int audioResourceId){
        mDfaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }


    //Get the defaultTranslation of the word.
    public String getmDfaultTranslation(){
        return mDfaultTranslation;
    }

    //Get the miwokTranslation of the word.
    public String getmMiwokTranslation(){
        return mMiwokTranslation;
    }


    public int getImageResourceId() {
        return mImageResourceId;
    }

    //Returns weather or no there is an image for this word.
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

      //Return the AudioResourceId for the word.
        public int getmAudioResourceId() {
            return mAudioResourceId;
        }

    }

