package com.otec.crevatech.UI

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.otec.crevatech.R
import com.otec.crevatech.utils.utilJava
import com.otec.crevatech.utils.utilKotlin
import java.util.*


class MainActivity : AppCompatActivity() {


    private   var fr: Fragment? = null

    private var backPressed: Long = 0
    private var TimeLapsed: Int = 2000
    private  var decide: Boolean = false;
    private var sessionDepth = 0


    override fun onResume() {
        super.onResume()
        decide = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homepage : RelativeLayout = findViewById(R.id.homePage)
        utilKotlin().Top_status_bar(window,this,homepage)

        if(FirebaseAuth.getInstance().currentUser == null)
              startActivity(Intent(this,Login::class.java))
        else
            utilJava().openFragment(Home(),"Home",1,this)
    }


    override fun onBackPressed() {
        if (FirebaseAuth.getInstance().uid == null)
            utilKotlin().message2("Pls sign in", this)
        else {
            if (decide) {
                utilKotlin().message2("Press twice to exit", applicationContext)
                if (backPressed + TimeLapsed > System.currentTimeMillis()) {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    freeMemory()
                }
                backPressed = System.currentTimeMillis()
            } else {
                super.onBackPressed()
                assert(fr?.tag != null)
                if (fr?.tag.equals("HOME")) {
                    decide = true
                    utilKotlin().message2(fr?.tag, applicationContext)
                }
            }
        }
    }

    private fun freeMemory() {
        val fm: FragmentManager = supportFragmentManager
        for (x in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }





    override fun onStart() {
        super.onStart()
        sessionDepth++
        if (sessionDepth == 1) {
            val uid: MutableMap<String, Any> = HashMap()
            uid["app_state"] = true
            utilJava().SET_DATA_TO_CACHE(applicationContext,uid,getString(R.string.APP_STATE))

        }
    }

    override fun onStop() {
        super.onStop()
        if (sessionDepth > 0) sessionDepth--
        if (sessionDepth == 0) {
            val uid: MutableMap<String, Any> = HashMap()
            uid["app_state"] = false
            utilJava().SET_DATA_TO_CACHE(applicationContext,uid,getString(R.string.APP_STATE))
        }
    }

}


