# To-Do-Liste - ChangMemory II Modernisierung

Diese Liste dokumentiert die verbleibenden Schritte, um *ChangMemory II* auf einen absolut sauberen, performanten und fehlerfreien Kotlin/LibKTX-Stand zu bringen.

---

## 🟨 Aktuelle Entwicklungsphase — Target: [2.0.0-alpha.2] (UI-Konsistenz & Modul-Splits)
Ziel: Alten Java-Layout-Boilerplate-Code entfernen, anonyme Listener durch flache Kotlin-Lambdas ersetzen und komplexe Riesen-Klassen entkoppeln.

- [x] **`SettingsScreen.kt` (Einstellungen)**: Vollständig auf modernste **KTX Scene2D DSL**-Syntax umgerüstet mitsamt smarter Eigenschafts-Kapselung.
- [x] **`CreditsScreen.kt` (Abspann-Framework)**: Die komplette UI-Infrastruktur steht, nutzt den Schiefertafel-Hintergrund und läuft absolut crash-sicher im KTX-Grid.
- [ ] **`CreditsScreen.kt` (Abspann-Inhalte & Typografie modernisieren)**
    - [ ] **Content-Optimierung:** Die unschönen Fragezeichen (`???`) bei `bird.ogg` und `pig.ogg` in der `credits.txt` auflösen (Quellen validieren oder Assets austauschen).
    - [ ] **DSL-Parser:** Den Text nicht als Riesenblock laden, sondern zeilenweise einlesen. Überschriften (z.B. `=== Music ===`) automatisch erkennen und über KTX fett/größer formatieren, um eine echte, optisch ansprechende Film-Credits-Rolle zu erzeugen. Am Dateiende einen künstlichen Padding-Puffer einbauen, damit der Text elegant aus dem Bild gleitet.
- [ ] **`Assets.kt` (Asset-Zentrale entflechten)**
    - [ ] **Klassen-Split:** Die "Gott-Klasse" auflösen. Trennung des asynchronen Kern-Lademanagers von der dynamic TrueType-Schriftgenerierung (`UiSkinFactory`) und der spielspezifischen Karten-Initialisierung (`CardAssetFactory`).
- [ ] **`ScoreList.kt` & `ScorePane.kt` (Highscore-Logik splitten)**
    - [ ] **Klassen-Split:** Die Daten- und Speicherverwaltung strikt vom UI-Layout trennen. Die KTX-Tabelle `scorePane` aus der Datenklasse heraustrennen und als eigenständige UI-Komponente im `ui`-Paket verankern.
- [ ] **`CardScreen.kt` (Spiel-Hauptbildschirm splitten)**
    - [ ] **Klassen-Split:** Den riesigen `CardScreen` radikal aufteilen! Trennung der Core-Spielsteuerung von den HUD-Elementen und Scene2D-Tabellenlayoutern, um die Datei übersichtlich und modular zu halten. *(Teilweise erledigt – GameHUD erfolgreich entkoppelt und typografisch optimiert)*
- [ ] **Globales UI- & Header-Refactoring (Konsistenz-Upgrade)**
    - [ ] **Zentraler Header (`GameHeader.kt`):** Den sich ständig wiederholenden Header („CHANG MEMORY II“ & Copyright) vollständig aus allen Screens herausbrechen. Ein eigenständiges, wiederverwendbares UI-Widget entwerfen, das von `Table` erbt und in jedem Screen per flachem Einzeiler (`add(GameHeader())`) injiziert werden kann.
    - [ ] **Google-Dienste aktualisieren:** Google Plus (G+) restlos aus dem UI entfernen. Die Google-Play-Buttons so überarbeiten, dass sie am Desktop unsichtbar sind und nur unter Android aktiv schalten.
    - [ ] **Versionsinfo im Hauptmenü:** Die Anzeige der Versionsnummer auch unten im Hauptmenü (`MenuLayout`) einbauen.
- [ ] **`ScoreScreen.kt` (Highscore-Tafel)**
    - [ ] Die Ausrichtung finalisieren und an das neue `ScoreList`-Design anbinden.
- [ ] **Scoreboard-Typografie verfeinern (Optisches Upgrade)**
    - [ ] Eine passende Kreide-Schriftart im **Monospace-Format** (.ttf) einbinden, damit die Zahlenkolonnen auf der Tafel exakt vertikal untereinander fluchten.

---

## 🟩 Nächste Entwicklungsphase — Target: [2.0.0-alpha.3] (Core-Logik & Objekt-Splits)
Ziel: Speicherfressende Strukturen optimieren, Google Play Games Services v2 auf den neuesten Stand bringen und Spielobjekte sauber entkoppeln.

- [ ] **`GameController.kt` (UI-Entkopplung)**
    - [ ] **Logik-Entkopplung:** Die Abhängigkeit zu `com.badlogic.gdx.graphics.Color` vollständig entfernen. Warnungs-Farben über semantische Statustypen an `InfoList` übergeben.
- [ ] **`Card.kt` & `CardState.kt` (Karten-Architektur splitten)**
    - [ ] **Klassen-Split:** Core-Spielzustände (Karten-IDs, Aufgedeckte Status) vollständig aus dem Scene2D-`Actor` heraustrennen und in eine leichtgewichtige, allokationsfreie Kotlin-Datenklasse auslagern. `GameController` auf `CardState` umstellen.
