package dge.dge_equiv_api;

import dge.dge_equiv_api.Utils.AESUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class DgeEquivApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DgeEquivApiApplication.class, args);

		System.out.println(AESUtil.encrypt(String.valueOf(108)));

	}



}
