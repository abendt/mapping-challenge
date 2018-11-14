package com.mhp.coding.challenges.mapping.mappers

import com.mhp.coding.challenges.mapping.models.db.Article
import com.mhp.coding.challenges.mapping.models.db.Image
import com.mhp.coding.challenges.mapping.models.db.ImageSize
import com.mhp.coding.challenges.mapping.models.db.blocks.*
import com.mhp.coding.challenges.mapping.models.dto.blocks.GalleryBlockDto
import org.junit.Test
import java.util.*
import strikt.api.*
import strikt.assertions.*
import com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock as TextBlockDto
import com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock as VideoBlockDto
import com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock as ImageBlockDto

class ArticleMapperTest {

    @Test
    fun canMapEmptyArticle() {
        val dto = ArticleMapper().map(anArticle())

        expectThat(dto) {
            get { id }.isEqualTo(10)
            get { author }.isEqualTo("author")
            get { description }.isEqualTo("description")
            get { title }.isEqualTo("title")
            get { blocks }.isEmpty()
        }
    }

    @Test
    fun canMapArticleWithBlock() {
        val blocks = setOf(aTextBlock("text", 2))
        val dto = ArticleMapper().map(anArticle(blocks))

        expectThat(dto.blocks).hasSize(1)

        expectThat(dto.blocks.first())
                .isA<TextBlockDto>()
                .and {
                    get { text }.isEqualTo("text")
                    get { sortIndex }.isEqualTo(2)
                }
    }

    @Test
    fun canMapArticleWithMixedBlock() {

        val blocks = setOf(aTextBlock(), aGalleryBlock(), aVideoBlock(), anImageBlock())
        val dto = ArticleMapper().map(anArticle(blocks))

        expectThat(dto.blocks).hasSize(4)

        expectThat(dto.blocks.map { it.javaClass })
                .contains(TextBlockDto::class.java,
                        GalleryBlockDto::class.java,
                        VideoBlockDto::class.java,
                        ImageBlockDto::class.java)
    }

    private fun anArticle(withBlocks: Set<ArticleBlock> = emptySet()) =
            Article().apply {
                id = 10
                author = "author"
                description = "description"
                title = "title"
                lastModifiedBy = "lastModifiedBy"
                lastModified = Date()
                blocks = withBlocks
            }


    private fun anImageBlock() =
            ImageBlock().apply {
                image = createImage(1L)
            }

    private fun aGalleryBlock() =
            GalleryBlock().apply {
                val galleryImages = ArrayList<Image>()
                galleryImages.add(createImage())
                galleryImages.add(createImage())

                images = galleryImages
            }

    private fun aVideoBlock() =
            VideoBlock().apply {
                type = VideoBlockType.YOUTUBE
                url = "https://youtu.be/myvideo"
                sortIndex = 5
            }

    private fun aTextBlock(withText: String = "text", withSortIndex: Int = 2) =
            TextBlock().apply {
                text = withText
                sortIndex = withSortIndex
            }

    private fun createImage(withId: Long = 2L) =
            Image().apply {
                id = withId
                lastModified = Date()
                lastModifiedBy = "Max Mustermann"
                imageSize = ImageSize.LARGE
                url = "https://someurl.com/image/1"
            }
}
