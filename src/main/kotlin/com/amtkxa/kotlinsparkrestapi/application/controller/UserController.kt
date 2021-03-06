package com.amtkxa.kotlinsparkrestapi.application.controller

import com.amtkxa.kotlinsparkrestapi.application.helper.JsonTransformer
import com.amtkxa.kotlinsparkrestapi.domain.model.User
import com.amtkxa.kotlinsparkrestapi.domain.service.UserService
import com.amtkxa.kotlinsparkrestapi.domain.service.UserServiceImpl
import com.amtkxa.kotlinsparkrestapi.infrastructure.annotation.SparkController
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import spark.Route
import spark.Spark.*

@SparkController
class UserController {
    private val jsonTransformer = JsonTransformer(ObjectMapper().registerKotlinModule())
    private val mapper = ObjectMapper()
    private val userService: UserService = UserServiceImpl()

    init {
        path("/users") {
            get("", index(), jsonTransformer)
            get("/:id", show(), jsonTransformer)
            post("", create(), jsonTransformer)
            patch("", update(), jsonTransformer)
            delete("/:id", destroy(), jsonTransformer)
        }
    }

    private fun index(): Route = Route { req, res ->
        userService.findAll()
    }

    private fun show(): Route = Route { req, res ->
        userService.findById(
            id = req.params("id").toLong()
        )
    }

    private fun create(): Route = Route { req, res ->
        val request = mapper.readValue(req.body(), User::class.java)
        val id = userService.create(
            id = request.id,
            name = request.name
        )
        res.status(201)
        id
    }

    private fun update(): Route = Route { req, res ->
        val request = mapper.readValue(req.body(), User::class.java)
        val id = userService.update(
            id = request.id,
            name = request.name
        )
        res.status(200)
        id
    }

    private fun destroy(): Route = Route { req, res ->
        userService.delete(
            id = req.params("id").toLong()
        )
        res.status(204)
    }
}
