package code;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

public class Assignment {

    /*public static void main(String[] args) {
        if (isExistUsername("TheMightyJew") == false)
            System.out.println(false);
        else if (isExistUsername("TheMightyJewwwwwwwwwwww") == true)
            System.out.println(false);
        else if (insertUser("TheMightyJew", "123", "a", "a", "17", "08", "1998")  != null)
            System.out.println(false);
        else if (insertUser("TheMightyJewwwwwwwwwww", "123", "a", "a", "17", "08", "1998")  == null)
            System.out.println(false);
        else if (getTopNItems(2).size() != 2)
            System.out.println(false);
        else if (validateUser("TheMightyJew","123").equals("Not Found"))
            System.out.println(false);
        else if (validateUser("TheMightyJew","1234").equals("Not Found")==false)
            System.out.println(false);
        else if (validateAdministrator("Jesus","123").equals("Not Found"))
            System.out.println(false);
        else if (validateAdministrator("Jesus","1234").equals("Not Found")==false)
            System.out.println(false);
        else if (getHistory("1").size() == 0)
            System.out.println(false);
        else if (getNumberOfRegistredUsers(2) == 0)
            System.out.println(false);
        else if (getUsers().size() == 0)
            System.out.println(false);
        else if (getUser("1").getUsername().equals("TheMightyJew")==false)
            System.out.println(false);
        else
            System.out.println(true);
        insertToHistory("1","1");
        insertToLog("1");
    }*/

