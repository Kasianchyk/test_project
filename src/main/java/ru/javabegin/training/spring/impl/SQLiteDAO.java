package ru.javabegin.training.spring.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.javabegin.training.spring.dao.interfaces.MP3Dao;
import ru.javabegin.training.spring.dao.objects.MP3;
import ru.javabegin.training.spring.dao.objects.Singer;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    @Transactional(propagation = Propagation.MANDATORY)
    public int insertMp3(MP3 mp3) {
        System.out.println(TransactionSynchronizationManager.isActualTransactionActive());


        int singer_id = insertSinger(mp3.getSinger());

        String insertSqlMp3 = "insert into mp3 (singer_id, name) values(:singer_id, :name)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", mp3.getName());
        params.addValue("singer_id", singer_id);
        return jdbcTemplate.update(insertSqlMp3, params);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertSinger(Singer singer) {
        System.out.println(TransactionSynchronizationManager.isActualTransactionActive());

        String insertSqlSinger = "insert into singer (name) values(:name)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", singer.getFirstName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(insertSqlSinger, params, keyHolder);

        return keyHolder.getKey().intValue();
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
        String sql = "select * from " + mp3View + " where upper(mp3_name) LIKE :mp3_name";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mp3_name", "%" + name.toUpperCase() + "%");
        return jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String singer) {
        String sql = "select * from " + mp3View + " where upper(singer_name) LIKE :singer_name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("singer_name", "%" + singer.toUpperCase() + "%");
        return jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    @Override
    public int insertMp3List(List<MP3> mp3List) {
        int counter = 0;
        for (MP3 mp3 : mp3List) {
            insertMp3(mp3);
            counter++;
        }
        return counter;
    }

    @Override
    public int getMp3Count() {
        String sql = "select count(*) from " + mp3Table;
        return jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }

    @Override
    public Map<String, Integer> getStats() {
        String sql = "select singer_name, count(*) as count from " + mp3View + " group by singer_name";
        return jdbcTemplate.query(sql, resultSet -> {
            Map<String, Integer> map = new TreeMap<>();
            while (resultSet.next()){
                String singer = resultSet.getString("singer_name");
                int count = resultSet.getInt("count");
                map.put(singer, count);
            }
            return map;
        });
    }

    private static final class MP3RowMapper implements RowMapper<MP3>{
        @Override
        public MP3 mapRow(ResultSet resultSet, int i) throws SQLException {
            Singer singer = new Singer();
            singer.setId(resultSet.getLong("singer_id"));
            singer.setFirstName(resultSet.getString("singer_name"));
            MP3 mp3 = new MP3();
            mp3.setId(resultSet.getInt("id"));
            mp3.setName(resultSet.getString("name"));
            mp3.setAuthor(singer);
            return mp3;
        }
    }
}

