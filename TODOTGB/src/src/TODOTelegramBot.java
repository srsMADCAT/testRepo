package src;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TODOTelegramBot extends TelegramLongPollingBot {
	int taskIdTemp = WorkWithTasks.getLineNumber() + 1;
	boolean newTaskPressed = false;
	boolean changeStatus = false;
	boolean deleteTaskPressed = false;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			String messageText = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();
			if (messageText.equals("/start")) {
				SendMessage message = new SendMessage().setChatId(chatId).setText(messageText);
				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if (messageText.equals("/keyboard")) {
				SendMessage message = new SendMessage().setChatId(chatId).setText("��� ��� �������� ���������");
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();
				KeyboardRow row = new KeyboardRow();
				row.add("�� ��������");
				row.add("������ ������");
				row.add("���� ��������");
				keyboard.add(row);
				row = new KeyboardRow();
				row.add("�������� ��������");
				row.add("����������� �� ��������");
				row.add("������ �������");
				keyboard.add(row);
				keyboardMarkup.setKeyboard(keyboard);
				message.setReplyMarkup(keyboardMarkup);
				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if (messageText.equals("�� ��������")) {
				SendMessage msg = new SendMessage().setChatId(chatId).setText("���� �� �������� ��������:");
				try {
					execute(msg);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				tasksToBeDone(update);
			} else if (messageText.equals("���� ��������")) {
				SendMessage msg = new SendMessage().setChatId(chatId)
						.setText("���� �������� \n��������� � �������� ����� �����!");
				newTaskPressed = true;
				try {
					execute(msg);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if (messageText.equals("����������� �� ��������")) {
				SendMessage msg = new SendMessage().setChatId(chatId).setText("��� ���� ��������:");
				try {
					execute(msg);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				allTasks(update);

			} else if (messageText.equals("������ ������")) {
				SendMessage msg = new SendMessage().setChatId(chatId)
						.setText("������ � �������� �� ����� ������ \n*������ ������� (Working/Done/Deleted)");
				changeStatus = true;
				try {
					execute(msg);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}

			} else if (messageText.equals("�������� ��������")) {
				SendMessage msg = new SendMessage().setChatId(chatId).setText("����� � ��������");
				try {
					execute(msg);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				deleteTaskPressed = true;
			} else if (messageText.equals("������ �������")) {
				SendMessage msg = new SendMessage().setChatId(chatId)
						.setText("��� 10 ������� ���������/��������� ��������");
				try {
					execute(msg);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				tasksHistory(update);
			} else if (messageText.equals("/hide")) {
				SendMessage msg = new SendMessage().setChatId(chatId).setText("��������� �������");
				ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
				msg.setReplyMarkup(keyboardMarkup);
				try {
					execute(msg);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if (newTaskPressed == true && update.getMessage().hasText()) {
				System.out.println("Here is flag!" + newTaskPressed);
				newTaskPressed = false;
				newTask(update);

			} else if (changeStatus == true && update.getMessage().hasText()) {
				System.out.println("Here is flag!" + changeStatus);
				changeStatus = false;
				changeTaskStatus(update);

			} else if (deleteTaskPressed == true && update.getMessage().hasText()) {
				System.out.println("Here is flag!" + deleteTaskPressed);
				changeStatus = false;
				deleteTask(update);

			} else {
				SendMessage message = new SendMessage().setChatId(chatId)
						.setText("������� ������� \n�����, �� �� �\'���� �����?");
				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void newTask(Update upd) {
		long chatId = upd.getMessage().getChatId();
		if (upd.hasMessage()) {
			String task_split[];
			String task_text = upd.getMessage().getText();
			task_split = task_text.split(" ", 2);
			Task t = new Task();
			t.task_id = taskIdTemp;
			t.Importance = task_split[0];
			t.task = task_split[1];
			t.status = "Working";
			System.out.println(t.toString());
			taskIdTemp++;
			SendMessage message = new SendMessage().setChatId(chatId).setText("�������� �" + t.task_id + " ������");
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			WorkWithTasks.taskWriter(t);
		}
	}

	public void allTasks(Update upd) {
		long chatId = upd.getMessage().getChatId();
		int i = 0;
		int lineNumber = WorkWithTasks.getLineNumber();
		Task[] t = new Task[lineNumber];
		WorkWithTasks.getAllTasks(lineNumber, t);
		while (i < lineNumber) {
			SendMessage message = new SendMessage().setChatId(chatId)
					.setText("���� �" + t[i].task_id + " " + t[i].Importance + "\n" + t[i].task + "\n" + t[i].status);
			i++;
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		SendMessage message = new SendMessage().setChatId(chatId).setText("������� ������� " + lineNumber);
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void tasksToBeDone(Update upd) {
		long chatId = upd.getMessage().getChatId();
		int i = 0;
		int count = 0;
		int lineNumber = WorkWithTasks.getLineNumber();
		Task[] t = new Task[lineNumber];
		WorkWithTasks.getAllTasks(lineNumber, t);
		while (i < lineNumber) {
			if (!t[i].status.equals("Done") && !t[i].status.equals("Deleted")) {
				SendMessage message = new SendMessage().setChatId(chatId).setText(
						"���� �" + t[i].task_id + " " + t[i].Importance + "\n" + t[i].task + "\n" + t[i].status);
				count++;
				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
			i++;
		}
		SendMessage message = new SendMessage().setChatId(chatId).setText("������� ������� " + count);
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

	}

	public void changeTaskStatus(Update upd) {
		long chatId = upd.getMessage().getChatId();
		int i = 0;
		int lineNumber = WorkWithTasks.getLineNumber();
		Task[] t = new Task[lineNumber];
		WorkWithTasks.getAllTasks(lineNumber, t);
		String changeText = upd.getMessage().getText();
		String changeSplit[];
		changeSplit = changeText.split(" ");
		int taskChangeId = Integer.parseInt(changeSplit[0]);
		String newStatus = changeSplit[1];
		while (i < lineNumber) {
			if (t[i].task_id == taskChangeId) {
				t[i].status = newStatus;
			}
			i++;
		}
		WorkWithTasks.tasksRewriter(lineNumber, t);
		SendMessage message = new SendMessage().setChatId(chatId)
				.setText("������ �������� �" + taskChangeId + " ������ ������ �� " + newStatus);
		try {
			execute(message);
		} catch (TelegramApiException tae) {
			tae.printStackTrace();
		}
	}

	public void tasksHistory(Update upd) {
		long chatId = upd.getMessage().getChatId();
		int count = 0;
		int lineNumber = WorkWithTasks.getLineNumber();
		int i = lineNumber - 1;
		Task[] t = new Task[lineNumber];
		WorkWithTasks.getAllTasks(lineNumber, t);
		while (i > 0 && count < 10) {
			if (t[i].status.equals("Done") || t[i].status.equals("Deleted")) {
				SendMessage message = new SendMessage().setChatId(chatId).setText(
						"���� �" + t[i].task_id + " " + t[i].Importance + "\n" + t[i].task + "\n" + t[i].status);
				count++;
				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
			i--;
		}
	}

	public void deleteTask(Update upd) {
		long chatId = upd.getMessage().getChatId();
		String taskIdToDelete = upd.getMessage().getText();
		int lineNumber = WorkWithTasks.getLineNumber();
		int i = 0;
		Task[] t = new Task[lineNumber];
		WorkWithTasks.getAllTasks(lineNumber, t);
		while (i < lineNumber) {
			if (t[i].task_id == Integer.parseInt(taskIdToDelete)) {
				t[i].status = "Deleted";
			}
			i++;
		}
		WorkWithTasks.tasksRewriter(lineNumber, t);
		SendMessage message = new SendMessage().setChatId(chatId)
				.setText("�������� �" + taskIdToDelete + " ������ ��������");
		try {
			execute(message);
		} catch (TelegramApiException tae) {
			tae.printStackTrace();
		}
	}

	@Override
	public String getBotUsername() {
		return "TODOMADCAT_bot";
	}

	@Override
	public String getBotToken() {
		return "586216935:AAH4nEEMQ5-Wg7ZTOPqawaJ2V8CbJtsu04M";
	}
}
