package com.codepath.finderapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.codepath.finderapp.CameraPreview;
import com.codepath.finderapp.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by chmanish on 11/11/16.
 */
public class CameraFragment extends Fragment {
    public static final String TAG = "CameraFragment";

    private int rotation = 270;
    private static ImageButton photoButton;


    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;

    public interface CameraFragmentDialogListener {
        void createParseFile(byte[] data);

    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            Camera.Parameters params = c.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            c.setParameters(params);
            photoButton.setEnabled(true);
        }
        catch (Exception e){
            Log.e(TAG, "No camera with exception: " + e.getMessage());
            photoButton.setEnabled(false);
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, parent, false);
        photoButton = (ImageButton) v.findViewById(R.id.camera_photo_button);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(getActivity(), mCamera);
        preview = (FrameLayout) v.findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCamera == null) {
                    return;
                }
                mCamera.takePicture(new Camera.ShutterCallback() {

                    @Override
                    public void onShutter() {
                        // nothing to do
                    }

                }, null, new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        saveScaledPhoto(data);
                    }

                });

            }
        });

        return v;
    }

    private void startCaptionFragment(Bitmap rotatedScaledMealImage) {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        SaveCaptionFragment saveCaptionFragment = SaveCaptionFragment.newInstance(rotatedScaledMealImage);
        saveCaptionFragment.show(fm, "fragment_save_caption");

    }

    /*
     * ParseQueryAdapter loads ParseFiles into a ParseImageView at whatever size
     * they are saved. Since we never need a full-size image in our app, we'll
     * save a scaled one right away.
     */
    private void saveScaledPhoto(byte[] data) {

        // Resize photo from camera byte array
        Bitmap mealImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap mealImageScaled = Bitmap.createScaledBitmap(mealImage, 960, 960
                * mealImage.getHeight() / mealImage.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap rotatedScaledMealImage = Bitmap.createBitmap(mealImageScaled, 0,
                0, mealImageScaled.getWidth(), mealImageScaled.getHeight(),
                matrix, false);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        byte[] scaledData = bos.toByteArray();

        // Save the scaled image to Parse
        CameraFragmentDialogListener mListener = (CameraFragmentDialogListener) getActivity();
        mListener.createParseFile(scaledData);

        startCaptionFragment(rotatedScaledMealImage);


    }


    @Override
    public void onResume() {
        super.onResume();

        if (mCamera == null){

            mCamera = getCameraInstance();
            preview.removeAllViews();
            mPreview = new CameraPreview(getActivity(), mCamera);
            preview.addView(mPreview);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
