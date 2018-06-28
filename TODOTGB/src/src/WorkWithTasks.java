package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

public class WorkWithTasks {
	public static void taskWriter(Task t) {
		try {
			FileWriter fout = new FileWriter("C:/Users/Олег Риснюк/Risn/TODOTGB/src/src/tasks", true);
			fout.write(t.toString() + "\n");
			fout.flush();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getLineNumber() {
		int lineNumber = 0;
		try {
			File f = new File("C:/Users/Олег Риснюк/Risn/TODOTGB/src/src/tasks");
			if (f.exists()) {
				FileReader fr = new FileReader(f);
				LineNumberReader lnr = new LineNumberReader(fr);

				while (lnr.readLine() != null) {
					lineNumber++;
				}

				lnr.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return lineNumber;
	}

	public static void getAllTasks(int lineNumber, Task[] tsk) {
		String s;
		int i = 0;
		try {
			File f = new File("C:/Users/Олег Риснюк/Risn/TODOTGB/src/src/tasks");
			if (f.exists()) {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				while ((s = br.readLine()) != null && i != lineNumber) {
					String temp[] = s.split(" ", 3);
					tsk[i] = new Task();
					tsk[i].task_id = Integer.parseInt(temp[0]);
					tsk[i].Importance = temp[1];
					String tempTask[] = temp[2].split(" ");
					tsk[i].task = "";
					for (int x = 0; x < tempTask.length - 1; x++) {
						tsk[i].task += tempTask[x] + " ";
						System.out.println(tsk[i].task);
					}
					tsk[i].status = tempTask[tempTask.length - 1];
					i++;
				}
				br.close();
				fr.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public static void tasksRewriter(int lineNumber, Task[] tsk) {
		int i = 0;
		try {
			FileWriter fout = new FileWriter("C:/Users/Олег Риснюк/Risn/TODOTGB/src/src/tasks", false);
			while (i < lineNumber) {
				fout.write(tsk[i].task_id + " " + tsk[i].Importance + " " + tsk[i].task + " " + tsk[i].status + "\n");
				fout.flush();
				i++;
			}
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
