package com.example.footballteamgenerator

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.footballteamgenerator.adapter.PlayerAdapter


class MainActivity : AppCompatActivity() {

    private val players = mutableListOf<Player>()
    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("players_prefs", MODE_PRIVATE)

        val playerNameInput = findViewById<EditText>(R.id.playerNameInput)
        val playerRatingSpinner = findViewById<Spinner>(R.id.playerRatingSpinner)
        val addPlayerButton = findViewById<Button>(R.id.addPlayerButton)
        val savePlayersButton = findViewById<Button>(R.id.savePlayersButton)
        val loadPlayersButton = findViewById<Button>(R.id.loadPlayersButton)
        val generateTeamsButton = findViewById<Button>(R.id.generateTeamsButton)
        recyclerView = findViewById(R.id.playersRecyclerView)

        // ✅ Initialize the rating spinner with values from 1 to 10
        val ratings = (1..10).map { it.toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ratings)
        playerRatingSpinner.adapter = adapter

        // ✅ Initialize PlayerAdapter with Remove & Edit Functions
        playerAdapter = PlayerAdapter(players,
            onRemove = { index ->
                players.removeAt(index)
                playerAdapter.notifyDataSetChanged()
            },
            onEdit = { index, newName, newRating ->
                players[index].name = newName
                players[index].rating = newRating
                playerAdapter.notifyDataSetChanged()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = playerAdapter

        // ✅ Add player to the list
        addPlayerButton.setOnClickListener {
            val name = playerNameInput.text.toString().trim()
            val rating = playerRatingSpinner.selectedItem.toString().toInt()

            if (name.isNotEmpty() && players.none { it.name == name }) {
                players.add(Player(name, rating))
                playerAdapter.notifyDataSetChanged()
                playerNameInput.text.clear()
            } else {
                Toast.makeText(this, "Invalid or duplicate name", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Save players to SharedPreferences
        savePlayersButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            val json = gson.toJson(players)
            editor.putString("saved_players", json)
            editor.apply()
            Toast.makeText(this, "Players saved", Toast.LENGTH_SHORT).show()
        }

        // ✅ Load players from SharedPreferences
        loadPlayersButton.setOnClickListener {
            val json = sharedPreferences.getString("saved_players", null)
            if (json != null) {
                val type = object : TypeToken<List<Player>>() {}.type
                val savedPlayers = gson.fromJson<List<Player>>(json, type)
                players.clear()
                players.addAll(savedPlayers)
                playerAdapter.notifyDataSetChanged()
            }
        }

        // ✅ Generate teams and go to new activity
        generateTeamsButton.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("players_list", gson.toJson(players))
            startActivity(intent)
        }
    }
}
