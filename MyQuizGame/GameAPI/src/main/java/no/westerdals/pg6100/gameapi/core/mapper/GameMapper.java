package no.westerdals.pg6100.gameapi.core.mapper;

import no.westerdals.pg6100.gameapi.core.Game;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameMapper implements ResultSetMapper<Game> {
    public Game map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Array a = resultSet.getArray("quizzes");
        Long[] input = (Long[]) a.getArray();

        return new Game(resultSet.getLong("id"), new ArrayList<Long>(Arrays.asList(input)), resultSet.getInt("answered"),
                resultSet.getInt("totalQuizzes"));
    }
}
