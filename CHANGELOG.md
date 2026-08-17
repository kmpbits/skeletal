# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [0.3.0]

### Added

- `LoadState<T, F>` — a `Loading`/`Success`/`Failure` sealed state, plus a
  matching `SkeletonContainer(state, onFailure, content)` overload for it.
  Unlike the `isFailure`/`dataOrNull` overload, `state`'s shape is fixed
  to `LoadState` in exchange for `onFailure` receiving a concretely typed
  failure payload with no cast required. `Loading` isn't special-cased by
  the overload — it's whatever `state` isn't `Success` or `Failure`, so
  the shimmer shows automatically with no extra branching needed.
  Demonstrated in the sample app via `LoadStatePostCard`.

### Changed

- `SkeletonContainer(state, dataOrNull, isFailure, onFailure, content)`'s
  `onFailure` slot is now `@Composable (S) -> Unit`, receiving the failure
  state directly instead of requiring callers to re-derive it from the
  outer `state`.

## [0.2.0]

### Added

- `SkeletonContainer(state, dataOrNull, isFailure, onFailure, content)` — a
  state-driven overload for sealed/state-based loading (e.g. a
  `Loading`/`Success`/`Failure` state), deriving `loading` and a typed
  success payload from the caller's own state instead of a plain
  `Boolean`, with a dedicated `onFailure` slot. Demonstrated in the sample
  app via `StateDrivenPostCard`.

## [0.1.0]

### Added

- `SkeletonContainer(loading)` — hosts one shared shimmer animation per subtree.
- `Modifier.skeleton(shape)` — draws a placeholder matching the element's own
  measured bounds while loading, crossfading to real content when it's done.
  Supports `SkeletonShape.Auto` (default), `SkeletonShape.Circle`, and
  `SkeletonShape.RoundedCorner(radius)`.
- `SkeletonDefaults` — corner radius, `MaterialTheme`-derived shimmer colors,
  and crossfade animation defaults, all overridable per `SkeletonContainer`.
- Android, iOS, and Desktop (JVM) targets.

[0.1.0]: https://github.com/kmpbits/skeletal/releases/tag/v0.1.0
