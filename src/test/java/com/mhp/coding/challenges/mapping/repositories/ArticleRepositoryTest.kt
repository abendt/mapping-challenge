package com.mhp.coding.challenges.mapping.repositories

import com.mhp.coding.challenges.mapping.models.db.blocks.ArticleBlock
import com.mhp.coding.challenges.mapping.models.db.blocks.GalleryBlock
import com.mhp.coding.challenges.mapping.models.db.blocks.ImageBlock
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.all
import strikt.assertions.isNotNull

// expose a bug in ArticleRepository

class ArticleRepositoryTest {

    val objectUnderTest = ArticleRepository()

    @Test
    fun imageBlockShouldContainImage() {

        val blocks = getBlocksFromArticle()

        val imageBlocksOnly = blocks.mapNotNull { it as? ImageBlock }

        expectThat(imageBlocksOnly).all {
            get { image }.isNotNull()
        }
    }

    private fun getBlocksFromArticle(): MutableSet<ArticleBlock> =
            objectUnderTest.findBy(1)?.blocks ?: throw AssertionError()

    @Test
    fun galleryImagesShouldContainImage() {
        val blocks = getBlocksFromArticle()

        val galleryBlocks = blocks.mapNotNull { it as? GalleryBlock }

        expectThat(galleryBlocks).all {
            get { images }.all { isNotNull() }
        }
    }
}
