package org.brainless.telyucreative.views.mainscreen.account.upload

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityUploadKaryaBinding
import org.brainless.telyucreative.views.mainscreen.MainActivity
import org.brainless.telyucreative.views.mainscreen.account.AccountFragment

class UploadKaryaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadKaryaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadKaryaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

    }

    private fun setupActionBar() {


//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.title = ""
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeButtonEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
//        }
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }



    }


//    override fun onBackPressed() {
//        val navHost = supportFragmentManager.findFragmentById(R.id.navigation_profile)
//        navHost?.let { navFragment ->
//            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
//                if (fragment is AccountFragment) {
//                    finish()
//                } else {
//                    super.onBackPressed()
//                }
//            }
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                finish()
//                return true
//            }
//        }
//        return super.onContextItemSelected(item)
//    }
}