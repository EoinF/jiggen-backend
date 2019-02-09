package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.DataMapper
import com.github.eoinf.jiggen.PlayablePuzzleRepository
import com.github.eoinf.jiggen.data.PlayablePuzzle
import com.github.eoinf.jiggen.data.PlayablePuzzleDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spark.Request
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

interface IPlayablePuzzleDao {
    fun get(request: Request): List<PlayablePuzzleDTO>
    fun get(request: Request, id: UUID): PlayablePuzzleDTO?
    fun save(request: Request, puzzle: PlayablePuzzleDTO): PlayablePuzzleDTO
    fun getToday(request: Request): List<PlayablePuzzleDTO>
}

@Service
open class PlayablePuzzleDao(private val dataMapper: DataMapper) : IPlayablePuzzleDao {
    @Autowired
    lateinit var playablePuzzleRepository: PlayablePuzzleRepository

    @Transactional
    override fun get(request: Request, id: UUID): PlayablePuzzleDTO? {
        val playablePuzzle = playablePuzzleRepository.findById(id).orElse(null)

        if (playablePuzzle == null)
            return null
        else
            return dataMapper.toPlayablePuzzleDTO(request, playablePuzzle, false)
    }

    override fun get(request: Request): List<PlayablePuzzleDTO> {
        return playablePuzzleRepository.findAllByReleaseDateBefore(Date(Instant.now().toEpochMilli())).toList().map {
            dataMapper.toPlayablePuzzleDTO(request, it, true)
        }
    }

    override fun getToday(request: Request): List<PlayablePuzzleDTO> {
        val todayStartInstant = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val todayStart = Date(todayStartInstant.toEpochMilli())
        val todayEnd = Date(todayStartInstant.plus(1, ChronoUnit.DAYS).toEpochMilli())

        return playablePuzzleRepository.findAllByReleaseDateBetween(todayStart, todayEnd).toList().map {
            dataMapper.toPlayablePuzzleDTO(request, it, true)
        }
    }

    override fun save(request: Request, puzzle: PlayablePuzzleDTO): PlayablePuzzleDTO {
        return dataMapper.toPlayablePuzzleDTO(request, playablePuzzleRepository.save(PlayablePuzzle(puzzle)), false)
    }
}