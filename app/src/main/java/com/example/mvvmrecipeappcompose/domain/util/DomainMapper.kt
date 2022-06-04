package com.example.mvvmrecipeappcompose.domain.util

interface DomainMapper<T, DomainModel>{ // generic: can serve networkDTO, cacheDTO, databaseDTO

    fun mapToDomainModel(model: T) : DomainModel

    fun mapFromDomainModel(domainModel: DomainModel) : T
}