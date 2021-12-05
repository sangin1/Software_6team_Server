package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import VO.duesVO;
import VO.loginVO;

public class serverThread extends Thread{
	private Socket socket;
	BufferedReader in;
	PrintWriter out;
	ObjectOutputStream ob_out;
	ObjectInputStream ob_in;
	String id,pw,date,a;
	loginVO login;
	duesVO dues;
	public serverThread(Socket socket) {
		this.socket = socket; 
	}
	@SuppressWarnings("unlikely-arg-type")
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);  
			ob_out = new ObjectOutputStream(socket.getOutputStream()); 
			ob_in= new ObjectInputStream(socket.getInputStream());
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e1) { 
				e1.printStackTrace();
			}
			while(true) {
				a = in.readLine(); 				
				if(a == null) {
					break;
				}
				String[] input = a.split("--");
				if(input[0].equals("checkIdPw")==true){
					try(Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/duesdb","root","1234");
						Statement stmt = conn.createStatement();
						 
						ResultSet rs = stmt.executeQuery(String.format("select * from login where id='%s' and pw='%s'",
								input[1],input[2]));
					){
						if(rs.next()){
							out.println("1-"+rs.getString("login_index")); 
							out.flush();
						}else {
							out.println("0"); 
							out.flush();
						}

					}catch(Exception e){
						e.printStackTrace();
					}	
					continue;
				}else if(input[0].equals("checkId")==true) {
					try(Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/duesdb","root","1234");
						Statement stmt = conn.createStatement();
						 
						ResultSet rs = stmt.executeQuery(String.format("select * from login where id='%s'",
								input[1]));
					){
						if(rs.next()){
							out.println("0"); 
							out.flush();
						}else {
							out.println("1"); 
							out.flush();
						}

					}catch(Exception e){
						e.printStackTrace();
					}	
					continue;
					
				}else if(input[0].equals("createUser")==true) { 
					try(Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/duesdb","root","1234");
						Statement stmt = conn.createStatement();
							
						//PreparedStatement pstmt = conn.prepareStatement();
					){ 		
						stmt.executeUpdate(String.format("insert into login(id, pw) value ('%s', '%s')",
								input[1],input[2]));
						System.out.println(String.format("insert into login(id, pw) value ('%s', '%s')",
								input[1],input[2]));
						//pstmt.executeUpdate();
					}catch(Exception e){
						e.printStackTrace();
					}
					continue;
				}else if(input[0].equals("duesSearch")==true) {
					String[] dateList = input[2].split("-");
					List<duesVO> duesList = new ArrayList<duesVO>();
					
					try(Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/duesdb","root","1234");
						Statement stmt = conn.createStatement();
						 
						ResultSet rs = stmt.executeQuery(String.format("select * from dues where id_index= %s and "
								+ "dues_date <= STR_TO_DATE('%s-%s-31', '%%Y-%%m-%%d') and dues_date >= STR_TO_DATE('%s-%s-01', '%%Y-%%m-%%d')",
								input[1],dateList[0],dateList[1],dateList[0],dateList[1]));
					){
						while(rs.next()){
							dues = new duesVO(rs.getString("dues_index"),rs.getString("dues_name"),
									rs.getString("dues"),rs.getString("dues_date"),rs.getString("login_index"));
							duesList.add(dues);
						}
						ob_out.writeObject(duesList);
					}catch(Exception e){
						e.printStackTrace();
					}	
					continue;
				}else if(input[0].equals("duesAdd")==true) {  
						try(Connection conn = DriverManager.getConnection(
								"jdbc:mysql://localhost:3306/duesdb","root","1234");
							Statement stmt = conn.createStatement();
						){ 		
							stmt.executeUpdate(String.format("insert into dues(dues_name, dues,dues_date,login_index) value ('%s', %s,'%s',%s)",
									input[2],input[3],input[4],input[1]));
						}catch(Exception e){
							e.printStackTrace();
						}
						continue;
				}else if(input[0].equals("duesDel")==true) {
					try {
						dues = (duesVO)ob_in.readObject();
						} catch (ClassNotFoundException e) { 
							e.printStackTrace();
						} catch (IOException e) { 
							e.printStackTrace();
						}
						try(Connection conn = DriverManager.getConnection(
								"jdbc:mysql://localhost:3306/duesdb","root","1234");
							Statement stmt = conn.createStatement();
						){ 		
							stmt.execute(String.format("delete from dues where dues_index = %s",
									input[1]));
						}catch(Exception e){
							e.printStackTrace();
						}
						continue;
				}else if(input[0].equals("duesUpdate")==true) {
					
						try(Connection conn = DriverManager.getConnection(
								"jdbc:mysql://localhost:3306/duesdb","root","1234");
							Statement stmt = conn.createStatement();
							PreparedStatement pstmt = conn.prepareStatement(String.format("update dues set dues_name = '%s', dues = %s, dues_date = '%s' where dues_index = %s",
										input[2],input[3],input[4],input[1]));
						){ 		
							pstmt.executeUpdate(); 
						}catch(Exception e){
							e.printStackTrace();
						}
						continue;
				}
					
			
			
			}
		}catch(IOException e) { 
			System.out.println("클라이언트 처리실패"+e);
		}finally {
			try {
				socket.close();
			}catch(IOException e) {
				System.out.println("소켓종료오류 "+e);
			} 
		}
		
	}
}
