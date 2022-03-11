package com.example.frequency.model.exception

import com.example.frequency.model.Field

class EmptyFieldException(
    val field: Field
) : AppException()