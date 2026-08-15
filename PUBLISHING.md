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
   signingInMemoryKey=<ASCII-armored private key>
   signingInMemoryKeyPassword=<key passphrase>
   ```
   Export your existing key with:
   ```bash
   gpg --export-secret-keys --armor <key-id> | tr -d '\n' 
   ```

Signing is conditional on `signingInMemoryKey` being present
(`skeletal/build.gradle.kts`), so `./gradlew build` and the test suite work
fine on a machine without these set — only the actual publish step needs
them.

## Releasing

1. Bump `VERSION_NAME` in the root `gradle.properties`.
2. Verify everything assembles correctly with no credentials needed:
   ```bash
   ./gradlew :skeletal:publishToMavenLocal
   ```
3. Publish to the Central Portal:
   ```bash
   ./gradlew :skeletal:publishToMavenCentral
   ```
4. This uploads the release but does **not** auto-release it — go to
   central.sonatype.com, find the deployment, and click "Publish" once
   you've reviewed it there.
