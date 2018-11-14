package com.mhp.coding.challenges.mapping.services

import com.mhp.coding.challenges.mapping.mappers.ArticleMapper
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto
import com.mhp.coding.challenges.mapping.repositories.ArticleRepository
import org.springframework.stereotype.Service

@Service
class ArticleService(private val repository: ArticleRepository, private val mapper: ArticleMapper) {

    fun list(): List<ArticleDto> =
            repository.all()
                    .map { mapper.map(it) }
                    .map { sortBlocks(it) }

    private fun sortBlocks(dto: ArticleDto) =
            dto.also {
                it.blocks = it.blocks.sortedBy { it.sortIndex }
            }

    fun articleForId(id: Long): ArticleDto? =
            repository.findBy(id)
                    ?.let { sortBlocks(mapper.map(it)) }

    fun create(articleDto: ArticleDto): ArticleDto {
        val create = mapper.map(articleDto)
        repository.create(create)
        return mapper.map(create)
    }
}
