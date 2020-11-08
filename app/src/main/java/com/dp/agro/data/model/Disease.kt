package com.dp.agro.data.model

data class Disease(
    val map: Map<String, Any?>
) {
    val name: String =
        if (map.containsKey("name")) (map["name"] as String) else ""
    val imageUrl: String =
        if (map.containsKey("imageUrl")) (map["imageUrl"] as String) else ""
    val thumbnailUrl: String =
        if (map.containsKey("thumbnailUrl")) (map["thumbnailUrl"] as String) else ""
    val description: String =
        if (map.containsKey("description")) (map["description"] as String) else ""
    val symptoms: String =
        if (map.containsKey("symptoms")) (map["symptoms"] as String) else ""
    val treatments: String =
        if (map.containsKey("treatments")) (map["treatments"] as String) else ""
    val source: String =
        if (map.containsKey("source")) (map["source"] as String) else ""
    val threatLevel: String =
        if (map.containsKey("threatLevel")) (map["threatLevel"] as String) else ""
    val isAirInfected: Boolean =
        if (map.containsKey("isAirInfected")) (map["isAirInfected"] as Boolean) else false
}