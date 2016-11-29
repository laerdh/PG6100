package no.westerdals.pg6100.gameapi.dao;

import no.westerdals.pg6100.gameapi.core.Game;
import no.westerdals.pg6100.gameapi.core.mapper.GameMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(GameMapper.class)
public interface GameDao {

    @SqlQuery("select * from GAME limit :limit")
    List<Game> getAll(@Bind("limit") Integer limit);

    @SqlQuery("select * from GAME where id = :id")
    Game findById(@Bind("id") Long id);

    @SqlUpdate("update GAME set answered = answered + 1 where id = :id")
    int updateAnswer(@Bind("id") Long id);

    @SqlUpdate("insert into GAME (quizzes, answered, totalQuizzes) values (:quizzes, :answered, :totalQuizzes)")
    @GetGeneratedKeys
    Long insert(@Bind("quizzes") String quizzes,
                @Bind("answered") Integer answered,
                @Bind("totalQuizzes") Integer totalQuizzes);
}