- [ ] **`AssetCard.kt` (Ressourcen-Bereinigung)**
    - [ ] **Modul-Verschiebung:** Die reine Datenstruktur aus `game.objects` heraustrennen und als Hilfsklasse in die `Assets.kt` integrieren. Umbenennung in `CardAsset` zur Einhaltung einheitlicher Namenskonventionen.
- [ ] **`FlashLabel.kt` (UI-Paketierung)**
    - [ ] **Paket-Verschiebung:** Das blinkende Textfeld aus dem Logikkern `game.objects` entfernen und an seinen rechtmäßigen Platz im visuellen Paket `ui.actors` verschieben.
- [ ] **Build-System finalisieren (Groovy-zu-KTS Migration)**
    - [ ] Die verbleibenden Build-Skripte von `lwjgl3/build.gradle` und `android/build.gradle` auf das moderne Kotlin DSL-Format (`.gradle.kts`) umstellen.
- [ ] **Google Play Games Services (GPGS) v2 einbinden**
    - [ ] Die aktuelle Google Play Games v2 SDK-Abhängigkeit einbinden.
    - [ ] Den modernen `PlayGames.getLeaderboardsClient(this)` und `getAchievementsClient(this)` in der `AndroidLauncher.kt` aktivieren.

---

## 🏆 Erledigte Meilensteine (Wall of Fame)

### 🟥 Version [2.0.0-alpha.1] — Veröffentlicht am 28.09.2026
- [x] **`GamePreferences.kt` runderneuert**: Die Einstellungs-Klasse vollständig in ein echtes Kotlin-**`object`** umgewandelt, das alte `Companion.instance`-Muster eliminiert, `by lazy`-Injektion für das Preferences-Backend integriert und ungenutzte Achievement-Kommentare entfernt.
- [x] Das gesamte Build-System erfolgreich auf **Kotlin DSL (`.gradle.kts`)** umgestellt.
- [x] Veralteten LWJGL2-Desktop-Launcher durch eine moderne **LWJGL3-Engine** ersetzt.
- [x] `Assets`, `ScoreList`, `AudioManager`, `InfoList` und `AchievementManager` in native Kotlin-**`object` Singletons** verwandelt.
- [x] **`MenuScreen.kt`**: Vollständig gesplittet! Die UI-Strukturen wurden sauber in das neue **`MenuLayout.kt`** ausgelagert, von 54 Warnungen befreit, auf KTX-Logging umgestellt und der zerstörerische `hide()`-Lifecycle-Bug gefixt.
- [x] **Echtes Vektor-Schriftensystem**: Die alten, klobigen Bitmap-Schriften restlos entfernt und durch den dynamischen **`FreeTypeFontGenerator`** mit deiner neuen Lieblingsschrift **`ArchitectsDaughter.ttf`** ersetzt. Alle Größen werden im RAM über ein ultrakurzes Extension-Befehlsmuster (`generator.create()`) verwaltet.
- [x] **Vernichtung von `AbstractCommandWindow`**: Die klobige, fehleranfällige abstrakte Fenster-Basisklasse restlos gelöscht. **`WindowGameOver.kt`** und **`WindowPause.kt`** erben nun direkt von der nativen `Table`, nutzen fehlerfreie Kotlin-`init`-Blöcke ohne Konstruktor-Leaks und sind komplett auf statische Named-Imports umgestellt.
- [x] **Globale Architektur- & Paketbereinigung**: Das gesamte `:core`-Modul über IntelliJ-Refactoring-Pipelines porentief reinwaschen. Den `GameController` aus den Screens verbannt, ein sauberes `.game.model`-Datenpaket für Highscores und Preferences etabliert und alle Enums, UI-Elemente sowie Spracherweiterungen in eigene, logische Namensräume (`.ui.actors`, `.ui.windows`, `.enums`, `.extensions`) entkoppelt.
- [x] **Code-Analyse-Bereinigung**: Die `Constants.kt` vollständig von ungenutzten Variablen-Leichen (`WIDTH`, `HEIGHT`, `LIBGDX_UI`, `FONTS`) befreit.
- [x] **Typensicheres Enum-Befehlssystem**: Das alte Interface `Command` gelöscht und durch das moderne `GameCommand`-Enum ersetzt. `CommandListener` umgestellt und in das Kern-UI-Paket integriert.
- [x] Den schweren Logik-Fehler behoben, bei dem falsche Karten-Paare `cardSetTries` doppelt (+2 statt +1) bestraft haben.
- [x] Die Musikallokationen durch Wechsel von bitweisem `and` auf logisches `&&` in der Audio-Schleife korrigiert.
- [x] `ActionResolver`-Interface komplett plattformunabhängig entkoppelt und ein typensicheres `GameAchievement`-Enum eingeführt.
- [x] Den `AndroidLauncher` sowie den `Lwjgl3Launcher` zu 100 % auf **Kotlin und KTX-Log** migriert.
- [x] Die AdMob-Werbung restlos aus dem gesamten Android-Subsystem entfernt.
- [x] Die gesamte Versionsnummer als `APP_VERSION` in den zentralen `Constants` verankert.
