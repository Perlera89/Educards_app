package com.educards.activity

import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.adapter.RecyclerCardAdapter
import com.educards.model.Deck
import com.educards.model.entities.Card
import com.educards.service.FirebaseConnection
import com.educards.service.SCard
import com.educards.service.SDeck
import com.educards.util.IndexDeckOrCard
import com.educards.util.Listeners
import com.educards.util.UTextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
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
    private lateinit var bundle: Bundle
    lateinit var cardListener:ValueEventListener

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
        bundle = this.intent.extras!!

        IndexDeckOrCard.realTimeIndexDeck()
        IndexDeckOrCard.selectedDeckKey = bundle?.getString("idDeck").toString()
        IndexDeckOrCard.realTimeIndexCardInSelectedDeck()

        tvDescription.text = bundle?.getString("description")
        title = bundle?.getString("title")

        var horizontalLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecyclerCardAdapter(cardsData, btReverse, this, activity)

        cardListener = object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    cardsData.clear()
                    snapshot.children.forEach{
                        if (it != null){
                            cardsData.add(it.getValue<Card>() as Card)
                        }
                    }
                    recyclerCard.adapter = adapter
                    Listeners.cardListener = cardListener
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("DeckActivity", "Error al mostrar las tarjetas del deck seleccionado. ${error}")
                }

            }
        FirebaseConnection.refGlobal.child("/cards").child("${bundle?.getString("idDeck").toString()}").addValueEventListener(cardListener)
        recyclerCard.setHasFixedSize(true)
        recyclerCard.layoutManager = horizontalLayoutManager
        recyclerCard.adapter = adapter
        recyclerCard.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener{
            override fun onChildViewAttachedToWindow(view: View) {
                recyclerCard.getChildAdapterPosition(view)
                    .let { recyclerCard.layoutManager?.scrollToPosition(it) }
            }
            override fun onChildViewDetachedFromWindow(view: View) {
                recyclerCard.getChildAdapterPosition(view)
                    .let { recyclerCard.layoutManager?.scrollToPosition(it) }
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
            val view = layoutInflater.inflate(R.layout.dialog_input, null)
            btUpdate = view.findViewById(R.id.bt_create)
            etTitle = view.findViewById(R.id.et_title)
            etHeader = view.findViewById(R.id.tv_header)

            etHeader.text = "Rename deck"
            btUpdate.text = "Update"
            etTitle.setText(title.toString())
            builder.setView(view)

            dialog = builder.create()
            dialog.show()

            btUpdate.setOnClickListener {
                if (UTextView.verifyContentInTextViews(this,etTitle,"Campo de título del mazo nulo o vacío")){
                    SDeck.updateDeck(Deck(
                        bundle.getString("idDeck").toString(),
                        etTitle.text.toString().replaceFirstChar { it.uppercase() },
                        bundle.getString("description").toString(),
                        bundle.getString("isFavorite").toString().toBoolean(),
                        bundle.getInt("count")
                    ))
                    title = etTitle.text.toString().replaceFirstChar { it.uppercase() }
                    Toast.makeText(this, "Titulo del mazo actualizado exitosamente", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
            }
        }
        tvDescription.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_input, null)
            btUpdate = view.findViewById(R.id.bt_create)
            etTitle = view.findViewById(R.id.et_title)
            etHeader = view.findViewById(R.id.tv_header)

            etHeader.text = "Update description"
            btUpdate.text = "Update"
            //contenido del input
            etTitle.setText(tvDescription.text)
            builder.setView(view)

            dialog = builder.create()
            dialog.show()

            btUpdate.setOnClickListener {
                if (UTextView.verifyContentInTextViews(this,etTitle,"Campo de descripción del mazo nulo o vacío")){
                    SDeck.updateDeck(Deck(
                        bundle.getString("idDeck").toString(),
                        title.toString(),
                        etTitle.text.toString(),
                        bundle.getString("isFavorite").toString().toBoolean(),
                        bundle.getInt("count")
                    ))
                    tvDescription.setText(etTitle.text)
                    Toast.makeText(this, "Titulo del mazo actualizado exitosamente", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
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
                this.finish()
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

    /*private fun getCards(): MutableList<Card>{
        val cards: MutableList<Card> = ArrayList()
        cards.add(Card("1", "Pregunta 1", "Respuesta 1"))
        cards.add(Card("2", "Pregunta 2", "Respuesta 2"))
        cards.add(Card("3", "Pregunta 3", "Respuesta 3"))

        return cards
    }*/
}
