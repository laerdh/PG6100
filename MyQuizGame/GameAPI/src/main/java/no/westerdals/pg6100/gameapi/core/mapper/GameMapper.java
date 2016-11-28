package no.westerdals.pg6100.gameapi.core.mapper;

import no.westerdals.pg6100.gameapi.core.Game;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements ResultSetMapper<Game> {
    public Game map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {

        return new Game(resultSet.getLong("id"), resultSet.getInt("quizzes"), resultSet.getInt("answered"),
                resultSet.getInt("totalQuizzes"));
    }
}
