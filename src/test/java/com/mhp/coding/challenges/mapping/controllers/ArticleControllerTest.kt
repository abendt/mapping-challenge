package com.mhp.coding.challenges.mapping.controllers

import com.mhp.coding.challenges.mapping.services.ArticleService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.hasSize
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @SpyBean
    lateinit var articleService: ArticleService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `get by id returns status 404 when article cannot be found`() {
        Mockito.doReturn(null).whenever(articleService).articleForId(any())

        mockMvc.perform(MockMvcRequestBuilders.get("/article/4711"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    fun `get by id returns status 200 when article was found`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/4711"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    fun `get by id returns returns JSON response`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/article/4711"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(4711)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.equalTo("Article Nr.: 4711")))
    }

    @Test
    fun `list returns returns JSON response`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/article"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize<Any>(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", CoreMatchers.equalTo(1001)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title", CoreMatchers.equalTo("Article Nr.: 1001")))
    }
}
