package learn.course.springboot.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LUCA
 *
 * Applicazione Server PASSAGGI FERMATE. 
 * Questa applicazione consiste di un server che, 
 * preso in input il numero di una fermata, restituisce i prossimi passaggi degli autobus in quella fermata
 * 
 * 
 * Features:
 * 
 * 1 - SpringBoot
 * 2 - Conversione automatica oggetti in JSON
 * 3 - Servizi REST parametrici
 * 4 - (Test JUnit)
 */


@SpringBootApplication
public class SpringBoot1DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot1DemoApplication.class, args);
	}
}
