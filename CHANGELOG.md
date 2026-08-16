# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

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
