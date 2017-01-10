package com.ixhuiyunproject.huiyun.ixconfig.bean;
public class Version {
		public int version_id;
		public String desc;
		public String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getVersion_id() {
			return version_id;
		}

		public void setVersion_id(int version_id) {
			this.version_id = version_id;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public Version(int version_id, String desc, String url) {
			super();
			this.version_id = version_id;
			this.desc = desc;
			this.url = url;
		}

		
	}