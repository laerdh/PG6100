package no.westerdals.pg6100.gameapi.core.mapper;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.westerdals.pg6100.gameapi.core.Game;
import java.lang.reflect.Type;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameMapper implements ResultSetMapper<Game> {
    public Game map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {

        // List of quizzes is saved as JSON String in DB
        // Need to convert from JSON Array to List<Long>
        String json = resultSet.getString("quizzes");
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        Type listType = new TypeToken<List<Long>>() {}.getType();
        List<Long> data = new Gson().fromJson(jsonObject.get("quizzes"), listType);

        return new Game(resultSet.getLong("id"), data, resultSet.getInt("answered"),
                resultSet.getInt("totalQuizzes"));
    }
}
