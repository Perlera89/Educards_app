package com.educards.activity

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.educards.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class DeckActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var activity: DeckActivity
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var cardQuestion: CardView
    private lateinit var cardAnswer: CardView
    private lateinit var btReverse: ImageButton

    private lateinit var frontAnim: AnimatorSet
    private lateinit var backAnim: AnimatorSet
    private var isFront = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        cardQuestion = findViewById(R.id.cv_question)
        cardAnswer = findViewById(R.id.cv_answer)
        btReverse = findViewById(R.id.bt_revert)

        activity = this
        initView()
        deckAnimation()
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar_deck)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_deck)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        var navigationView: NavigationView = findViewById(R.id.nav_view_deck)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setItemIconTintList(null)

        val headerView: View = navigationView.getHeaderView(0)
        val nav_header = headerView.findViewById<LinearLayout>(R.id.nav_header)
        nav_header.setOnClickListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.deck_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_trash -> {
                Toast.makeText(this, "All decks", Toast.LENGTH_SHORT).show()
//                val aboutIntent = Intent(this, AboutActivity::class.java)
//                startActivity(aboutIntent)
            }
            R.id.menu_all_decks -> {
                Toast.makeText(this, "All decks", Toast.LENGTH_SHORT).show()
//                val aboutIntent = Intent(this, AboutActivity::class.java)
//                startActivity(aboutIntent)
            }
            R.id.menu_save_exit -> {
                Toast.makeText(this, "Save and exit", Toast.LENGTH_SHORT).show()
//                val donateIntent = Intent(this, DonateActivity::class.java)
//                startActivity(donateIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.nav_header -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                val drawer: DrawerLayout =
                    findViewById(R.id.drawer_main)
                drawer.closeDrawer(GravityCompat.START)
            }

            R.id.bt_revert -> Toast.makeText(this, "Reverse", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_decks -> {
                Toast.makeText(this, "All sets", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_all_favorites -> {
                Toast.makeText(this, "All favorites", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_about -> {
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_donate -> {
                Toast.makeText(this, "Donate", Toast.LENGTH_SHORT).show()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun deckAnimation(){
        val scale = applicationContext.resources.displayMetrics.density
        cardQuestion.cameraDistance = 8000 * scale
        cardAnswer.cameraDistance = 8000 * scale

        frontAnim = AnimatorInflater.loadAnimator(applicationContext, R.animator.front_animation) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(applicationContext, R.animator.back_animation) as AnimatorSet

        btReverse.setOnClickListener{
            if(isFront){
                frontAnim.setTarget(cardQuestion)
                backAnim.setTarget(cardAnswer)
                frontAnim.start()
                backAnim.start()

                isFront = false
            } else{
                frontAnim.setTarget(cardAnswer)
                backAnim.setTarget(cardQuestion)
                backAnim.start()
                frontAnim.start()

                isFront = true
            }
        }
    }
}