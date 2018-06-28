package src;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {
	public static void main(String[] args) {
		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();

		try {
			botsApi.registerBot(new TODOTelegramBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		System.out.println("TODOMADCAT_Bot successfully started!");
	}
}
