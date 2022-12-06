package com.educards.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.*
import androidx.core.view.GravityCompat
import androidx.core.view.get
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
import com.educards.service.SDeck
import com.educards.service.SUser
import com.educards.util.UAlertGenericDialog
import com.educards.util.UIndexDeckOrCard
import com.educards.util.UIndexDeckOrCard.getLastKeyDeck
import com.educards.util.UListeners
import com.educards.util.UListeners.setDeckListener
import com.educards.util.UTextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
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
    private lateinit var logout: Button

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
        if (SUser.getCurrentUser()!=null && SUser.getCurrentUserDetailData().getVerified()) {
            UIndexDeckOrCard.realTimeIndexDeck()

            initView()
            initViewPager()

            val sharedPreferences = getSharedPreferences("app", MODE_PRIVATE)
            context = this

            if (isShowPageStart) {
                relative_main.visibility = View.VISIBLE
                Glide.with(this@MainActivity).load(R.mipmap.ic_launcher_round).into(page_start)
                if (sharedPreferences.getBoolean("isFirst", true)) {
                } else {
                    mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_START_PAGE, 3000)
                }
                isShowPageStart = false
            }

            if (sharedPreferences.getBoolean("isFirst", true)) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_DRAWER_LAYOUT, 2500)
            }
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
        logout = headerView.findViewById(R.id.bt_logout)

        //colocando el email del usuario logeado
        val nameUser:TextView = headerView.findViewById(R.id.tv_name)
        val emailUser:TextView = headerView.findViewById(R.id.tv_email)

        nameUser.setText(SUser.getCurrentUserDetailData().getDisplayName())
        emailUser.setText(SUser.getCurrentUserDetailData().getEmail())

        logout.setOnClickListener(this)

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

    private fun getFragments(_allDecks: ArrayList<Deck?>,_favoriteDecks: ArrayList<Deck?>):MutableList<Fragment>{
        val fragments: MutableList<Fragment> = ArrayList()
        fragments.add(DecksFragment(_allDecks))
        fragments.add(FavoritesFragment(_favoriteDecks))
        return fragments
    }

    lateinit var deckListener:ValueEventListener
    var deckData = ArrayList<Deck?>()
    var deckFavoriteData = ArrayList<Deck?>()
    var currentTabSelectedPosition:Int = 0
    private fun initViewPager() {
        tabLayout = findViewById(R.id.tab_layout_main)
        viewPager = findViewById(R.id.view_pager_main)

        viewPager.offscreenPageLimit = 2

        val mFragmentAdapter = FragmentAdapter(supportFragmentManager, getFragments(deckData,deckFavoriteData), getTitles())
        deckListener = FirebaseConnection.refGlobal.child("/decks").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                deckData.clear()
                deckFavoriteData.clear()
                currentTabSelectedPosition = tabLayout.selectedTabPosition
                snapshot.children.forEach{
                    if (it?.getValue<Deck?>()?.getIsFavorite()==true){
                        deckFavoriteData.add(it.getValue<Deck?>())
                    }
                    deckData.add(it.getValue<Deck?>())
                }
                viewPager.adapter = mFragmentAdapter
                if (currentTabSelectedPosition == 1){
                    tabLayout.selectTab(tabLayout.getTabAt(currentTabSelectedPosition))
                }

                setDeckListener(deckListener)
            }

            override fun onCancelled(error: DatabaseError) {
                UAlertGenericDialog.createDialogAlert(this@MainActivity,"Recarga de datos","Error al en la función de escucha de los mazos. \nDetalles: $error")
            }
        })
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.setTabsFromPagerAdapter(mFragmentAdapter)
        fab.show()

        //si esta viendo las cards y decide ir a todos los decks o todos los favoritos
        if (this.intent.extras?.getBoolean("allFavorites") == true){
            tabLayout.selectTab(tabLayout.getTabAt(1))
        }
    }

    private lateinit var dialog: AlertDialog
    private lateinit var etTitle: EditText
    private lateinit var titleDescription: TextInputLayout
    private lateinit var btAddDeck: MaterialButton

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bt_logout -> {
                UListeners.removeAllListeners()
                SUser.userLogout(this)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                val drawer: DrawerLayout = findViewById(R.id.drawer_main)
                drawer.closeDrawer(GravityCompat.START)
                this.finish()
            }

            R.id.fab_main -> {
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.dialog_input, null)
                btAddDeck = view.findViewById(R.id.bt_create)
                etTitle = view.findViewById(R.id.et_title)
                titleDescription = view.findViewById(R.id.titl_description)
                btAddDeck = view.findViewById(R.id.bt_create)

                titleDescription.visibility = View.VISIBLE
                builder.setView(view)

                dialog = builder.create()
                dialog.show()
                dialog.setCanceledOnTouchOutside(true)
                btAddDeck.setOnClickListener{
                    val oldPosition = viewPager.verticalScrollbarPosition
                    val newDeckKey = getLastKeyDeck()?.toInt()?.plus(1)
                    if (UTextView.verifyContentInEditText(this,etTitle,"Null or empty title field")) {
                        if (UTextView.verifyContentInEditText(this,titleDescription.editText!!,"Null or empty description field")){
                            SDeck.saveDeck(Deck(
                                "$newDeckKey",
                                etTitle.text.toString().replaceFirstChar { it.uppercase() },
                                titleDescription.editText?.text.toString(),
                                false,
                                0),this)
                            dialog.dismiss()
                        }
                    }
                    viewPager.setCurrentItem(oldPosition,false)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about -> {
                val aboutIntent = Intent(this, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
            R.id.menu_donate -> {
                val donateIntent = Intent(this, DonateActivity::class.java)
                startActivity(donateIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_decks -> {
                tabLayout.selectTab(tabLayout.getTabAt(0))
            }
            R.id.nav_all_favorites -> {
                tabLayout.selectTab(tabLayout.getTabAt(1))
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