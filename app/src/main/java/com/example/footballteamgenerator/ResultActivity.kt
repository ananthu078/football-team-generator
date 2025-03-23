package com.example.footballteamgenerator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class ResultActivity : AppCompatActivity() {

    private lateinit var team1: MutableList<Pair<String, Int>>
    private lateinit var team2: MutableList<Pair<String, Int>>
    private lateinit var team1View: TextView
    private lateinit var team2View: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        team1View = findViewById(R.id.team1View)
        team2View = findViewById(R.id.team2View)
        val alternateButton = findViewById<Button>(R.id.alternateButton)

        // Get team data from Intent
        val team1Names = intent.getStringArrayListExtra("team1") ?: arrayListOf()
        val team2Names = intent.getStringArrayListExtra("team2") ?: arrayListOf()
        val team1Ratings = intent.getIntegerArrayListExtra("team1Weights") ?: arrayListOf()
        val team2Ratings = intent.getIntegerArrayListExtra("team2Weights") ?: arrayListOf()

        team1 = team1Names.zip(team1Ratings).toMutableList()
        team2 = team2Names.zip(team2Ratings).toMutableList()

        displayTeams()

        alternateButton.setOnClickListener {
            generateAlternateTeams()
            displayTeams()
        }
    }

    private fun displayTeams() {
        team1View.text = "Team 1:\n" + team1.joinToString("\n") { "${it.first} (Rating: ${it.second})" }
        team2View.text = "Team 2:\n" + team2.joinToString("\n") { "${it.first} (Rating: ${it.second})" }
    }

    private fun generateAlternateTeams() {
        val allPlayers = (team1 + team2).shuffled(Random)
        val sortedPlayers = allPlayers.sortedByDescending { it.second }

        team1.clear()
        team2.clear()
        var team1Weight = 0
        var team2Weight = 0

        for (player in sortedPlayers) {
            if (team1Weight <= team2Weight) {
                team1.add(player)
                team1Weight += player.second
            } else {
                team2.add(player)
                team2Weight += player.second
            }
        }
    }
}
