package datadixit.limsbi.action;

import java.util.Calendar;


public class Test0 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());

			String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);
			String time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);

			System.out.println(date + "-" + time);			

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
        
		
	}

}
