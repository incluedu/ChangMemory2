# Changelog - ChangMemory II

All notable changes and version milestones of this project will be documented in this file. This project is based on the historical Java predecessor *ChangMemory* (2015).

---

## [2.0.0-alpha.1] - In Development (Unreleased)
### Added
* `[+]` Complete porting of the entire game source code from Java to **Kotlin**.
* `[+]` Modernized the build system to the current **Gradle** multi-module structure.
* `[+]` Upgraded the desktop launcher to **LWJGL3** for perfect modern PC compatibility.
* `[+]` Future-proofed `AndroidManifest.xml` with modern `dataExtractionRules` for Android 12 up to Android 16+.
* `[+]` Migrated all asset and level files to the central `assets/` directory for robust cross-platform loading.
* `[+]` Migrated core build scripts from Groovy (`build.gradle`) to the modern, type-safe **Kotlin DSL (`build.gradle.kts`)**.
* `[+]` Integrated **LibKTX** modules (`ktx-actors` and `ktx-scene2d`) to eliminate legacy Java boilerplate code.
* `[+]` Implemented an inline lambda factory `invoke` operator inside `CommandListener` to completely eliminate Java boilerplate (`object : CommandListener()`) in favor of flat Kotlin syntax.
* `[+]` Added comprehensive, professional English KDoc documentation to `CommandListener`, `WindowPause`, `WindowGameOver`, and their respective screen wrapper methods.
* `[+]` Refactored interactive game buttons (`BtnBack`, Google Play services UI) into minimalist, crash-safe KTX **`onClick`** lambda listeners.
* `[+]` Cleaned up and modularized complex layout containers (`WindowGameOver`, `WindowPause`) using specialized Kotlin scope functions (`.apply`) and localized string constants.
* `[+]` Fully overhauled the global asset management (`Assets.kt` & `AssetCard.kt`), implementing type-safe generic resource loading (`manager.load<T>`), automated array disposers (`cardAssetList.dispose`), and central companion object configurations for all game sounds.
* `[+]` Integrated **ktx-log** inline lambdas to eliminate legacy `Gdx.app.debug` string allocation overhead and companion object tags.

### Changed
* `[c]` Rebranded the project from *ChangMemory* to **ChangMemory II**.
* `[c]` Drastically increased thread safety and stability across all screens using Kotlin's null-safety features (`lateinit var`, `?.let`, smart casts).
* `[c]` Radically downsized `CardScreen` by decoupling UI event handling and routing click actions directly into `WindowPause` and `WindowGameOver`.
* `[c]` Refactored core gameplay buttons inside `CardScreen` into type-safe KTX **`onClick`** lambda listeners.

### Fixed
* `[f]` Fixed deep Z-index rendering bugs where the interactive pause button was obscured by background image swaps during difficulty increments.
* `[f]` Fixed a hidden state freeze where exiting a paused game left flags active, causing subsequent matches to lock instantly.
* `[f]` Resolved critical input crashes by synchronizing `stage.clear()` execution order to fire strictly before fresh asset deployment.
* `[f]` Fixed a game statistics bug where the final count of solved cards incorrectly displayed as `0` on the Game Over screen due to a premature variable reset during level advancements. This was resolved by tracking a new persistent `totalCardSetSolvedCount` in the `GameController`.
* `[c]` Consolidated state reset routines by removing redundant `prepareManualRestart` hooks in favor of atomic, unified variable clearing.

---

## History - ChangMemory (Java Era)

### Version 1.0.5
* `[a]` Add new leaderboards for highest level, most cards flipped, most cards solved and most lucky strike in a single game.

### Version 1.0.4
* `[c]` Hidden achievements should now work correctly.
* `[c]` Now show score in card-screen HUD display.
* `[c]` Time add in card-screen HUD display only shows when 1 or more seconds left.
* `[c]` Cards in card-screen flipping now faster.
* `[f]` Hopefully fix a bug that sometimes flips a single card back after being resolved.

### Version 1.0.3
* `[c]` Change Android Target SDK to Android 5.1.1.
* `[f]` GameTime in `ScoreScreen` was not saved correctly.
* `[a]` Add a couple of new achievements! `:-)`

### Version 1.0.2
* `[c]` Show version number in credits screen.
* `[f]` Back button now works in credits screen.

### Version 1.0.1
* `[f]` Fix points achievement not working when aborting the game.
* `[c]` Change the handling of the back button in `GameOverWindow` and `GamePauseWindow` to go back to MenuScreen when pressing the button in one of these states.
* `[c]` Button leaderboards and button achievements only visible when signed in to Google Plus.

### Version 1.0.0.034 *(25-Apr-2015)*
* `[c]` First release.

---

## Alpha Phases (Archive)

### Version 0.3.1.0033 alpha *(25-Apr-2015 / Build 33)*
* `[+]` Add counter for cards flipped, cards solved and lucky strike to `GameOverWindow`.
* `[c]` Skip achievements local saving; achievements will now only be available online and load from Google.
* `[+]` Add new achievements for score.

### Version 0.3.0.0028 alpha *(17-Apr-2015 / Build 28)*
* `[f]` Fix a bug with level 20 Achievement.
* `[c]` Back button in game screen now goes to pause window (before it exited to `MenuScreen`).
* `[c]` An aborted game will now also be added to the leaderboards.

### Version 0.2.4 alpha *(11-Apr-2015 / Build 24)*
* `[-]` Remove some outdated images.
* `[*]` New level builder for more consistent levels.
* `[*]` A lot of code cleanup in the main game class.

### Version 0.2.3 alpha *(10-Apr-2015 / Build 23)*
* `[*]` Fix a problem where Achievements would not be received when the Achievement comes offline.

### Version 0.2.2 alpha *(10-Apr-2015 / Build 22)*
* `[+]` Add *Google Analytics V4* service.
* `[+]` Add **SCORE** button to `GameOverWindow`.
* `[c]` Change methods for *Android Game Services* to access with *Google Play Services, Version 7.0 (March 2015)*.