    /*
     * 1.2.5
     */
    public static boolean isExistUsername(String username) {
        Session session = null;
        boolean usernameExists = false;
        try {
            session = HibernateUtil.currentSession();
            String hql = "select username from Users users where users.username='" + username + "'";
            Query query = session.createQuery(hql);
            usernameExists = query.list().size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
        return usernameExists;
    }

	private static boolean isExistUserID(String id) {
		return (getUser(id)!=null);
	}

    /*
     * 1.2.6
     */
    public static String insertUser(String username, String password, String
            first_name, String last_name, String day_of_birth, String
                                            month_of_birth, String year_of_birth) {
        if (isExistUsername(username))
            return null;
        Session session = null;
        Users user = new Users();
        Date date = new Date();
        try {
            session = HibernateUtil.currentSession();
            if (isRealMonthID(month_of_birth)) {
                if (month_of_birth.length() == 1)
                    month_of_birth = "0" + month_of_birth;
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                date = format.parse(day_of_birth + "/" + month_of_birth + "/" + year_of_birth);
            } else //invalid
                return null;
            user.setDateOfBirth(toTimestamp(date));
            user.setUsername(username);
            user.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
            user.setFirstName(first_name);
            user.setLastName(last_name);
            user.setPassword(password);
            Transaction tr = session.beginTransaction();
            session.saveOrUpdate(user);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
        return "" + user.getUserid();
    }

    /*
     * 1.2.7
     * if there is less than top_n items then return all the items in the table
     */
    public static List<Mediaitems> getTopNItems(int top_n) {
        Session session = null;
        List<Mediaitems> top_nMediaitems = null;
        try {
            session = HibernateUtil.currentSession();
            String hql = "select items from Mediaitems items where rownum<=" + top_n + " order by mid desc";
            Query query = session.createQuery(hql);
            top_nMediaitems = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
        return top_nMediaitems;
    }

    /*
     * 1.2.8
     */
    public static String validateUser(String username, String password) {
        Session session = null;
        List<Users> userFromQuery = null;
        try {
            session = HibernateUtil.currentSession();
            String hql = "select users from Users users where users.username='" + username + "' and users.password='" + password + "'";
            Query query = session.createQuery(hql);
            userFromQuery = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
        if (userFromQuery.size() > 0)
            return userFromQuery.get(0).getUserid() + "";
        return "Not Found";
    }

    /*
     * 1.2.9
     */
    public static String validateAdministrator(String username, String password) {
        Session session = null;
        List<Administrators> adminsFromQuery = null;
        try {
            session = HibernateUtil.currentSession();
            String hql = "select admins from Administrators admins where admins.username='" + username + "' and admins.password='" + password + "'";
            Query query = session.createQuery(hql);
            adminsFromQuery = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
        if (adminsFromQuery.size() > 0)
            return adminsFromQuery.get(0).getAdminid() + "";
        return "Not Found";
    }

    /*
     * 1.2.10
     */
    public static void insertToHistory(String userid, String mid) {
        Session session = null;
        List<Mediaitems> item = null;
        History his = new History();
        //check that both mid and user exists
        if (isExistUserID(userid)==false || midExists(mid) == false)
            return;
		//set history
        try {
            HistoryId newHist = new HistoryId(Integer.parseInt(userid), Integer.parseInt(mid), new Timestamp(System.currentTimeMillis()));
            his.setId(newHist);
            session = HibernateUtil.currentSession();
            Transaction tr = session.beginTransaction();
            session.saveOrUpdate(his);
            tr.commit();
            System.out.println("The insertion to history table was successful " + newHist.getViewtime());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
    }

	private static boolean midExists(String mid) {
		Session session = null;
		List<Mediaitems> item = null;
		try {
			session = HibernateUtil.currentSession();
			String hql = "select mitems from Mediaitems mitems where mid='" + mid + "'";
			Query query = session.createQuery(hql);
			item = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		if (item == null || item.size() == 0)
			return false;
		return true;
	}

	/*
     * 1.2.11
     */
    public static List<Object[]> getHistory(String userid) {
        Session session = null;
        List<Object[]> userHistoryWithTitle = null;
        try {
            session = HibernateUtil.currentSession();
            String hql = "select h.mediaitems.title, h.id.viewtime from History h where h.id.userid='" + userid + "' order by viewtime";
            Query query = session.createQuery(hql);
            userHistoryWithTitle = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
        return userHistoryWithTitle;

    }

    /*
     * 1.2.12
     */
    public static void insertToLog(String userid) {
        Session session = null;
        Loginlog log = new Loginlog();
        //check if user exists
        if (isExistUserID(userid)==false) {
            return;
        }
        //set log
        try {
            LoginlogId newLoginlogId = new LoginlogId(Integer.parseInt(userid), new Timestamp(System.currentTimeMillis()));
            log.setId(newLoginlogId);
            session = HibernateUtil.currentSession();
            Transaction tr = session.beginTransaction();
            session.saveOrUpdate(log);
            tr.commit();
            System.out.println("The insertion to log table was successful " + newLoginlogId.getLogintime());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /*
     * 1.2.13
     */
    public static int getNumberOfRegistredUsers(int n) {
        long toReturn = 0;
        Session session = null;
        List<Long> count = null;
        try {
            session = HibernateUtil.currentSession();
            String hql = "select count(users.username) from Users users where users.registrationDate > sysdate-" + n;
            Query query = session.createQuery(hql);
            count = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
        if (count.size() > 0)
            toReturn = count.get(0);
        return (int) toReturn;
    }

    /*
     * 1.2.14
     */
    public static List<Users> getUsers() {
        Session session = null;
        List<Users> usersFromQuery = null;
        try {
            session = HibernateUtil.currentSession();
            String hql = "select users from Users users";
            Query query = session.createQuery(hql);
            usersFromQuery = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
        return usersFromQuery;
    }

    /*
     * 1.2.15
     */
    public static Users getUser(String userid) {
        Session session = null;
        List<Users> userFromQuery = null;
        try {
            session = HibernateUtil.currentSession();
            String hql = "select user from Users user where user.userid='" + userid + "'";
            Query query = session.createQuery(hql);
            userFromQuery = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
        if (userFromQuery.size() > 0)
            return userFromQuery.get(0);
        return null;
    }

    private static boolean isRealMonthID(String mNumber) {
        try {
            int num = Integer.parseInt(mNumber);
            if (num >= 1 && num <= 12) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static Timestamp toTimestamp(Date date) {
        return date == null ? null : new java.sql.Timestamp(date.getTime());
    }
}
