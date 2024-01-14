package com.desss.collegeproduct.repository.model.errorModel

data class Error (
    val code: String,
    val message: String,
    val isPaymentFailure: Boolean,
    val fieldErrors: List<FieldError>,
    val links: List<Link>
)

data class FieldError (
    val field: String,
    val error: String
)

data class Link (
    val rel: String,
    val href: String
)
