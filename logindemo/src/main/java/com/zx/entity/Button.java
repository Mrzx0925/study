package com.zx.entity;

public class Button {
	private int id;
	private int userid;
	private int add;
	private int delete;
	private int update;
	private int query;
	private int in;
	private int out;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getAdd() {
		return add;
	}
	public void setAdd(int add) {
		this.add = add;
	}
	public int getDelete() {
		return delete;
	}
	public void setDelete(int delete) {
		this.delete = delete;
	}
	public int getUpdate() {
		return update;
	}
	public void setUpdate(int update) {
		this.update = update;
	}
	public int getQuery() {
		return query;
	}
	public void setQuery(int query) {
		this.query = query;
	}
	public int getIn() {
		return in;
	}
	public void setIn(int in) {
		this.in = in;
	}
	public int getOut() {
		return out;
	}
	public void setOut(int out) {
		this.out = out;
	}
	@Override
	public String toString() {
		return "Button [id=" + id + ", userid=" + userid + ", add=" + add + ", delete=" + delete + ", update=" + update
				+ ", query=" + query + ", in=" + in + ", out=" + out + "]";
	}
	
	
	
}
