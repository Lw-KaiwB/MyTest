package com.test.retrofit;

/**
 * Created by Administrator on 2018/7/13.
 */

public class ResponseBody {
	/**
	 * code : 200
	 * msg : OK
	 * data : {"id":2,"date":"2016-04-15 03:17:50","author":"怪盗kidou","title":"Retrofit2 测试2","content":"这里是 Retrofit2 Demo 测试服务器2"}
	 * count : 0
	 * page : 0
	 */

	private int code;
	private String msg;
	private DataBean data;
	private int count;
	private int page;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public static class DataBean {
		/**
		 * id : 2
		 * date : 2016-04-15 03:17:50
		 * author : 怪盗kidou
		 * title : Retrofit2 测试2
		 * content : 这里是 Retrofit2 Demo 测试服务器2
		 */

		private int id;
		private String date;
		private String author;
		private String title;
		private String content;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
