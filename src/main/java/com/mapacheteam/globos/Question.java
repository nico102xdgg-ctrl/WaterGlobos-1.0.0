package com.mapacheteam.globos;

/** correct = índice 0..3 de la respuesta correcta. */
public record Question(String text, String[] options, int correct) {}
