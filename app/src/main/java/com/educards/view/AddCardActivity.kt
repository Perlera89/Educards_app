package com.educards.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.educards.R

class AddCardActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuReverse -> {
                Toast.makeText(this, "Opcion reverso", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.menuDelete -> {
                Toast.makeText(this, "Opcion eliminar", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.menuSave -> {
                Toast.makeText(this, "Opcion guardar", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.menuCards -> {
                Toast.makeText(this, "Opcion lista de cartas", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}