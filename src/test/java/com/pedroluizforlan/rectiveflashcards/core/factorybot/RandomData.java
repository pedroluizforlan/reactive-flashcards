package com.pedroluizforlan.rectiveflashcards.core.factorybot;

import com.github.javafaker.Faker;

import java.util.Locale;

public class RandomData {

    private static final Faker faker = new Faker(new Locale("pt","br"));

    public static Faker getFaker(){
        return faker;
    }
}
