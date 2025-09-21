import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "dev.g000sha256"
version = "1.1.0"

plugins {
    alias(notation = catalog.plugins.g000sha256.sonatypeMavenCentral)
    alias(notation = catalog.plugins.gradle.javaGradlePlugin)
    alias(notation = catalog.plugins.gradle.mavenPublish)
    alias(notation = catalog.plugins.gradle.signing)
    alias(notation = catalog.plugins.jetBrains.binaryCompatibilityValidator)
    alias(notation = catalog.plugins.jetBrains.kotlin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withJavadocJar()
    withSourcesJar()
}

kotlin {
    explicitApi()

    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
        moduleName = "dev.g000sha256.gradle_module_tree"
    }
}

dependencies {
    implementation(dependencyNotation = catalog.libraries.jetBrains.annotations)
    implementation(dependencyNotation = catalog.libraries.jetBrains.kotlin)

    val gradleApiDependency = gradleApi()
    implementation(dependencyNotation = gradleApiDependency)
}

gradlePlugin {
    plugins {
        register("release") {
            id = "dev.g000sha256.gradle-module-tree"
            implementationClass = "dev.g000sha256.gradle_module_tree.ModuleTreePlugin"
        }
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                name = "Gradle Module Tree plugin"
                description = "A Gradle settings plugin that provides a hierarchical DSL " +
                    "for organizing and auto-creating multi-module project structures"

                url = "https://github.com/g000sha256/gradle-module-tree"
                inceptionYear = "2025"

                licenses {
                    license {
                        name = "Apache License 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                developers {
                    developer {
                        id = "g000sha256"
                        name = "Georgii Ippolitov"
                        email = "detmmpmznb@g000sha256.dev"
                        url = "https://github.com/g000sha256"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/g000sha256/gradle-module-tree.git"
                    developerConnection = "scm:git:git@github.com:g000sha256/gradle-module-tree.git"
                    url = "https://github.com/g000sha256/gradle-module-tree"
                }

                issueManagement {
                    system = "GitHub Issues"
                    url = "https://github.com/g000sha256/gradle-module-tree/issues"
                }
            }
        }
    }
}

signing {
    val key = getProperty(key = "Signing.Key") ?: getEnvironment(key = "SIGNING_KEY")
    val password = getProperty(key = "Signing.Password") ?: getEnvironment(key = "SIGNING_PASSWORD")
    useInMemoryPgpKeys(key, password)

    sign(publishing.publications)
}

sonatypeMavenCentralRepository {
    credentials {
        username = getProperty(key = "SonatypeMavenCentral.Username") ?: getEnvironment(key = "SONATYPE_USERNAME")
        password = getProperty(key = "SonatypeMavenCentral.Password") ?: getEnvironment(key = "SONATYPE_PASSWORD")
    }
}

private fun getProperty(key: String): String? {
    return properties.get(key = key) as String?
}

private fun getEnvironment(key: String): String? {
    return System.getenv(key)
}
