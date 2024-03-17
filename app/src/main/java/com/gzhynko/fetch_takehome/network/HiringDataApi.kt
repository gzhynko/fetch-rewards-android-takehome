package com.gzhynko.fetch_takehome.network

import com.gzhynko.fetch_takehome.model.HiringDataEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URL
import java.util.*

private const val JSON_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

interface HiringDataApiService {
    /**
     * Makes a request to the Fetch API to get the JSON file and returns parsed data.
     *
     * @return A SortedMap where the keys are the listID's and the values are HiringDataEntries
     * sorted by name (null and blank names are excluded).
     */
    suspend fun getData(): SortedMap<Int, List<HiringDataEntry>>
}

/**
 * An implementation of the API service using java.net's URL() as a way to retrieve remote data.
 */
class HiringDataApi : HiringDataApiService {
    override suspend fun getData(): SortedMap<Int, List<HiringDataEntry>> =
        withContext(Dispatchers.IO) {
            val remoteJson = URL(JSON_URL).readText();
            val data = Json.decodeFromString<List<HiringDataEntry>>(remoteJson)
            val sortedData = sortData(data)

            sortedData.groupBy { it.listId }.toSortedMap()
        }

    /**
     * Sorts the data according to the assignment specs.
     */
    private fun sortData(data: List<HiringDataEntry>): List<HiringDataEntry> =
        data.filter { e -> !e.name.isNullOrBlank() }
            // here we sort by the integer value within the name, not in alphabetic order
            .sortedBy { e ->
                val nameSplit = e.name?.split(" ")
                if (nameSplit == null || nameSplit.size != 2)
                    Int.MAX_VALUE // push all names of unexpected format towards the end
                else
                    nameSplit[1].toInt() // if the format is good, sort by the int contained within the name
            }
}
