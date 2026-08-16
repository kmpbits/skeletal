# Skeletal

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kmpbits/skeletal)](https://central.sonatype.com/artifact/io.github.kmpbits/skeletal)
[![Tests](https://github.com/kmpbits/skeletal/actions/workflows/test.yml/badge.svg)](https://github.com/kmpbits/skeletal/actions/workflows/test.yml)
[![License](https://img.shields.io/github/license/kmpbits/skeletal)](LICENSE)

Automatic loading skeletons for Compose Multiplatform. Wrap your existing
composables — no parallel skeleton UI to build or maintain.

```kotlin
SkeletonContainer(loading = post == null) {
    Card {
        Row {
            Image(
                painter = rememberImagePainter(post?.avatarUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .skeleton(shape = SkeletonShape.Circle)
            )

            Column {
                Text(
                    text = post?.title ?: "",
                    modifier = Modifier.fillMaxWidth(0.6f).skeleton()
                )
                Text(
                    text = post?.subtitle ?: "",
                    modifier = Modifier.fillMaxWidth(0.4f).skeleton()
                )
            }
        }
    }
}
```

While `loading` is `true`, every `.skeleton()` element draws a shimmering
placeholder sized to its own measured bounds instead of its real content.
When `loading` flips to `false`, it crossfades into the real content. One
shared shimmer animation runs per `SkeletonContainer`, so a whole screen of
placeholders animates in sync instead of paying for one animation driver
per element.

**Platforms:** Android, iOS, Desktop (JVM).

## Install

Available on [Maven Central](https://central.sonatype.com/artifact/io.github.kmpbits/skeletal):

```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.kmpbits:skeletal:0.1.0")
}
```

See [`PUBLISHING.md`](PUBLISHING.md) for how new versions are released.

## API

```kotlin
@Composable
fun SkeletonContainer(
    loading: Boolean,
    modifier: Modifier = Modifier,
    shimmerColors: List<Color> = SkeletonDefaults.shimmerColors, // MaterialTheme-derived by default
    cornerRadius: Dp = SkeletonDefaults.cornerRadius,             // 4.dp by default
    content: @Composable () -> Unit,
)

fun Modifier.skeleton(
    shape: SkeletonShape = SkeletonShape.Auto, // own bounds, rounded rect
    // also: SkeletonShape.Circle, SkeletonShape.RoundedCorner(radius)
): Modifier
```

`Modifier.skeleton()` with no ancestor `SkeletonContainer` is a no-op, so
it's safe to leave on an element regardless of whether it's currently
inside a loading context.

### State-driven loading

For state modeled as a sealed class instead of a plain `Boolean`, a second
overload takes the state directly plus two small extractor lambdas — the
success payload flows into `content` already typed, and failures get their
own dedicated slot:

```kotlin
sealed interface Loadable<out T> {
    data object Loading : Loadable<Nothing>
    data class Loaded<T>(val value: T) : Loadable<T>
    data class Failed(val error: Throwable) : Loadable<Nothing>
}

SkeletonContainer(
    state = state, // Loadable<Post>
    dataOrNull = { (it as? Loadable.Loaded)?.value },
    isFailure = { it is Loadable.Failed },
    onFailure = { Text("Something went wrong") },
) { post ->
    Card {
        Text(
            text = post?.title ?: "",
            modifier = Modifier.fillMaxWidth(0.6f).skeleton()
        )
    }
}
```

`onFailure` has no default — a caller reaching for this overload already
has a failure case to handle. Callers without one should keep using the
plain `loading: Boolean` overload above.

## Sample

An Android sample app lives in [`sample/`](sample) — a scrollable feed of
cards exercising all three `SkeletonShape` variants, with a "Reload" button
to re-trigger the loading state. Run it from Android Studio, or:

```bash
./gradlew :sample:assembleDebug
```

## Development

```bash
./gradlew :skeletal:desktopTest   # run the test suite
./gradlew :skeletal:build         # build the library for all targets
```
