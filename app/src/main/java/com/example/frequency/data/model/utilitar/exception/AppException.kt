package com.example.frequency.data.model.utilitar.exception

open class AppException : RuntimeException()

class PasswordMismatchException : AppException()

class AccountAlreadyExistsException : AppException()

class AuthException : AppException()

class StorageException: AppException()