# Toybox

Minecraft tooling a la carte. Use what you need.

**NOTE:** Heavy work in progress. Not tested. Maybe *don't* use it yet.

**NOTE:** The Gradle version catalog is defined in-code inside `settings.gradle`.

## Tour

### toybox-checksum

Small utilities for working with `MessageFormat` objects and formatting hashes as hex strings.

No dependencies. Compatible with Java 8.

### toybox-gdiff

A simple implementation of the Generic Diff algorithm described by [W3C NOTE-GDIFF](https://www.w3.org/TR/NOTE-gdiff-19970825.html). This format is used by Minecraft Forge binpatches. Does not have the capability to create diffs; only applies them.

No dependencies. Compatible with Java 8.

### toybox-manifest-index

Parser for Mojang's `version_manifest_v2.json` file on piston-meta.

The `ManifestIndexParser` class needs Google GSON. Compatible with Java 8.

### toybox-version-manifest

Parser for per-version manifests like `1.20.1.json` on piston-meta.

The `VersionManifestParser` class needs Google GSON. Compatible with Java 8.

## Name

Yes, the name is ripping off [landley/toybox](https://github.com/landley/toybox). I like the name, and later I'd like to provide Minecraft-relevant CLI utilities from this project too, but it's too early for that.

## License

Currently unlicensed. This will change shortly.

Code is a mishmash of original stuff, old stuff from Voldeloom that I wrote, and old stuff from Voldeloom that I borrowed from other places like the original Loom. That will need to get sorted out first.