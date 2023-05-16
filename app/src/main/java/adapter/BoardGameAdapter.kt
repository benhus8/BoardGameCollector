package adapter

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inf151313.boardgamecollector.R
import com.squareup.picasso.Picasso
import database.BoardGameDataSource
import enums.Type
import model.BoardGame

class BoardGameAdapter(private val context: Context, private val boardGames: List<BoardGame>, private val type: Type) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == VIEW_TYPE_HEADER) {
            val headerView = inflater.inflate(R.layout.header_board_games, parent, false)
            HeaderViewHolder(headerView)
        } else {
            val itemView = inflater.inflate(R.layout.item_board_game, parent, false)
            ItemViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
        } else if (holder is ItemViewHolder) {
            val itemPosition = position - 1 // Odejmujemy 1, ponieważ pierwsza pozycja to nagłówek
            val boardGame = boardGames[itemPosition]
            holder.bind(boardGame)
        }
    }

    override fun getItemCount(): Int {
        // Liczba elementów to liczba gier planszowych plus 1 (nagłówek)
        return boardGames.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textOrder: TextView = itemView.findViewById(R.id.textOrder)
        private val imageThumbnail: ImageView = itemView.findViewById(R.id.imageThumbnail)
        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        private val textYearPublished: TextView = itemView.findViewById(R.id.textYearPublished)

        fun bind(boardGame: BoardGame) {
            textOrder.text = null
            textTitle.text = null
            textYearPublished.text = null
            imageThumbnail.setImageDrawable(null)

            val itemPosition = adapterPosition - 1 // Odejmujemy 1, ponieważ pierwsza pozycja to nagłówek
            textOrder.text = (itemPosition + 1).toString() // Wyświetlamy liczbę porządkową
            textTitle.text = boardGame.title
            textYearPublished.text ="Publish Year: "+  boardGame.yearPublished.toString()


            // Zaciąganie miniaturki na podstawie linku
            val dataSource = BoardGameDataSource(context)
            var thumbnailUrl: String = ""
            if(type.equals(Type.BOARDGAME)) {
                thumbnailUrl = dataSource.getThumbnailByGameId(boardGame.id).toString()
            } else {
                thumbnailUrl = dataSource.getThumbnailByExpansionId(boardGame.id).toString()
            }
            Picasso.get().load(thumbnailUrl).into(imageThumbnail)
        }
    }
}