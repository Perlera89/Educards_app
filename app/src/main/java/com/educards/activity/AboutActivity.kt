package com.educards.activity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.educards.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AboutActivity : AppCompatActivity(), View.OnClickListener {
    val DESIGNED_BY = "Designed by July"
    var GIT_HUB = "https://github.com/Perlera89/Educards_app"
    var SHARE_CONTENT = "A beautiful app designed with Material Design: $DESIGNED_BY. \n$GIT_HUB"
    var EMAIL = "mailto:manuenitoo@gmailcom.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar: Toolbar = findViewById(R.id.toolbar_about)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        initView()
    }

    fun initView() {
        val animation = AnimationUtils.loadAnimation(this, R.animator.anim_about_card_show)
        val scroll_about: ScrollView = findViewById(R.id.scroll_about)
        scroll_about.startAnimation(animation)
        val ll_card_about_2_email: LinearLayout = findViewById(R.id.ll_card_about_email)
        val ll_card_about_2_git_hub: LinearLayout = findViewById(R.id.ll_card_about_github)
        val ll_card_about_source_licenses: LinearLayout = findViewById(R.id.ll_card_about_licenses)
        ll_card_about_2_email.setOnClickListener(this)
        ll_card_about_2_git_hub.setOnClickListener(this)
        ll_card_about_source_licenses.setOnClickListener(this)
        val fab: FloatingActionButton = findViewById(R.id.fab_about_share)
        fab.setOnClickListener(this)
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 300
        alphaAnimation.startOffset = 600
        val tv_about_version: TextView = findViewById(R.id.tv_about_version)
        tv_about_version.setText("1.0.0")
        tv_about_version.startAnimation(alphaAnimation)
    }

    override fun onClick(view: View) {
        val intent = Intent()
        when (view.id) {
            R.id.ll_card_about_email -> {
                intent.action = Intent.ACTION_SENDTO
                intent.data = Uri.parse(EMAIL)
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_email_intent))
                //intent.putExtra(Intent.EXTRA_TEXT, "Prueba");
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        this@AboutActivity,
                        getString(R.string.about_not_found_email),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.ll_card_about_licenses -> {
                val dialog = Dialog(this, R.style.DialogFullscreenWithTitle)
                dialog.setTitle(getString(R.string.about_licenses))
                dialog.setContentView(R.layout.dialog_licenses)
                val webView = dialog.findViewById<WebView>(R.id.web_licenses)
                webView.loadUrl("") //TODO: URL de la licencia
                val btn_source_licenses_close =
                    dialog.findViewById<Button>(R.id.btn_licenses_close)
                btn_source_licenses_close.setOnClickListener { dialog.dismiss() }
                dialog.show()
            }
            R.id.ll_card_about_github -> {
                intent.data = Uri.parse(GIT_HUB)
                intent.action = Intent.ACTION_VIEW
                startActivity(intent)
            }
            R.id.fab_about_share -> {
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, SHARE_CONTENT)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, getString(R.string.share_with)))
            }
        }
    }
}