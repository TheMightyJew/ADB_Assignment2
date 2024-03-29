package hib;
// Generated Dec 17, 2019 6:48:15 PM by Hibernate Tools 4.3.5.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Mediaitems generated by hbm2java
 */
public class Mediaitems implements java.io.Serializable {

	private int mid;
	private String title;
	private Short prodYear;
	private Short titleLength;
	private Set histories = new HashSet(0);
	private Set similaritiesForMid1 = new HashSet(0);
	private Set similaritiesForMid2 = new HashSet(0);

	public Mediaitems() {
	}

	public Mediaitems(int mid) {
		this.mid = mid;
	}

	public Mediaitems(int mid, String title, Short prodYear, Short titleLength, Set histories, Set similaritiesForMid1,
			Set similaritiesForMid2) {
		this.mid = mid;
		this.title = title;
		this.prodYear = prodYear;
		this.titleLength = titleLength;
		this.histories = histories;
		this.similaritiesForMid1 = similaritiesForMid1;
		this.similaritiesForMid2 = similaritiesForMid2;
	}

	public int getMid() {
		return this.mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Short getProdYear() {
		return this.prodYear;
	}

	public void setProdYear(Short prodYear) {
		this.prodYear = prodYear;
	}

	public Short getTitleLength() {
		return this.titleLength;
	}

	public void setTitleLength(Short titleLength) {
		this.titleLength = titleLength;
	}

	public Set getHistories() {
		return this.histories;
	}

	public void setHistories(Set histories) {
		this.histories = histories;
	}

	public Set getSimilaritiesForMid1() {
		return this.similaritiesForMid1;
	}

	public void setSimilaritiesForMid1(Set similaritiesForMid1) {
		this.similaritiesForMid1 = similaritiesForMid1;
	}

	public Set getSimilaritiesForMid2() {
		return this.similaritiesForMid2;
	}

	public void setSimilaritiesForMid2(Set similaritiesForMid2) {
		this.similaritiesForMid2 = similaritiesForMid2;
	}

}
