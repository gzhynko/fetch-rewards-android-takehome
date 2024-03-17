package com.gzhynko.fetch_takehome.model

import kotlinx.serialization.Serializable

/**
 * A model of an entry from the JSON data retrieved from Fetch's API.
 */
@Serializable
data class HiringDataEntry(val id: Int, val listId: Int, val name: String?)
