/*
 * Copyright 2025 Georgii Ippolitov (g000sha256)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.g000sha256.gradle_module_tree

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

public class ModuleTreePlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        target.enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

        val includesBuilder = target.createIncludeBuilder()

        target.extensions.add(
            IncludesBuilder::class.java,
            "buildIncludes",
            createDeprecatedIncludeBuilder(includesBuilder = includesBuilder),
        )

        target.extensions.add(
            IncludesBuilder::class.java,
            "include",
            includesBuilder,
        )
    }

    private fun createDeprecatedIncludeBuilder(includesBuilder: IncludesBuilder): IncludesBuilder {
        return object : IncludesBuilder {

            override fun directory(name: String, builder: IncludesBuilder.() -> Unit) {
                printWarning()
                includesBuilder.directory(name = name, builder = builder)
            }

            override fun module(name: String) {
                printWarning()
                includesBuilder.module(name = name)
            }

            private fun printWarning() {
                println(message = "Warning: 'buildIncludes' is deprecated and will be removed in future versions. Use 'include' instead.")
            }

        }
    }

    private fun Settings.createIncludeBuilder(
        directories: List<String> = emptyList(),
    ): IncludesBuilder {
        return object : IncludesBuilder {

            override fun directory(name: String, builder: IncludesBuilder.() -> Unit) {
                val directories = directories + name

                createDirectory(directories = directories)

                val includesBuilder = createIncludeBuilder(directories = directories)
                includesBuilder.builder()
            }

            override fun module(name: String) {
                val directories = directories + name

                createDirectory(directories = directories)

                val path = directories.joinToString(separator = ":", prefix = ":")
                include(path)
            }

//            private fun moduleIf(name: String, condition: () -> Boolean) {
//                val createModule = condition()
//                if (createModule) {
//                    module(name)
//                }
//            }

        }
    }

//    private fun validateName(name: String) {
//        require(name.isNotBlank()) { "Module/directory name cannot be blank" }
//        require(!name.contains(":")) { "Name cannot contain ':' character: $name" }
//        require(name.matches(Regex("[a-zA-Z0-9_-]+"))) { "Invalid characters in name: $name" }
//    }

    private fun createDirectory(directories: List<String>) {
        val path = directories.joinToString(separator = "/")
        val file = File(path)
        file.mkdirs()
    }

}
