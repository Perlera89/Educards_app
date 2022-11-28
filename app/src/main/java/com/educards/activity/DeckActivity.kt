package com.educards.activity

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.educards.R
import com.educards.adapter.RecyclerCardAdapter
import com.educards.adapter.RecyclerDeckAdapter
import com.educards.fragment.FavoritesFragment
import com.educards.model.Deck
import com.educards.model.entities.Card
import com.educards.service.FirebaseConnection
import com.educards.util.IndexDeckOrCard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firestore.admin.v1.Index

class DeckActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var recyclerCard: RecyclerView
    private lateinit var activity: DeckActivity
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var btReverse: ImageButton

    private lateinit var fabStudy: FloatingActionButton
    private lateinit var fabAdd: FloatingActionButton

    var cardsData = ArrayList<Card>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerCard = findViewById(R.id.recycler_cards)
        fabStudy = findViewById(R.id.fab_study)
        fabAdd = findViewById(R.id.fab_add)

        btReverse = findViewById(R.id.fab_revert)

        activity = this
        val bundle = this.intent.extras
        IndexDeckOrCard.realTimeIndexDeck()
        IndexDeckOrCard.selectedDeckKey = bundle?.getString("idDeck").toString()
        IndexDeckOrCard.realTimeIndexCardInSelectedDeck()

        var horizontalLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecyclerCardAdapter(cardsData, btReverse, this, activity)

        FirebaseConnection.refGlobal.child("/cards").child("${bundle?.getString("idDeck").toString()}")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    cardsData.clear()
                    snapshot.children.forEach{
                        if (it != null){
                            cardsData.add(it.getValue<Card>() as Card)
                        }
                    }
                    recyclerCard.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        //recyclerCard.setHasFixedSize(true)
        recyclerCard.layoutManager = horizontalLayoutManager
       // recyclerCard.adapter = adapter

        initView()
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

        fabStudy.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.deck_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_trash -> {
                Toast.makeText(this, "All decks", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_all_decks -> {
                Toast.makeText(this, "All decks", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_save_exit -> {
                Toast.makeText(this, "Save and exit", Toast.LENGTH_SHORT).show()
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
                finish()
            }

            R.id.fab_revert -> Toast.makeText(this, "Reverse", Toast.LENGTH_SHORT).show()

            R.id.fab_study -> {
                val estudyIntent = Intent(this, EstudyActivity::class.java)
                startActivity(estudyIntent)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_decks -> {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
            }
            R.id.nav_all_favorites -> {
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

    fun addQuestion(view: View){
        Toast.makeText(this, "Se ha agregado la pregunta", Toast.LENGTH_SHORT).show()
    }

    fun addAnswer(view: View){
        Toast.makeText(this, "Se ha agregado la respuesta", Toast.LENGTH_SHORT).show()
    }

  /*  private fun getCards(): MutableList<Card>{
        val cards: MutableList<Card> = ArrayList()
        cards.add(Card("1", "Pregunta 1", "Respuesta 1"))
        cards.add(Card("2", "Pregunta 2", "Respuesta 2"))
        cards.add(Card("3", "Pregunta 3", "Respuesta 3"))

        return cards
    }*/
}