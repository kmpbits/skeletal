package com.kmpbits.skeletal.sample

data class SamplePost(
    val title: String,
    val subtitle: String,
)

val samplePosts = listOf(
    SamplePost("Morning run recap", "6.2 km around the harbour"),
    SamplePost("New KMP release", "Compose Multiplatform 1.10 notes"),
    SamplePost("Weekend hike", "Views from the ridge trail"),
    SamplePost("Coffee experiment", "Trying a slower pour-over ratio"),
    SamplePost("Reading list", "Three books on distributed systems"),
    SamplePost("Studio update", "New desk setup, same old bugs"),
    SamplePost("Garden progress", "Tomatoes finally turning red"),
    SamplePost("Conference talk", "Slides from the KMP meetup"),
    SamplePost("Bike maintenance", "Chain replaced, brakes bled"),
    SamplePost("Late night build", "Fixed the flaky CI job at last"),
)
