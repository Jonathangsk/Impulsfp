package com.impulsfp.server.service;

import com.impulsfp.server.model.OfferTest;
import com.impulsfp.server.enums.TestType;

/**
 * Factory per crear instàncies de tests d'oferta basats en el tipus de test especificat.
 *
 * @author Jonathan Giraldo Giraldo
 */
public class TestFactory {

    /**
     * Crea un test d'oferta basat en el tipus de test proporcionat. El test es configura amb una pregunta, un fragment de codi, opcions de resposta i la resposta correcta segons el tipus de test.
     * @param type El tipus de test que es vol crear (JAVA, SQL, PYTHON, etc.).
     * @return Una instància de OfferTest configurada segons el tipus especificat.
     */
    public static OfferTest createTest(TestType type){

        OfferTest test = new OfferTest();
        test.setType(type);

        switch (type){

            case JAVA -> {
                test.setQuestion("Quin és el resultat?");
                test.setCodeSnippet("int x = 5; System.out.println(x++);");
                test.setOptions("5;6;error;null");
                test.setCorrectAnswer("5");
            }

            case SQL -> {
                test.setQuestion("Què retorna aquesta query?");
                test.setCodeSnippet("SELECT COUNT(*) FROM users;");
                test.setOptions("files;columnes;error;res");
                test.setCorrectAnswer("files");
            }

            case PYTHON -> {
                test.setQuestion("Quin és el resultat?");
                test.setCodeSnippet("x = 3\nprint(x * 2)");
                test.setOptions("5;6;3;error");
                test.setCorrectAnswer("6");
            }

            default -> {
                test.setQuestion("Pregunta genèrica");
                test.setOptions("A;B;C;D");
                test.setCorrectAnswer("A");
            }
        }

        return test;
    }
}