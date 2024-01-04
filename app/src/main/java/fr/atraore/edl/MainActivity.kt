package fr.atraore.edl

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

const val REQUEST_CODE_CAMERA_WRITE_STORAGE = 400

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    lateinit var mNavigationFragment: OnNavigationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        checkPermissions()
    }

    @AfterPermissionGranted(REQUEST_CODE_CAMERA_WRITE_STORAGE)
    private fun checkPermissions() {
        if (!EasyPermissions.hasPermissions(this, CAMERA, WRITE_EXTERNAL_STORAGE)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_camera),
                REQUEST_CODE_CAMERA_WRITE_STORAGE,
                CAMERA, WRITE_EXTERNAL_STORAGE
            )
        }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        // Some permissions have been granted
        // ...
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        // Some permissions have been denied
        // ...
    }
}