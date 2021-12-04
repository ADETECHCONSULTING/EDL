package fr.atraore.edl

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.photo.PhotoPickerFragment
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var mNavigationFragment: OnNavigationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_item, menu)
        return true
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
        if (this::mNavigationFragment.isInitialized) {
            mNavigationFragment.navigateFragment(actionNext)
        }
    }

    interface OnNavigationFragment {
        fun navigateFragment(actionNext: Boolean)
    }
}