package fr.atraore.edl

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.photo.PhotoPickerFragment
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.first_page.StartConstatFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PhotoPickerFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        Log.d("MainActivity", "photos ajoutées ${photos}")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_next -> {
                goToFragment(true)
            }
            R.id.action_previous -> {
                goToFragment(false)
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun goToFragment(actionNext: Boolean) {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (fragment is BaseFragment) {
            if (actionNext) {
                fragment.goNext()
            } else {
                fragment.goBack()
            }
        }
    }
}