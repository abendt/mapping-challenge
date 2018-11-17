package com.mhp.coding.challenges.mapping.mappers

import com.mhp.coding.challenges.mapping.models.db.Article
import com.mhp.coding.challenges.mapping.models.db.blocks.*
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto
import com.mhp.coding.challenges.mapping.models.dto.blocks.GalleryBlockDto
import io.github.classgraph.ClassGraph
import ma.glasnost.orika.impl.DefaultMapperFactory
import org.springframework.stereotype.Component
import com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock as ImageBlockDto
import com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock as TextBlockDto
import com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock as VideoBlockDto

@Component
class ArticleMapper {

    /**
     * configure mapping from entity -> dto.
     */
    private val blockMappingConfiguration = mapOf(
            TextBlock::class to TextBlockDto::class,
            GalleryBlock::class to GalleryBlockDto::class,
            ImageBlock::class to ImageBlockDto::class,
            VideoBlock::class to VideoBlockDto::class
    )

    init {
        verifyMappingConfiguration()
    }

    /**
     * this check will fail if a new
     * block entity is introduced w/o updating our mapping configuration above.
     */
    private fun verifyMappingConfiguration() {
        ClassGraph()
                .enableClassInfo()
                .whitelistPackages(ArticleBlock::class.java.`package`.name)
                .scan().use { scanResult ->

                    val foundEntities = scanResult
                            .getSubclasses(ArticleBlock::class.java.name)
                            .map { it.name }
                            .toSet()

                    val configuredEntities = blockMappingConfiguration
                            .keys
                            .map { it.qualifiedName }
                            .toSet()

                    val notConfiguredEntities = foundEntities - configuredEntities

                    if (!notConfiguredEntities.isEmpty()) {
                        // fail fast here
                        throw RuntimeException("configuration error: detected unknown Entities:\n" + notConfiguredEntities.joinToString(separator = "\n") { "* $it" })
                    }
                }
    }

    private val mapper = DefaultMapperFactory.Builder().build().also { mapperFactory ->
        blockMappingConfiguration.forEach { entity, dto ->
            mapperFactory.classMap(entity.java, dto.java).byDefault().register()
        }
    }.mapperFacade

    fun map(article: Article): ArticleDto {
        return mapper.map(article, ArticleDto::class.java)
    }

    fun map(articleDto: ArticleDto): Article {
        // Nicht Teil dieser Challenge.
        return Article()
    }

}
