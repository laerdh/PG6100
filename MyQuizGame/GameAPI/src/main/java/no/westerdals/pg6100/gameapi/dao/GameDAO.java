package no.westerdals.pg6100.gameapi.dao;

import no.westerdals.pg6100.gameapi.core.Game;
import no.westerdals.pg6100.gameapi.core.mapper.GameMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(GameMapper.class)
public interface GameDAO {

    @SqlQuery("select * from GAME limit :limit")
    List<Game> getAll(@Bind("limit") Integer limit);

    @SqlQuery("select from GAME where id = :id")
    Game findById(@Bind("id") Long id);

    @SqlBatch("insert into GAME (quizzes, answered, totalQuizzes) values (:quizzes, :answered, :totalQuizzes")
    int[] insert(@Bind("quizzes") List<Long> quizzes,
               @Bind("answered") Integer answered,
               @Bind("totalQuizzes") Integer totalQuizzes);
}
