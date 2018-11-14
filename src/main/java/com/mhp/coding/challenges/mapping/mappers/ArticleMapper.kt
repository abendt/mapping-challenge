package com.mhp.coding.challenges.mapping.mappers

import com.mhp.coding.challenges.mapping.models.db.Article
import com.mhp.coding.challenges.mapping.models.db.blocks.*
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto
import com.mhp.coding.challenges.mapping.models.dto.blocks.GalleryBlockDto
import io.github.classgraph.ClassGraph
import ma.glasnost.orika.impl.DefaultMapperFactory
import org.springframework.stereotype.Component
import java.util.*
import com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock as ImageBlockDto
import com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock as TextBlockDto
import com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock as VideoBlockDto

@Component
class ArticleMapper {

    /**
     * define to which dto type a given block entity should be mapped.
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
     * check that we how to map all found Block entities. this check will fail if a new
     * block entity is introduced w/o updating our blockMappingConfiguration configuration above.
     */
    private fun verifyMappingConfiguration() {
        ClassGraph()
                .enableClassInfo()
                .whitelistPackages(ArticleBlock::class.java.`package`.name)
                .scan().use { scanResult ->

                    val foundEntities = scanResult.getSubclasses(ArticleBlock::class.java.name)
                            .map { it.name }
                            .toSet()

                    val configuredEntities = blockMappingConfiguration.keys.map { it.qualifiedName }.toSet()

                    val notConfiguredEntities = foundEntities - configuredEntities

                    if (!notConfiguredEntities.isEmpty()) {
                        throw RuntimeException("configuration error: detected unknown Entities:\n" + notConfiguredEntities.joinToString(separator = "\n") { "* $it" })
                    }
                }
    }

    private val mapper = DefaultMapperFactory.Builder().build().also {
        blockMappingConfiguration.forEach { entity, dto ->
            it.classMap(entity.java, dto.java).byDefault().register()
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
