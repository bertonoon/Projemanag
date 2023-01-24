package eu.tutorials.projemanag.adapters

import android.content.ClipData.Item
import android.content.Context
import android.media.Image
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import eu.tutorials.projemanag.R
import eu.tutorials.projemanag.databinding.ItemBoardBinding
import eu.tutorials.projemanag.models.Board

open class BoardItemsAdapter(

    private val context:Context,
    private val list: ArrayList<Board>)
    :RecyclerView.Adapter<BoardItemsAdapter.BoardViewHolder>() {

    inner class BoardViewHolder(
        private val binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(board: Board){
            binding.tvName.text = board.name
            binding.tvCreatedBy.text = buildString {
                append("Created by: ")
                append(board.createdBy)
            }
            Glide
                .with(context)
                .load(board.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(binding.ivBoardImage)

            binding.llBoard.setOnClickListener{
                Toast.makeText(context,"Board open ${list[0].name}",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}