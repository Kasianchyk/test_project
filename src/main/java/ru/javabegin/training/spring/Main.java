package ru.javabegin.training.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.javabegin.training.spring.dao.objects.MP3;
import ru.javabegin.training.spring.impl.SQLiteDAO;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        MP3 mp3 = new MP3();
        mp3.setName("Song_name");
        mp3.setAuthor("Author_name");

        SQLiteDAO sqLiteDAO = (SQLiteDAO)context.getBean("sqliteDAO");
        sqLiteDAO.insert(mp3);
        MP3 mp3ByID = sqLiteDAO.getMP3ByID(7);
        String test = "";

    }
}
