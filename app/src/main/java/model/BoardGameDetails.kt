package model

data class BoardGameDetails(
    val gameID: String,
    val thumbnail: String,
    val image: String,
    val name: String,
    val description: String,
    val yearPublished: String,
    val minPlayers: String,
    val maxPlayers: String,
    val playingTime: String,
    val minAge: String,
    val rank: String
)