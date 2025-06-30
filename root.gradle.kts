plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    "1.21.5-fabric"(12105, "yarn") {
        "1.8.9-forge"(10809, "srg")
    }
}