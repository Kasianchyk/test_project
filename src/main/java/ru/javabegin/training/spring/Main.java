package ru.javabegin.training.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.javabegin.training.spring.dao.interfaces.MP3Dao;
import ru.javabegin.training.spring.dao.objects.MP3;
import ru.javabegin.training.spring.dao.objects.Singer;
import ru.javabegin.training.spring.impl.SQLiteDAO;

public class Main {
    public static void main(String[] args) {


        MP3 mp3 = new MP3();
        mp3.setName("Simple song");

        Singer singer = new Singer();
        singer.setFirstName("Android");
        mp3.setAuthor(singer);


        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        MP3Dao sqLiteDAO = (MP3Dao)context.getBean("sqliteDAO");
        System.out.println(sqLiteDAO.insertMp3(mp3));

    }
}
