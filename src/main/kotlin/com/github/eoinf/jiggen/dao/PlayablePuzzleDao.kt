package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.DataMapper
import com.github.eoinf.jiggen.PlayablePuzzleRepository
import com.github.eoinf.jiggen.data.PlayablePuzzle
import com.github.eoinf.jiggen.data.PlayablePuzzleDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface IPlayablePuzzleDao {
    fun get() : List<PlayablePuzzleDTO>
    fun get(id: UUID) : PlayablePuzzleDTO?
    fun save(puzzle: PlayablePuzzleDTO) : PlayablePuzzleDTO
}

@Service
open class PlayablePuzzleDao(private val dataMapper: DataMapper) : IPlayablePuzzleDao {
    @Autowired lateinit var playablePuzzleRepository: PlayablePuzzleRepository

    @Transactional
    override fun get(id: UUID) : PlayablePuzzleDTO? {
        val playablePuzzle = playablePuzzleRepository.findById(id).orElse(null)

        if (playablePuzzle == null)
            return null
        else
            return dataMapper.toPlayablePuzzleDTO(playablePuzzle, depth = 2)
    }

    override fun get() : List<PlayablePuzzleDTO> {
        return playablePuzzleRepository.findAll().toList().map {
            dataMapper.toPlayablePuzzleDTO(it, depth=1)
        }
    }

    override fun save(puzzle: PlayablePuzzleDTO) : PlayablePuzzleDTO {
        return dataMapper.toPlayablePuzzleDTO(playablePuzzleRepository.save(PlayablePuzzle(puzzle)), depth=1)
    }
}