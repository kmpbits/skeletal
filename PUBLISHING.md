# Publishing Skeletal to Maven Central

Publishing uses [`com.vanniktech.maven.publish`](https://vanniktech.github.io/gradle-maven-publish-plugin/),
targeting the Sonatype Central Portal, under the `io.github.kmpbits`
namespace (already verified via GitHub ownership of the `kmpbits` org).

## One-time setup (per machine)

These live outside the repo — in `~/.gradle/gradle.properties` or as
`ORG_GRADLE_PROJECT_*` environment variables — never in this project's own
`gradle.properties`, which is committed to git.

1. **Sonatype user token** — from your Central Portal account
   (central.sonatype.com → your account → Generate User Token):
   ```properties
   mavenCentralUsername=<token username>
   mavenCentralPassword=<token password>
   ```
2. **GPG signing key** — the plugin signs every publication in-memory from
   an ASCII-armored key, rather than reading a local keyring file:
   ```properties
   signingInMemoryKey=<ASCII-armored private key, with newlines escaped as \n>
   signingInMemoryKeyPassword=<key passphrase>
   ```
   A `.properties` file can't hold real line breaks in a value, so export
   your existing key with each line joined by a literal `\n` (which Gradle
   converts back into real newlines when it reads the property) — **not**
   by stripping newlines entirely, which produces an unparseable key:
   ```bash
   gpg --export-secret-keys --armor <key-id> | awk '{printf "%s\\n", $0}'
   ```

Signing is conditional on `signingInMemoryKey` being present
(`skeletal/build.gradle.kts`), so `./gradlew build` and the test suite work
fine on a machine without these set — only the actual publish step needs
them.

## Releasing manually

1. Verify everything assembles correctly with no credentials needed:
   ```bash
   ./gradlew :skeletal:publishToMavenLocal
   ```
2. Publish to the Central Portal:
   ```bash
   ./gradlew :skeletal:publishToMavenCentral -PVERSION_NAME=<version>
   ```
   (omit `-PVERSION_NAME` to use the `VERSION_NAME` already in the root
   `gradle.properties`)
3. This uploads the release but does **not** auto-release it — go to
   central.sonatype.com, find the deployment, and click "Publish" once
   you've reviewed it there.

## Releasing via GitHub Actions

[`.github/workflows/publish.yml`](.github/workflows/publish.yml) runs
automatically whenever a **GitHub Release is published** on this repo. It
derives the version from the release's tag (e.g. tag `v0.2.0` → published
version `0.2.0`), overwrites the release's notes with the matching
`## [X.Y.Z]` section from [`CHANGELOG.md`](CHANGELOG.md) (whatever you
typed into the Release form is replaced — leave it blank), runs the test
suite, then runs the same `publishToMavenCentral` step as the manual flow
above — including the same manual "Publish" click required afterward on
central.sonatype.com.

Before tagging a release, add a `## [X.Y.Z] - YYYY-MM-DD` section to
`CHANGELOG.md` (move the relevant `[Unreleased]` entries under it). If no
matching section exists for the tag's version, the workflow leaves the
release notes as whatever was already there and continues — it doesn't
fail the release.

The workflow needs four repository secrets, matching the same values as
the local `~/.gradle/gradle.properties` setup above:

| Secret name | Value |
|---|---|
| `MAVEN_CENTRAL_USERNAME` | Sonatype user token username |
| `MAVEN_CENTRAL_PASSWORD` | Sonatype user token password |
| `SIGNING_IN_MEMORY_KEY` | The same `\n`-escaped ASCII-armored key as above |
| `SIGNING_IN_MEMORY_KEY_PASSWORD` | GPG key passphrase |

Add them with the GitHub CLI, reading straight from your own
`~/.gradle/gradle.properties` so the values never appear in your shell
history or an editor buffer:

```bash
gh secret set MAVEN_CENTRAL_USERNAME --repo kmpbits/skeletal \
  --body "$(grep '^mavenCentralUsername=' ~/.gradle/gradle.properties | cut -d= -f2-)"

gh secret set MAVEN_CENTRAL_PASSWORD --repo kmpbits/skeletal \
  --body "$(grep '^mavenCentralPassword=' ~/.gradle/gradle.properties | cut -d= -f2-)"

gh secret set SIGNING_IN_MEMORY_KEY --repo kmpbits/skeletal \
  --body "$(grep '^signingInMemoryKey=' ~/.gradle/gradle.properties | cut -d= -f2-)"

gh secret set SIGNING_IN_MEMORY_KEY_PASSWORD --repo kmpbits/skeletal \
  --body "$(grep '^signingInMemoryKeyPassword=' ~/.gradle/gradle.properties | cut -d= -f2-)"
```

To release: draft and publish a new Release on GitHub (tag it `vX.Y.Z`),
and the workflow picks it up automatically.
