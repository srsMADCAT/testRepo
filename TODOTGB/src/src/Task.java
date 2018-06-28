package src;

public class Task {
	public int task_id;
	public String Importance;
	public String task;
	public String status;

	public Task() {
	}

	public String toString() {
		return this.task_id + " " + this.Importance + " " + this.task + " " + this.status;
	}

}
