package code;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hib.Administrators;
import hib.History;
import hib.HistoryId;
import hib.Loginlog;
import hib.LoginlogId;
import hib.Mediaitems;
import hib.Users;

public class Assignment
{


	public static void main(String[] args) 
	{
		System.out.println(isExistUsername("TheMightyJew"));
		System.out.println(isExistUsername("TheMightyjew"));
	}

	/*
	 * 1.2.3
	 */
	public static boolean isExistUsername (String username)
	{
		Session session=null;
		int usersAmount=0;
		try
		{
			session=HibernateUtil.currentSession();
			String hql="select username from Users users where users.username='"+username+"'";
			Query query=session.createQuery(hql);
			usersAmount=query.list().size();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		return usersAmount>0;
	}
	/*
	 * 1.2.4
	 */
	public static String insertUser(String username, String password, String
			first_name, String last_name, String day_of_birth, String
			month_of_birth, String year_of_birth)
	{
		if(Integer.parseInt(year_of_birth)<0 ||Integer.parseInt(year_of_birth)>2017||Integer.parseInt(day_of_birth)<1||Integer.parseInt(day_of_birth)>31)
			return null;
		if(isExistUsername(username))
			return null;
		Session session=null;
		Users user=new Users();
		Date d=new Date();
		try
		{
			session=HibernateUtil.currentSession();
			if(isMonthValidName(month_of_birth))
			{
				DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
				d=format.parse(month_of_birth+" "+day_of_birth+", "+year_of_birth);
			}
			else if(isMonthValidNumber(month_of_birth))
			{
				if(month_of_birth.length()==1)
					month_of_birth="0"+month_of_birth;
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				d=format.parse(day_of_birth+"/"+month_of_birth+"/"+year_of_birth);
			}
			else //invalid month
				return null;
			user.setDateOfBirth(getTimestamp(d));
			user.setUsername(username);
			user.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
			user.setFirstName(first_name);
			user.setLastName(last_name);
			user.setPassword(password);
			Transaction tr=session.beginTransaction();
			session.saveOrUpdate(user);
			tr.commit();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		return ""+user.getUserid();
	}
	/*
	 * 1.2.5
	 * if there is less than top_n items then return all the items in the table
	 */
	public static List<Mediaitems> getTopNItems (int top_n)
	{
		Session session=null;
		List<Mediaitems> top_nMediaitems=null;
		try
		{
			session=HibernateUtil.currentSession();
			String hql="select items from Mediaitems items where rownum<="+top_n+" order by mid";
			Query query=session.createQuery(hql);
			top_nMediaitems=query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		return top_nMediaitems;
	}
	/*
	 * 1.2.6
	 */
	public static String validateUser (String username, String password)
	{
		Session session=null;
		List<Users> userFromQuery=null;
		try
		{
			session=HibernateUtil.currentSession();
			String hql="select users from Users users where users.username='"+username+"' and users.password='"+password+"'";
			Query query=session.createQuery(hql);
			userFromQuery=query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		if(userFromQuery.size()>0)
			return userFromQuery.get(0).getUserid()+"";
		return null;
	}
	/*
	 * 1.2.7
	 */
	public static String validateAdministrator (String username, String password)
	{
		Session session=null;
		List<Administrators> adminsFromQuery=null;
		try
		{
			session=HibernateUtil.currentSession();
			String hql="select admins from Administrators admins where admins.username='"+username+"' and admins.password='"+password+"'";
			Query query=session.createQuery(hql);
			adminsFromQuery=query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		if(adminsFromQuery.size()>0)
			return adminsFromQuery.get(0).getAdminid()+"";
		return null;
	}
	/*
	 * 1.2.8
	 */
	public static void insertToHistory (String userid, String mid)
	{//TODO check if userid exists in Users, check if mid exists in Mediaitems
		Session session=null;
		List<Users> user=null;
		List<Mediaitems> item= null;
		History his=new History();
		//get user
		try 
		{
			session=HibernateUtil.currentSession();
			String hql="select user from Users user where user.userid='"+userid+"'";
			Query query=session.createQuery(hql);
			user=query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		if(user==null||user.size()==0)
			return;
		//get item
		try 
		{
			session=HibernateUtil.currentSession();
			String hql="select mitems from Mediaitems mitems where mid='"+mid+"'";
			Query query=session.createQuery(hql);
			item= query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		if(item==null||item.size()==0)
			return;
		//set history
		try
		{
			his.setId(new HistoryId(Integer.parseInt(userid),Integer.parseInt(mid),new Timestamp(System.currentTimeMillis())));
			session=HibernateUtil.currentSession();
			Transaction tr=session.beginTransaction();
			session.saveOrUpdate(his);
			tr.commit();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
	}
	/*
	 * 1.2.9
	 */
	public static List<?> getHistory (String userid)
	{
		Session session = null;
		List<?> userHistoryWithTitle=null;
		try 
		{
			session=HibernateUtil.currentSession();
			String hql="select h.mediaitems.title, h.id.viewtime from History h where h.id.userid='"+userid+"' order by viewtime desc" ;
			Query query=session.createQuery(hql);
			userHistoryWithTitle=query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		return userHistoryWithTitle;
		
	}
	/*
	 * 1.2.10
	 */
	public static void insertToLog (String userid)
	{
		Session session=null;
		List<Users> user=null;
		Loginlog log=new Loginlog();
		//get user
		try 
		{
			session=HibernateUtil.currentSession();
			String hql="select user from Users user where user.userid='"+userid+"'";
			Query query=session.createQuery(hql);
			user=query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		if(user==null||user.size()==0)
			return;
		//set log
		try
		{
			log.setId(new LoginlogId(Integer.parseInt(userid),new Timestamp(System.currentTimeMillis())));
			session=HibernateUtil.currentSession();
			Transaction tr=session.beginTransaction();
			session.saveOrUpdate(log);
			tr.commit();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
	}
	/*
	 * 1.2.11
	 */
	public static int getNumberOfRegistredUsers(int n)
	{
		long toReturn=0;
		Session session=null;
		List<Long> count=null;
		try 
		{
			session=HibernateUtil.currentSession();
			String hql="select count(users.username) from Users users where users.registrationDate > sysdate-"+n;
			Query query=session.createQuery(hql);
			count=query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		if(count.size()>0)
			toReturn=count.get(0);
		return (int)toReturn;
	}
	/*
	 * 1.2.12
	 */
	public static List<Users> getUsers ()
	{
		Session session=null;
		List<Users> usersFromQuery=null;
		try
		{
			session=HibernateUtil.currentSession();
			String hql="select users from Users users";
			Query query=session.createQuery(hql);
			usersFromQuery=query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		return usersFromQuery;
	}
	/*
	 * 1.2.13
	 */
	public static Users getUser (String userid)
	{
		Session session=null;
		List<Users> userFromQuery=null;
		try
		{
			session=HibernateUtil.currentSession();
			String hql="select user from Users user where user.userid='"+userid+"'";
			Query query=session.createQuery(hql);
			userFromQuery=query.list();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtil.closeSession();
		}
		if(userFromQuery.size()>0)
			return userFromQuery.get(0);
		return null;
	}

	/*
	 * help functions
	 */
	private static boolean isMonthValidName(String mName)
	{
		Set<String>MonthsNames=new HashSet<String>();
		MonthsNames.add("January");
		MonthsNames.add("February");
		MonthsNames.add("March");
		MonthsNames.add("April");
		MonthsNames.add("May");
		MonthsNames.add("June");
		MonthsNames.add("July");
		MonthsNames.add("August");
		MonthsNames.add("September");
		MonthsNames.add("October");
		MonthsNames.add("November");
		MonthsNames.add("December");
		return MonthsNames.contains(mName);
	}
	private static boolean isMonthValidNumber(String mNumber)
	{
		Set<String>MonthsNumbers=new HashSet<String>();
		for(int i=1;i<10;i++)
		{
			MonthsNumbers.add(""+i);
			MonthsNumbers.add("0"+i);
		}
		for(int i=10;i<=12;i++)
			MonthsNumbers.add(""+i);
		return MonthsNumbers.contains(mNumber);
	}
	private static Timestamp getTimestamp(Date date)
	{
		return date == null ? null : new java.sql.Timestamp(date.getTime());
	}
}
