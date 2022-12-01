package com.educards.activity

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent.DispatcherState
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
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
import com.educards.service.SCard
import com.educards.util.IndexDeckOrCard
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firestore.admin.v1.Index
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeckActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var recyclerCard: RecyclerView
    private lateinit var activity: DeckActivity
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var btReverse: ImageButton

    private lateinit var fabStudy: FloatingActionButton
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView

    var cardsData = ArrayList<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerCard = findViewById(R.id.recycler_cards)
        fabStudy = findViewById(R.id.fab_study)
        fabAdd = findViewById(R.id.fab_add)
        tvDescription = findViewById(R.id.card_description)
        btReverse = findViewById(R.id.fab_revert)

        activity = this
        val bundle = this.intent.extras

        tvDescription.text = bundle?.getString("description")
        title = bundle?.getString("title")

        IndexDeckOrCard.realTimeIndexDeck()
        IndexDeckOrCard.selectedDeckKey = bundle?.getString("idDeck").toString()
        IndexDeckOrCard.realTimeIndexCardInSelectedDeck()

        var horizontalLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecyclerCardAdapter(getCards(), btReverse, this, activity)

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
                    Log.d("DeckActivity", "Error al mostrar las tarjetas del deck seleccionado. ${error}")
                }

            })
        recyclerCard.setHasFixedSize(true)
        recyclerCard.layoutManager = horizontalLayoutManager
        recyclerCard.adapter = adapter
        recyclerCard.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener{
            override fun onChildViewAttachedToWindow(view: View) {
                recyclerCard.getChildAdapterPosition(view)
                    .let { recyclerCard.layoutManager?.scrollToPosition(it) }
                println("vista agregada")
            }
            override fun onChildViewDetachedFromWindow(view: View) {
                recyclerCard.getChildAdapterPosition(view)
                    .let { recyclerCard.layoutManager?.scrollToPosition(it) }
                println("vista eliminada")
            }

        })

        initView()
    }

    private lateinit var dialog: AlertDialog
    private lateinit var etTitle: EditText
    private lateinit var etHeader: TextView
    private lateinit var btUpdate: MaterialButton

    private fun initView() {
        toolbar = findViewById(R.id.toolbar_deck)
        toolbar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_input_deck, null)
            btUpdate = view.findViewById(R.id.bt_create)
            etTitle = view.findViewById(R.id.et_title)
            etHeader = view.findViewById(R.id.tv_header)

            etHeader.text = "Rename deck"
            btUpdate.text = "Update"
            builder.setView(view)

            dialog = builder.create()
            dialog.show()

            btUpdate.setOnClickListener {
//                TODO: Actualizar titulo
                title = etTitle.text
                dialog.hide()
            }
        }
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
        val navHeader = headerView.findViewById<LinearLayout>(R.id.nav_header)
        navHeader.setOnClickListener(this)

        fabStudy.setOnClickListener(this)
        fabAdd.setOnClickListener(this)
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
              if (cardsData.size > 0){
                  val studyIntent = Intent(this, EstudyActivity::class.java)
                  studyIntent.putExtra("title", this.intent.extras?.getString("title"))
                  startActivity(studyIntent)
              }
            }
            R.id.fab_add -> {
                IndexDeckOrCard.realTimeIndexCardInSelectedDeck()
                CoroutineScope(Dispatchers.IO).launch {
                    Thread.sleep(1000)
                    SCard.saveCard(Card("", "Click to edit this question ${cardsData.size+1}", "Click to edit this answer ${cardsData.size+1}"))
                }
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

    private fun getCards(): MutableList<Card>{
        val cards: MutableList<Card> = ArrayList()
        cards.add(Card("1", "Pregunta 1", "Respuesta 1"))
        cards.add(Card("2", "Pregunta 2", "Respuesta 2"))
        cards.add(Card("3", "Pregunta 3", "Respuesta 3"))

        return cards
    }
}
