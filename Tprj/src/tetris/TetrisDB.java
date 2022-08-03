package tetris;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TetrisDB {
	public static ResultSet rs;

	public static Connection makeConnection() {
		String url = "jdbc:oracle:thin:@10.30.3.95:1521:orcl";
		String id = "c##1801110";
		String password = "p1801110";
		Connection con = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver Load Completed");
			con = DriverManager.getConnection(url, id, password);
			System.out.println("Database Connection Completed\n");

		} catch (ClassNotFoundException err) {
			System.out.println("Driver not Found");
		} catch (SQLException err) {
			err.printStackTrace();
			System.out.println("Database Connection Failed\n");
		}

		return con;
	}
	
	public static void insertDB(Statement stmt) {
		
		try {
			// no(number), time(varchar2), score(number)
			String sql = "";
			
			sql = "insert into SCORE_BOARD(time, score) " 
					+ "VALUES('" + GameStats.times + "', " + GameStats.gameScore + ")";
			//System.out.println(sql); //입력된 sql 확인

			if (stmt.executeUpdate(sql) == 1) {
				System.out.println("insert 쿼리 성공.");
			}
			System.out.println("\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void display(Statement stmt) {
		try {
			String sql = "select TIME, SCORE from SCORE_BOARD order by TIME";
			// System.out.println(sql);
			rs = stmt.executeQuery(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteDB(Statement stmt) {
		try {
			String sql = "";

			sql = "truncate table SCORE_BOARD";

			int check = stmt.executeUpdate(sql);
			if (check == 1) {
				System.out.println("데이터 정리 완료");
			}
			System.out.println("\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
