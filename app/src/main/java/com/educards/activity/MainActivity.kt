package com.educards.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.*
import com.bumptech.glide.Glide
import com.educards.R
import com.educards.adapter.FragmentAdapter
import com.educards.fragment.DecksFragment
import com.educards.fragment.FavoritesFragment
import com.educards.model.Deck
import com.educards.service.FirebaseConnection
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var relative_main: RelativeLayout
    private lateinit var page_start: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var context: Context

    private var isShowPageStart = true
    private val MESSAGE_SHOW_DRAWER_LAYOUT = 0x001
    private val MESSAGE_SHOW_START_PAGE = 0x002


    var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_SHOW_DRAWER_LAYOUT -> {
                    drawer.openDrawer(androidx.core.view.GravityCompat.START)
                    val sharedPreferences = getSharedPreferences("app", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isFirst", false)
                    editor.apply()
                }
                MESSAGE_SHOW_START_PAGE -> {
                    val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
                    alphaAnimation.duration = 300
                    alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {}
                        override fun onAnimationEnd(animation: Animation) {
                            relative_main.visibility = View.GONE
                        }

                        override fun onAnimationRepeat(animation: Animation) {}
                    })
                    relative_main.startAnimation(alphaAnimation)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initViewPager()

        val sharedPreferences = getSharedPreferences("app", MODE_PRIVATE)
        context = this

        if (isShowPageStart) {
            relative_main.visibility = View.VISIBLE
            Glide.with(this@MainActivity).load(R.mipmap.ic_launcher_round).into(page_start)
            if (sharedPreferences.getBoolean("isFirst", true)) {
            } else {
                mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_START_PAGE, 100)
            }
            isShowPageStart = false
        }

        if (sharedPreferences.getBoolean("isFirst", true)) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_DRAWER_LAYOUT, 2500)
        }
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_main)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        var navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setItemIconTintList(null)

        val headerView: View = navigationView.getHeaderView(0)
        val nav_header = headerView.findViewById<LinearLayout>(R.id.nav_header)
        nav_header.setOnClickListener(this)

        fab = findViewById(R.id.fab_main)
        fab.setOnClickListener(this)

        relative_main = findViewById(R.id.relative_main)
        page_start = findViewById(R.id.img_page_start)
    }

    private fun getTitles():MutableList<String>{
        val titles: MutableList<String> = ArrayList()
        titles.add("All Decks")
        titles.add("Favorites")
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]))
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]))
        return titles
    }

    private fun getFragments(_decks: ArrayList<Deck?>):MutableList<Fragment>{
        val fragments: MutableList<Fragment> = ArrayList()
        fragments.add(DecksFragment(_decks))
        fragments.add(FavoritesFragment())
        return fragments
    }

    private fun initViewPager() {
        tabLayout = findViewById(R.id.tab_layout_main)
        viewPager = findViewById(R.id.view_pager_main)

        var deckData = ArrayList<Deck?>()

        viewPager.offscreenPageLimit = 2

        val mFragmentAdapter = FragmentAdapter(supportFragmentManager, getFragments(deckData), getTitles())
        FirebaseConnection.refGlobal.child("/decks").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                deckData.clear()
                snapshot.children.forEach{
                    deckData.add(it.getValue<Deck?>())
                }
                viewPager.adapter = mFragmentAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("mainActivity", "Error al escuchar los cambios en la rama /decks. detalles: $error")
            }
        })
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.setTabsFromPagerAdapter(mFragmentAdapter)
        fab.show()
    }

    private lateinit var dialog: AlertDialog
    override fun onClick(view: View) {
        when (view.id) {
            R.id.nav_header -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                val drawer: DrawerLayout =
                    findViewById(R.id.drawer_main)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.fab_main -> {
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.dialog_input_deck, null)
                builder.setView(view)

                dialog = builder.create()
                dialog.show()
            }
        }
    }

    fun createDeck(view: View){
        val deckIntent = Intent(this, DeckActivity::class.java)
        startActivity(deckIntent)
        dialog.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_all_decks -> {
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_save_exit -> {
                Toast.makeText(this, "Donate", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_decks -> {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
            }
            R.id.nav_all_favorites -> {
                Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_about -> {
                val aboutIntent = Intent(this, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
            R.id.nav_donate -> {
                val donateIntent = Intent(this, DonateActivity::class.java)
                startActivity(donateIntent)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}