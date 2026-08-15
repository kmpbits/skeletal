# Skeletal

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

This library isn't published to Maven Central yet, so there's no
`implementation("com.kmpbits:skeletal:...")` coordinate to depend on. Until
it is, pull it in as a Gradle composite build:

1. Clone this repo somewhere alongside your project:
   ```bash
   git clone https://github.com/kmpbits/skeletal.git
   ```
2. In your project's `settings.gradle.kts`, add:
   ```kotlin
   includeBuild("../skeletal")
   ```
   (adjust the relative path to wherever you cloned it)
3. Depend on it from your module's `build.gradle.kts` as usual:
   ```kotlin
   dependencies {
       implementation("com.kmpbits:skeletal")
   }
   ```

Once this is published to Maven Central, installation will just be:

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.kmpbits:skeletal:<version>")
}
```

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
