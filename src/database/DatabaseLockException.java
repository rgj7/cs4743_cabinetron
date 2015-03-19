package database;

@SuppressWarnings("serial")
public class DatabaseLockException extends Exception {
	public DatabaseLockException(String s) {
		super(s);
	}
}
