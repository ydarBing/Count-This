package com.gurpgork.countthis.core.domain

import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.model.data.Counter
import javax.inject.Inject

class GetCounter @Inject constructor(
    private val repository: CounterRepository
) {
    suspend operator fun invoke(id: Long): Counter?{
        return repository.getCounterById(id)
    }
}