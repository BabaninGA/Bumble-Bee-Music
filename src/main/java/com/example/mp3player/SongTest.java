package com.example.mp3player;

public class SongTest extends Song {
    public void setNameTest() {
        String sname = "чупапи муняня";
        setName(sname);
        System.out.println(name + "\tдолжно быть: " + sname);
    }

    public void setGeneralNameTest() {
        String name = "12345";
        setGeneral_name(name);
        System.out.println(general_name + "\tдолжно быть: " + name);
    }

    public void checkAll() {
        System.out.println("Проверка корректности...");
        for (int i = 1; i <= 3; i++) {
            System.out.println(i + "...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setNameTest();
        setGeneralNameTest();
        setAuthorTest();
        setFileTest();
        setPathTest();
    }
    public void setAuthorTest() {
        String name = "1";
        setGeneral_name(name);
        System.out.println(general_name + "\tдолжно быть: " + name);
    }

    public void setFileTest() {
        String name = "Коровы";
        setGeneral_name(name);
        System.out.println(general_name + "\tдолжно быть: " + name);
    }

    public void setPathTest() {
        String name = "Тридцать пять шалашиков под Воронежем";
        setGeneral_name(name);
        System.out.println(general_name + "\tдолжно быть: " + name);
    }


}
