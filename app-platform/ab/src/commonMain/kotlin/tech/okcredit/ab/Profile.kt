package tech.okcredit.ab

import tech.okcredit.ab.remote.Experiment

data class Profile(
    val features: Map<String, Boolean> = emptyMap(),
    val experiments: Map<String, Experiment> = emptyMap(),
)
