pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven {
            url = uri("https://jitpack.io")
            credentials { username = "jp_p1hah77u2v08mli199fs77olfu" }
        }
        mavenCentral()
    }
}

rootProject.name = "QR Code"
include(":app")
include(":qr-code")
