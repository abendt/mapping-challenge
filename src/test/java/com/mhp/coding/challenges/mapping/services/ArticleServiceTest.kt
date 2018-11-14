package com.mhp.coding.challenges.mapping.services

import com.mhp.coding.challenges.mapping.mappers.ArticleMapper
import com.mhp.coding.challenges.mapping.repositories.ArticleRepository
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.all
import strikt.assertions.isNotNull
import strikt.assertions.isSorted

class ArticleServiceTest {

    @Test
    fun `articleForId Dto collection is sorted by sortIndex`() {
        val articleService = ArticleService(ArticleRepository(), ArticleMapper())

        val article = articleService.articleForId(1)

        expectThat(article)
                .isNotNull()
                .and {
                    get { blocks }.isSorted(compareBy { it.sortIndex })
                }
    }

    @Test
    fun `list Dto collections are sorted by sortIndex`() {
        val articleService = ArticleService(ArticleRepository(), ArticleMapper())

        val article = articleService.list()

        expectThat(article.map { it.blocks })
                .all { isSorted(compareBy { it.sortIndex }) }
    }
}
