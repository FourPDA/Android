package four.pda.client.testapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

import four.pda.client.FourPdaClient;
import four.pda.client.model.Captcha;

/**
 * Created by asavinova on 09/02/16.
 */
public class AuthTest {

	private static final Logger L = LoggerFactory.getLogger(AuthTest.class);

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		FourPdaClient client = new FourPdaClient();
		FourPdaClient.LoginParams params = client.new LoginParams();

		Captcha captcha = client.getCaptcha();
		params.setCaptchaTime(captcha.getTime());
		params.setCaptchaSig(captcha.getSig());
		System.out.println("Captcha url: " + captcha.getUrl());

		System.out.print("Captcha: ");
		String captchaValue = scanner.nextLine();
		params.setCaptcha(captchaValue);

		System.out.print("Login: ");
		String login = scanner.nextLine();
		params.setLogin(login);

		System.out.print("Password: ");
		String password = scanner.nextLine();
		params.setPassword(password);

		client.login(params);
	}

}
