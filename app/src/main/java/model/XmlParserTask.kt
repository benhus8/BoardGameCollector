package model

import android.os.AsyncTask
import android.util.Log
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class XmlParserTask : AsyncTask<String, Void, List<BoardGame>>() {

    override fun doInBackground(vararg urls: String?): List<BoardGame> {
        val url = URL(urls[0])
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.readTimeout = 10000
        connection.connectTimeout = 15000
        connection.requestMethod = "GET"
        connection.doInput = true

        try {
            connection.connect()
            return parseXml(connection.inputStream)
        } catch (e: Exception) {
            Log.e("model.XmlParserTask", "Error during parsing", e)
        } finally {
            connection.disconnect()
        }
        return emptyList()
    }

    private fun parseXml(inputStream: InputStream): List<BoardGame> {
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(inputStream)
        doc.documentElement.normalize()

        val itemList = doc.getElementsByTagName("item")
        val boardGames = mutableListOf<BoardGame>()
        for (i in 0 until itemList.length) {
            val itemNode = itemList.item(i)

            if (itemNode.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                val elem = itemNode as org.w3c.dom.Element
                val boardGame = BoardGame(
                    elem.getAttribute("objectid").toInt().toString(),
                    elem.getElementsByTagName("name").item(0).textContent,
                    elem.getElementsByTagName("yearpublished").item(0)?.textContent?.toInt() ?: 0
                )
                boardGames.add(boardGame)
            }
        }
        return boardGames
    }


}