Upgrade plan: Move project to Java 21 (latest LTS)

Goal
- Update the project to compile and run with Java 21, update Gradle/JDK toolchain settings, and ensure Android Gradle Plugin (AGP) and Kotlin versions are compatible.

Summary of current state
- AGP: 8.13.0 (from gradle/libs.versions.toml)
- Kotlin: 2.0.21
- Gradle wrapper: 8.13
- Module currently targeted to Java 11 (updated to Java 21 in `app/build.gradle.kts` by this plan)

Manual upgrade steps

1) Install JDK 21 on Windows
- Recommended: Eclipse Temurin (Adoptium) or Microsoft Build of OpenJDK.
- Example (PowerShell):
  - Use a package manager like winget: `winget install -e --id Eclipse.Adoptium.Temurin.21JDK` 
  - Or download and install from https://adoptium.net

2) Point Gradle to the JDK (optional but recommended for reproducible builds)
- Edit `gradle.properties` and add or update:
  - `org.gradle.java.home=C:\\Path\\To\\jdk-21`
- Alternatively, configure Gradle toolchains in `settings.gradle.kts` or module build files.

3) Ensure AGP and Kotlin versions are compatible
- Current AGP 8.13.0 and Kotlin 2.0.21 are recent. Keep them or upgrade if you encounter tooling issues.
- If you upgrade Kotlin or AGP, also update the version catalog `gradle/libs.versions.toml` accordingly.

4) Update module Java/Kotlin targets
- `app/build.gradle.kts` now uses Java 21 and `kotlinOptions.jvmTarget = "21"`.
- If you use Java toolchains, configure them in `build.gradle.kts`:
  - `java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }`

5) Build and run
- From PowerShell in project root:
  - `./gradlew clean assembleDebug` (on Windows use `gradlew.bat` if issues)
- If Gradle fails due to JDK mismatch, set `org.gradle.java.home` or configure toolchains as above.

6) Fix compile or runtime issues
- Common problems:
  - Third-party libraries requiring newer language features: update their versions.
  - Kotlin compatibility: update `kotlin` plugin or adjust `jvmTarget`.

7) CI and developer machines
- Update CI images to include JDK 21 or set `org.gradle.java.home` in CI config.

Notes and rollback
- Keep a branch for the upgrade work. If issues arise, revert to previous Java target and diagnose.
- Consider running the Android emulator or instrumentation tests that depend on API levels.

What I changed now
- Updated `app/build.gradle.kts` to use Java 21 and set Kotlin JVM target to 21.
- Created this `JAVA_UPGRADE_PLAN.md` with manual upgrade steps because automated Copilot upgrade tools are not available on your plan.

Next steps I can take (if you want):
- Configure Gradle toolchains explicitly in `build.gradle.kts`.
- Add `org.gradle.java.home` to `gradle.properties` if you provide the JDK install path or want me to install JDK 21 locally (note: automated JDK installer tool is restricted by plan).
- Run the Gradle build and fix compiler errors (I can run `./gradlew assembleDebug` here if you want me to try and report issues).
