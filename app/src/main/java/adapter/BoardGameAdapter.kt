package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inf151313.boardgamecollector.R
import com.squareup.picasso.Picasso
import database.BoardGameDataSource
import enums.Type
import model.BoardGame
import java.io.File
import android.graphics.BitmapFactory

class BoardGameAdapter(private val context: Context, private val boardGames: List<BoardGame>, private val type: Type) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1
    private var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(boardGameId: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == VIEW_TYPE_HEADER) {
            val headerView:View = if(type.equals(Type.BOARDGAME)) {
                inflater.inflate(R.layout.header_board_games, parent, false)
            } else {
                inflater.inflate(R.layout.header_expansions, parent, false)
            }
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
                val imageFileForThumbnail = dataSource.getImageFilesByGameId(boardGame.id)
                if(imageFileForThumbnail.isNotEmpty()) {
                    val file = File(context.getExternalFilesDir(null), "images/${imageFileForThumbnail[0]}")
                    if (file.exists()) {
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        imageThumbnail.setImageBitmap(bitmap)
                    }
                } else {
                    thumbnailUrl = dataSource.getThumbnailByGameId(boardGame.id).toString()
                    Picasso.get().load(thumbnailUrl).into(imageThumbnail)
                }
            } else {
                thumbnailUrl = dataSource.getThumbnailByExpansionId(boardGame.id).toString()
                Picasso.get().load(thumbnailUrl).into(imageThumbnail)
            }



            itemView.setOnClickListener {
                val boardGameId = boardGame.id
                onItemClickListener?.onItemClick(boardGameId)
            }
        }
    }
}