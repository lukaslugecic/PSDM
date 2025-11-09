package com.example.psdmclientapp.model.request

import com.example.psdmclientapp.model.request.SolutionRequest

data class GroupSolutionRequest (
    val solutionIds: List<Long>,
    val solution: SolutionRequest
)