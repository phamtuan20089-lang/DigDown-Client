# DigDown Client (combined source workspace)

This workspace packages both uploaded Minecraft Fabric 1.21.11 client sources under one distribution name, **DigDown Client**.

## Why this is a two-mod bundle, not a single merged JAR
The projects use different Minecraft mappings (67Client uses Yarn mappings; Anubis uses Mojang official mappings), different Loom versions, and separate entrypoints/mixin configurations. Combining them into one compilation unit would require a full source remap and integration pass. This workspace keeps each project build isolated so both feature sets can run together in one Fabric instance without package/mapping collisions.

## Included projects
- `67client/67client` — 67Client v2.0.3, display name changed to DigDown Client (67 Modules).
- `Anubis/anubis-1.0-src` — Anubis 1.0, display name changed to DigDown Client (Anubis Modules).

Both target Minecraft 1.21.11 and Java 21. Keep the original mod IDs and internal packages to preserve configs and mixin identifiers.

## Build
Build each mod separately from its project folder:

```sh
cd 67client/67client
./gradlew build

cd ../../Anubis/anubis-1.0-src
./gradlew build
```

Built JARs are in each project's `build/libs/`. Put both JARs into the same Minecraft Fabric `mods` folder with the matching Fabric Loader/API versions.

## Windows
Use `build-all.bat` from this directory.\n

## Notes
- This is a source bundle with display-name branding; it is not a single merged Fabric mod JAR.
- The builds have not been verified in this environment. Build locally and inspect Gradle errors if any occur.
- Both original project licenses and source notices are retained in their project folders.

## Integration / audit caveat
The 67Client source contains process-launch, class-loading, environment-check, and HTTP-related code paths (for example, Spotify integration and protection checks). Those areas were retained as source from the upload and were not independently audited line-by-line. Review them before running the client, especially if you use a third-party server or environment.
