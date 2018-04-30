package ru.javabegin.training.spring.dao.interfaces;

import java.util.List;
import java.util.Map;

import ru.javabegin.training.spring.dao.objects.MP3;
import ru.javabegin.training.spring.dao.objects.Singer;

public interface MP3Dao {

	int insertMp3(MP3 mp3);

	int insertSinger(Singer singer);

	void delete(int id);

	MP3 getMP3ByID(int id);

	List<MP3> getMP3ListByName(String name);

	List<MP3> getMP3ListByAuthor(String author);

	int insertMp3List(List<MP3> mp3List);

	int getMp3Count();

	Map<String, Integer> getStats();

}
