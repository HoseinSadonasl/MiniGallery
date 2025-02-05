pluginManagement {
    includeBuild("build-logic")
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
        mavenCentral()
    }
}

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "MiniGallery"
include(":app")
include(":feature:home")
include(":feature:albums")
include(":libraries:navigation")
include(":libraries:designsystem")
include(":media:media-domain")
include(":media:media-data")
include(":core:database")
include(":media:media-datasource")
include(":core:datastore")
include(":core:common")
include(":core:domain")
include(":libraries:ui")
