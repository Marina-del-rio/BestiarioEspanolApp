package com.example.bestiario_espaol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bestiario_espaol.data.Criatura
import com.example.bestiario_espaol.databinding.ItemCriaturaBinding

class CriaturaAdapter(
    private var criaturas: List<Criatura>,
    private val onItemClick: (Criatura) -> Unit
) : RecyclerView.Adapter<CriaturaAdapter.CriaturaViewHolder>() {

    inner class CriaturaViewHolder(val binding: ItemCriaturaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriaturaViewHolder {

        val binding = ItemCriaturaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CriaturaViewHolder(binding)
    }

    override fun getItemCount(): Int = criaturas.size

    override fun onBindViewHolder(holder: CriaturaViewHolder, position: Int) {
        val criatura = criaturas[position]

        holder.binding.textViewNombre.text = criatura.nombre
        holder.binding.textViewTipo.text = criatura.tipo
        holder.binding.textViewDescripcion.text = criatura.descripcion
        holder.binding.textViewComAutonoma.text = criatura.comAutonoma

        holder.binding.textViewDescripcion.visibility = View.GONE
        holder.binding.textViewComAutonoma.visibility = View.GONE

        holder.itemView.setOnClickListener {

            val isVisible = holder.binding.textViewDescripcion.visibility == View.VISIBLE
            holder.binding.textViewDescripcion.visibility = if (isVisible) View.GONE else View.VISIBLE
            holder.binding.textViewComAutonoma.visibility = if (isVisible) View.GONE else View.VISIBLE
            onItemClick(criatura)
        }
    }

    fun updateData(newList: List<Criatura>) {
        criaturas = newList
        notifyDataSetChanged()
    }

    fun getCriaturaAt(position: Int): Criatura {
        return criaturas[position]
    }
}