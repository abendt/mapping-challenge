package com.mhp.coding.challenges.mapping.services

import com.mhp.coding.challenges.mapping.mappers.ArticleMapper
import com.mhp.coding.challenges.mapping.models.db.Article
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto
import com.mhp.coding.challenges.mapping.repositories.ArticleRepository
import org.springframework.stereotype.Service

@Service
class ArticleService(
        private val repository: ArticleRepository,
        private val mapper: ArticleMapper
) {

    fun list(): List<ArticleDto> =
            repository.all()
                    .map { it.toDto() }

    fun articleForId(id: Long): ArticleDto? =
            repository.findBy(id)?.toDto()

    fun create(articleDto: ArticleDto): ArticleDto =
            mapper.map(articleDto)
                    .also(repository::create)
                    .toDto()

    private fun Article.toDto() =
            mapper.map(this)
                    .also { dto -> dto.blocks = dto.blocks.sortedBy { it.sortIndex } }
}
