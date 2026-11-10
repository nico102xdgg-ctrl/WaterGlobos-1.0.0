package com.mapacheteam.globos;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.mapacheteam.globos.Difficulty.*;

public final class QuestionBank {
    private static final Map<Difficulty, List<Question>> BANK = new EnumMap<>(Difficulty.class);

    private static void add(Difficulty d, String text, int correct1Based, String... o) {
        BANK.get(d).add(new Question(text, o, correct1Based - 1));
    }

    public static List<Question> get(Difficulty d) {
        return BANK.get(d);
    }

    static {
        for (Difficulty d : Difficulty.values()) BANK.put(d, new ArrayList<>());

        // ================= DIFÍCIL =================
        add(HARD, "¿Cuál es el río más largo del mundo?", 2, "Amazonas", "Nilo", "Yangtsé", "Misisipi");
        add(HARD, "¿Capital de Australia?", 1, "Canberra", "Sídney", "Melbourne", "Perth");
        add(HARD, "¿Cordillera más larga del mundo?", 2, "Amazonas", "Andes", "Alpes", "Himalaya");
        add(HARD, "¿País sin salida al mar?", 4, "Perú", "Chile", "Brasil", "Bolivia");
        add(HARD, "¿Cuál es el océano más grande?", 1, "Pacífico", "Atlántico", "Índico", "Ártico");
        add(HARD, "¿Quién pintó la Mona Lisa?", 1, "Leonardo da Vinci", "Picasso", "Van Gogh", "Miguel Ángel");
        add(HARD, "¿Planeta más grande?", 3, "Marte", "Venus", "Júpiter", "Saturno");
        add(HARD, "¿Ave que no vuela?", 4, "Águila", "Paloma", "Gallina", "Pingüino");
        add(HARD, "¿Elemento necesario para vivir?", 4, "Fuego", "Agua", "Tierra", "Aire");
        add(HARD, "¿Cuál es el metal más valioso?", 1, "Oro", "Plata", "Bronce", "Hierro");
        add(HARD, "¿Qué animal es un mamífero marino?", 3, "Perro", "Gato", "Delfín", "Murciélago");
        add(HARD, "¿Cuántos planetas hay en el sistema solar?", 2, "7", "8", "9", "10");
        add(HARD, "¿Planeta más cercano al Sol?", 1, "Mercurio", "Venus", "Marte", "Júpiter");
        add(HARD, "¿Órgano que bombea sangre?", 2, "Pulmón", "Corazón", "Cerebro", "Riñón");
        add(HARD, "¿Qué es el Sol?", 2, "Sol", "Estrella", "Planeta", "Satélite");
        add(HARD, "¿Metal usado en cables eléctricos?", 1, "Cobre", "Oro", "Plata", "Aluminio");

        // ================= MEDIO =================
        add(MEDIUM, "¿Cuál es la capital del Perú?", 1, "Lima", "Cusco", "Arequipa", "Trujillo");
        add(MEDIUM, "¿En qué continente está Egipto?", 2, "Asia", "África", "Europa", "América");
        add(MEDIUM, "¿De qué color es una esmeralda?", 3, "Azul", "Rojo", "Verde", "Amarillo");
        add(MEDIUM, "¿Fórmula del agua?", 1, "H2O", "CO2", "O2", "NaCl");
        add(MEDIUM, "¿País más grande de Sudamérica?", 1, "Brasil", "Argentina", "Perú", "Chile");
        add(MEDIUM, "¿Cuántas patas tiene una araña?", 1, "8", "6", "10", "12");
        add(MEDIUM, "¿Dónde está Francia?", 1, "Europa", "Asia", "África", "Oceanía");
        add(MEDIUM, "¿Animal terrestre más grande?", 3, "Tigre", "León", "Elefante", "Jirafa");
        add(MEDIUM, "¿Qué se obtiene del mar?", 1, "Sal", "Azúcar", "Harina", "Arroz");
        add(MEDIUM, "¿Dónde está China?", 4, "África", "Europa", "América", "Asia");
        add(MEDIUM, "¿Dónde se habla portugués?", 3, "España", "México", "Brasil", "Argentina");
        add(MEDIUM, "¿Dónde está Japón?", 1, "Asia", "Europa", "África", "América");
        add(MEDIUM, "¿Estado sólido del agua?", 1, "Hielo", "Vapor", "Agua líquida", "Nube");
        add(MEDIUM, "¿Dónde están las pirámides?", 1, "Egipto", "Grecia", "Italia", "China");
        add(MEDIUM, "¿Ser vivo que hace fotosíntesis?", 1, "Árbol", "Flor", "Roca", "Animal");
        add(MEDIUM, "¿Cuántos días tiene un año normal?", 1, "365", "366", "364", "360");
        add(MEDIUM, "¿Capital de Italia?", 1, "Roma", "Atenas", "París", "Madrid");

        // ================= FÁCIL =================
        add(EASY, "¿Cuántas horas tiene un día?", 2, "12", "24", "60", "30");
        add(EASY, "¿De qué material se hacen clavos comunes?", 1, "Hierro", "Plástico", "Madera", "Vidrio");
        add(EASY, "¿Qué astro ilumina la Tierra?", 1, "Sol", "Luna", "Marte", "Venus");
        add(EASY, "¿Cuántos lados tiene un triángulo?", 3, "1", "2", "3", "4");
        add(EASY, "¿Qué apaga mejor el fuego?", 1, "Agua", "Aceite", "Alcohol", "Arena");
        add(EASY, "¿Cuántos segundos tiene un minuto?", 2, "30", "60", "90", "100");
        add(EASY, "¿Color de la leche?", 1, "Blanco", "Negro", "Rojo", "Azul");
        add(EASY, "¿Cuántos meses tiene 1 año?", 3, "24", "48", "12", "60");
        add(EASY, "¿Figura sin lados?", 3, "Triángulo", "Cuadrado", "Círculo", "Rectángulo");
        add(EASY, "¿Color del semáforo para detenerse?", 1, "Rojo", "Azul", "Verde", "Blanco");
        add(EASY, "¿Cuántas ruedas tiene una bicicleta?", 1, "2", "4", "6", "8");
        add(EASY, "¿Fruta amarilla y curva?", 2, "Manzana", "Banana", "Pera", "Uva");
        add(EASY, "¿Cuál es más largo?", 1, "Año", "Mes", "Semana", "Día");
        add(EASY, "¿Qué atrae un imán?", 1, "Hierro", "Madera", "Plástico", "Vidrio");
        add(EASY, "¿Cuántos dedos tiene una mano?", 2, "4", "5", "6", "7");
        add(EASY, "¿Con qué se escribe?", 1, "Lápiz", "Regla", "Borrador", "Libro");
        add(EASY, "¿Cuándo sale el sol?", 1, "Día", "Noche", "Madrugada", "Tarde");
    }

    private QuestionBank() {}
}
