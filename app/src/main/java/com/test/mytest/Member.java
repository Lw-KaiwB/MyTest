package com.test.mytest;

import com.test.annotation.DBClass;

/**
 * Created by Administrator on 2018/7/12.
 */
@DBClass.DBTable(name = "Member")
public class Member extends BaseMember{
	@DBClass.SQLString(name = "ID", value = 50, constraint = @DBClass.Constraints(primaryKey = true))
	private String id;
	@DBClass.SQLString(name = "NAME", value = 30)
	private String name;
	@DBClass.SQLInteger(name = "AGE")
	private String age;
	@DBClass.SQLString(name = "DESCRIPTION", value = 50, constraint = @DBClass.Constraints(allowNull = true))
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
