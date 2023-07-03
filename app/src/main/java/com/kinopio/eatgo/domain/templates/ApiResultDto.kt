package com.kinopio.eatgo.domain.templates

data class ApiResultDto(
    val status: String,
    val message: String,
) {
    companion object {
        const val STATUS_SUCCESS = "success"
        const val STATUS_FAIL = "fail"
        const val STATUS_ERROR = "error"
    }
}
