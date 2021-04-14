package fr.atraore.edl

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import fr.atraore.edl.photo.PhotoPickerFragment

class MainActivity : AppCompatActivity(), PhotoPickerFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        Log.d("MainActivity", "photos ajoutées ${photos}")
    }
}