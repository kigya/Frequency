package com.example.frequency.data.model.utilitar.exception

import com.example.frequency.data.model.utilitar.Field

class EmptyFieldException(
    val field: Field
) : AppException()