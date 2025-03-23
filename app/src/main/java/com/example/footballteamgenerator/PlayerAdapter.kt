package com.example.footballteamgenerator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.footballteamgenerator.Player
import com.example.footballteamgenerator.R

class PlayerAdapter(
    private val players: MutableList<Player>,
    private val onRemove: (Int) -> Unit,  // ✅ Add this
    private val onEdit: (Int, String, Int) -> Unit // ✅ Add this
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerName: TextView = itemView.findViewById(R.id.playerName)
        val playerRating: TextView = itemView.findViewById(R.id.playerRating)
        val editName: EditText = itemView.findViewById(R.id.editPlayerName)
        val editRating: EditText = itemView.findViewById(R.id.editPlayerRating)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val removeButton: Button = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = players[position]
        holder.playerName.text = player.name
        holder.playerRating.text = "Rating: ${player.rating}"

        // Set Remove Click Listener
        holder.removeButton.setOnClickListener {
            onRemove(position)  // ✅ Call remove function
        }

        // Set Edit Click Listener
        holder.editButton.setOnClickListener {
            val newName = holder.editName.text.toString().trim()
            val newRating = holder.editRating.text.toString().toIntOrNull() ?: player.rating
            if (newName.isNotEmpty()) {
                onEdit(position, newName, newRating)  // ✅ Call edit function
            }
        }
    }

    override fun getItemCount(): Int = players.size
}
