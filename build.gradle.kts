plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "me.xtopz.shooter"
version = "1.0-SNAPSHOT"

application {
    mainClass = "me.xtopz.shooter.ShooterApplication"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-controls:17")
    implementation("org.openjfx:javafx-fxml:17")
}

javafx {
    modules(
        "javafx.controls",
        "javafx.fxml",
        "javafx.graphics",
    )
}