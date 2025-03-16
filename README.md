# Toybox

Minecraft tooling a la carte. Use what you need.

**NOTE:** Heavy work in progress. Not tested. Maybe *don't* use it yet.

Also an excuse to try out [mill](https://mill-build.org).

* Test with `./mill __.test`. Work with a specific module with, say, `./mill toybox-string.test`.
* Create jars with `./mill __.jar`. They go in `out/<module name>/jar.dest/`.
* Create fatjars with `./mill __.assembly`; artifacts go in `out/<subproject name>/assembly.dest/`.

## TODO

* better MCP api
  * don't tie the format parsers to their representation
  * possibly a new module containing definitions for class/field/method/package mappings, and MCP is just a module depending on it?
  * that way i can reuse those definitions in other mapping formats
* more tests lol

There is also fabric mappingio, which [has a cool visitor api](https://github.com/FabricMC/mapping-io/blob/dev/src/main/java/net/fabricmc/mappingio/MappingVisitor.java) and [supports a lot of formats](https://github.com/FabricMC/mapping-io/blob/dev/src/main/java/net/fabricmc/mappingio/format/MappingFormat.java) but is geared towrads modern MCP obviously, so it doesn't support package mappings or fields.csv/methods.csv

## Tour

### toybox-checksum

Small utilities for working with `MessageFormat` objects and formatting hashes as hex strings.

No dependencies. Compatible with Java 8.

Might get removed/merged into other stuff, we'll see

### toybox-gdiff

A simple implementation of the Generic Diff algorithm described by [W3C NOTE-GDIFF](https://www.w3.org/TR/NOTE-gdiff-19970825.html). This format is used by Minecraft Forge binpatches. Does not have the capability to create diffs; only applies them.

No dependencies. Compatible with Java 8.

### toybox-manifest-index

Parser for Mojang's `version_manifest_v2.json` file on piston-meta.

The `ManifestIndexParser` class needs Google GSON. Compatible with Java 8.

### toybox-mcp

Utilities for MCP format mappings

### toybox-string

Deduplication utilities for strings, used throughout the mapping parsers to reduce memory usage

### toybox-version-manifest

Parser for per-version manifests like `1.20.1.json` on piston-meta.

The `VersionManifestParser` class needs Google GSON. Compatible with Java 8.

## Name

Yes, the name is ripping off [landley/toybox](https://github.com/landley/toybox).

## License

Currently unlicensed. This will change.

Code is a mishmash of original stuff, old stuff from Voldeloom that I wrote, and old stuff from Voldeloom that I borrowed from other places like the original Loom. That will need to get sorted out first, i'll need to include relevant licenses in each jar...