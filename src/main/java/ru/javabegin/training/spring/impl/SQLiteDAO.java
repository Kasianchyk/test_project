package ru.javabegin.training.spring.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.javabegin.training.spring.dao.interfaces.MP3Dao;
import ru.javabegin.training.spring.dao.objects.MP3;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("sqliteDAO")
public class SQLiteDAO implements MP3Dao {

    private static final String mp3Table = "mp3";
    private static final String mp3View = "mp3_view";

    private SimpleJdbcInsert insertMp3;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(DataSource dataSource){
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertMp3 = new SimpleJdbcInsert(dataSource).withTableName("mp3").usingColumns("name", "singer");
    }

    @Override
    public void insert(MP3 mp3) {
        /*String sql = "insert into mp3 (name, author) VALUES (?, ?)";
        jdbcTemplate.getJdbcOperations().update(sql, new Object[]{mp3.getName(), mp3.getSinger()});*/
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", mp3.getName());
        params.addValue("singer", mp3.getSinger());
        insertMp3.execute(params);
    }

    @Override
    public void delete(int id) {
        String sql = "delete from mp3 where id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.getJdbcOperations().execute(sql);
    }

    @Override
    public MP3 getMP3ByID(int id) {
        String sql = "select * from mp3 where id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.queryForObject(sql, params, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByName(String name) {
        return null;
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String author) {
        return null;
    }

    @Override
    public int insertMp3List(List<MP3> mp3List) {
        String sql = "insert into mp3 (name, singer) values (:name, :singer)";
        SqlParameterSource[] params = new SqlParameterSource[mp3List.size()];

        int counter = 0;
        for (MP3 mp3 : mp3List) {
            MapSqlParameterSource p = new MapSqlParameterSource();
            p.addValue("name", mp3.getName());
            p.addValue("singer", mp3.getSinger());
            params[counter] = p;
            counter++;
        }
        return jdbcTemplate.batchUpdate(sql, params).length;
    }

    private static final class MP3RowMapper implements RowMapper<MP3>{

        @Override
        public MP3 mapRow(ResultSet resultSet, int i) throws SQLException {
            MP3 mp3 = new MP3();
            mp3.setId(resultSet.getInt("id"));
            mp3.setName(resultSet.getString("name"));
            return mp3;
        }
    }
}

