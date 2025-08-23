# Gradle Module Tree plugin

[![Maven Central](https://img.shields.io/maven-central/v/dev.g000sha256/gradle-module-tree?label=Maven%20Central&labelColor=171C35&color=E38E33)](https://central.sonatype.com/artifact/dev.g000sha256/gradle-module-tree)

A Gradle settings plugin that provides a hierarchical DSL for organizing and auto-creating multi-module project structures.

### Features

- 🌳 Hierarchical DSL for module organization
- 📁 Automatic directory creation
- 🔗 Automatic module inclusion in Gradle
- ⚡️ Type-safe project accessors support

## Gradle setup

Add this to your `settings.gradle.kts` file:

```kotlin
pluginManagement {
    repositories {
        mavenCentral()
    }
}

plugins {
    id(id = "dev.g000sha256.gradle-module-tree") version "0.0.1"
}
```

## Usage

Define your directories and modules. They will be created and included after syncing the project.

```kotlin
buildIncludes {
    module(name = "app")
    directory(name = "core") {
        module(name = "architecture")
        module(name = "di")
        module(name = "resources")
        module(name = "ui")
    }
    directory(name = "features") {
        directory(name = "main") {
            module(name = "data")
            module(name = "domain")
            module(name = "presentation")
        }
        directory(name = "profile") {
            module(name = "data")
            module(name = "domain")
            module(name = "presentation")
        }
    }
    directory(name = "utils") {
        module(name = "coroutines")
    }
}
```

Then you can reference modules in dependencies using type-safe accessors:

```kotlin
dependencies {
    implementation(dependencyNotation = projects.core.architecture)
    implementation(dependencyNotation = projects.core.resources)
    implementation(dependencyNotation = projects.utils.coroutines)
}
```
