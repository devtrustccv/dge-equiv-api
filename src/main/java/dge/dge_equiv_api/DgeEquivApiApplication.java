package dge.dge_equiv_api;

import dge.dge_equiv_api.Utils.AESUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DgeEquivApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DgeEquivApiApplication.class, args);
//		String encrypted = AESUtil.decrypt("6WVVKrZL73rehj0pir1Lgw");
//		System.out.println("Criptografado: " + encrypted);

	}



}
