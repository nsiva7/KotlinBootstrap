import com.varabyte.kobweb.gradle.library.util.configAsKobwebLibrary
import kotlinx.html.link
import kotlinx.html.script

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.library)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
}

group = "com.stevdza.san.bootstrap"
version = "0.1.5"

kotlin {
    configAsKobwebLibrary(includeServer = false)

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.kobweb.compose)
            implementation(npm("bootstrap", "5.3.1"))
        }
    }
}

kobweb {
    library {
        index {
            head.add {
                script {
                    src = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
                }
                link {
                    rel = "stylesheet"
                    href = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css"
                }
                link {
                    rel = "stylesheet"
                    href = "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css"
                }
            }
        }
    }
}

publishing {
    publications {
        register("mavenJsLibrary", MavenPublication::class) {
            from(components["kotlin"])
            groupId = "com.github.nsiva7"
            artifactId = "KotlinBootstrap"
            version = version
        }
    }
}
