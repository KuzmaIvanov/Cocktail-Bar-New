package com.example.cocktailbarnew.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cocktailbarnew.R
import com.example.cocktailbarnew.databinding.ItemCocktailBinding
import com.example.cocktailbarnew.domain.model.Cocktail

class CocktailItemAdapter(
    var cocktails: List<Cocktail>,
    val onItemClickAction: (Long) -> Unit
) : RecyclerView.Adapter<CocktailItemAdapter.CocktailItemViewHolder>(), View.OnClickListener {

    class CocktailItemViewHolder(
        val binding: ItemCocktailBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCocktailBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return CocktailItemViewHolder(binding)
    }

    override fun getItemCount(): Int = cocktails.size

    override fun onBindViewHolder(holder: CocktailItemViewHolder, position: Int) {
        val cocktail = cocktails[position]
        with(holder.binding) {
            holder.itemView.tag = cocktail
            cocktailNameTextView.text = cocktail.name
            cocktailImageView.load(cocktail.image ?: R.drawable.cocktail_placeholder)
        }
    }

    override fun onClick(p0: View) {
        onItemClickAction((p0.tag as Cocktail).id)
    }
}