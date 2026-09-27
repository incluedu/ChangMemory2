# To-Do-Liste - ChangMemory II Modernisierung

Diese Liste dokumentiert die verbleibenden Schritte, um *ChangMemory II* auf einen absolut sauberen, performanten und fehlerfreien Kotlin/LibKTX-Stand zu bringen.

---

## 🟥 Priorität 1: Datenhaltung & UI-Infrastruktur vorbereiten (Jetzt fällig)
Ziel: Das Fundament für Einstellungen und globale Layouts glattziehen, um doppelten Code im restlichen Projekt zu verhindern.

- [ ] **`GamePreferences.kt` (Einstellungen speichern)**
    - [ ] Die Einstellungs-Klasse in ein echtes Kotlin-**`object`**-Singleton umwandeln, um das alte `.Companion.instance`-Muster endgültig loszuwerden.

---

## 🟨 Priorität 2: Screens modernisieren & UI vereinheitlichen
Ziel: Alten Java-Layout-Boilerplate-Code entfernen, anonyme Listener durch flache Kotlin-Lambdas ersetzen und auf KTX Scene2D DSL umstellen.

- [ ] **Globales UI- & Header-Refactoring (Konsistenz-Upgrade)**
    - [ ] **Zentraler Header:** Das Spiellogo („CHANG MEMORY II“ & Copyright) in eine wiederverwendbare Komponente auslagern, um doppelten Code in allen Screens zu verhindern.
    - [ ] **Google-Dienste aktualisieren:** Google Plus (G+) restlos aus dem UI entfernen. Die Google-Play-Buttons so überarbeiten, dass sie am Desktop unsichtbar sind und nur unter Android aktiv schalten.
    - [ ] **Versionsinfo im Hauptmenü:** Die Anzeige der Versionsnummer auch unten im Hauptmenü (`MenuLayout`) einbauen.
- [ ] **`SettingsScreen.kt` (Einstellungen)**
    - [ ] UI-Slider, Checkboxen und Layout-Tabellen auf KTX-DSL-Syntax umstellen.
    - [ ] Die Steuerung sauber an das neue `GamePreferences`-Singleton koppeln.
    - [ ] Das Logging auf `ktx-log` umrüsten.
- [ ] **`ScoreScreen.kt` (Highscore-Tafel)**
    - [ ] Die Ausrichtung finalisieren und an das neue `ScoreList`-Design anbinden.
- [ ] **`CreditsScreen.kt` (Abspann)**
    - [ ] **Hintergrund-Fix:** Dem Credits-Screen ebenfalls die grüne Schiefertafel (`Skins.BACKGROUND_6`) als Hintergrund verpassen.
    - [ ] Den Screen komplett modernisieren und das Logging auf `ktx-log` umrüsten.
- [ ] **Scoreboard-Typografie verfeinern (Optisches Upgrade)**
    - [ ] Eine passende Kreide-Schriftart im **Monospace-Format** (.ttf) einbinden, damit die Zahlenkolonnen auf der Tafel exakt vertikal untereinander fluchten.

---

## 🟩 Priorität 3: Spielobjekte & Android-Schnittstellen (Später)
Ziel: Speicherfressende Strukturen optimieren und die Google Play Games Services v2 auf den neuesten Stand bringen.

- [ ] **`Card.kt` & `CardList.kt`**
    - [ ] Den Karten-Actor auf unnötige Objekt-Erzeugungen innerhalb der permanenten `update`-Schleife prüfen.
    - [ ] Die Zustandsänderungen in saubere Kotlin-Properties umwandeln.
- [ ] **Build-System finalisieren (Groovy-zu-KTS Migration)**
    - [ ] Die verbleibenden Build-Skripte von `lwjgl3/build.gradle` und `android/build.gradle` auf das moderne Kotlin DSL-Format (`.gradle.kts`) umstellen.
- [ ] **Google Play Games Services (GPGS) v2 einbinden**
    - [ ] Die aktuelle Google Play Games v2 SDK-Abhängigkeit einbinden.
    - [ ] Den modernen `PlayGames.getLeaderboardsClient(this)` und `getAchievementsClient(this)` in der `AndroidLauncher.kt` aktivieren.

---

## 🏆 Erledigte Meilensteine (Wall of Fame)
- [x] Das gesamte Build-System erfolgreich auf **Kotlin DSL (`.gradle.kts`)** umgestellt.
- [x] Veralteten LWJGL2-Desktop-Launcher durch eine moderne **LWJGL3-Engine** ersetzt.
- [x] `Assets`, `ScoreList`, `AudioManager`, `InfoList` und `AchievementManager` in native Kotlin-**`object` Singletons** verwandelt.
- [x] **`MenuScreen.kt`**: Vollständig splittet! Die UI-Strukturen wurden sauber in das neue **`MenuLayout.kt`** ausgelagert, von 54 Warnungen befreit, auf KTX-Logging umgestellt und der zerstörerische `hide()`-Lifecycle-Bug gefixt.
- [x] **Echtes Vektor-Schriftensystem:** Die alten, klobigen Bitmap-Schriften restlos entfernt und durch den dynamischen **`FreeTypeFontGenerator`** mit deiner neuen Lieblingsschrift **`ArchitectsDaughter.ttf`** ersetzt. Alle Größen werden im RAM über ein ultrakurzes Extension-Befehlsmuster (`generator.create()`) verwaltet.
- [x] Den schweren Logik-Fehler behoben, bei dem falsche Karten-Paare `cardSetTries` doppelt (+2 statt +1) bestraft haben.
- [x] Die Musikallokationen durch Wechsel von bitweisem `and` auf logisches `&&` in der Audio-Schleife korrigiert.
- [x] `ActionResolver`-Interface komplett plattformunabhängig entkoppelt und ein typensicheres `GameAchievement`-Enum eingeführt.
- [x] Den `AndroidLauncher` sowie den `Lwjgl3Launcher` zu 100 % auf **Kotlin und KTX-Log** migriert.
- [x] Die AdMob-Werbung restlos aus dem gesamten Android-Subsystem entfernt.
- [x] Die gesamte Versionsnummer als `APP_VERSION` in den zentralen `Constants` verankert.
