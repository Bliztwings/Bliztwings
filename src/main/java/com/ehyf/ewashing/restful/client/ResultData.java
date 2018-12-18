package com.ehyf.ewashing.restful.client;

public class ResultData <T extends Object>{
		
		private String resCode;
		private String msg;
		private T obj;
		
		public ResultData() {
			super();
		}

		public ResultData(String resCode, String msg, T obj) {
			super();
			this.resCode = resCode;
			this.msg = msg;
			this.obj = obj;
		}

		public String getResCode() {
			return resCode;
		}

		public void setResCode(String resCode) {
			this.resCode = resCode;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public T getObj() {
			return obj;
		}

		public void setObj(T obj) {
			this.obj = obj;
		}
		
		public void setResult(String resCode,String msg,T obj) {
	        this.resCode = resCode;
	        this.msg = msg;
			this.obj = obj;
		}
		
}
