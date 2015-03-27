package com.jiafan.qirenjongbao.domain;

public class Loginrespose {
	private Results results;
	
	public Results getResults() {
		return results;
	}

	@Override
	public String toString() {
		return "Loginrespose [results=" + results + "]";
	}

	public void setResults(Results results) {
		this.results = results;
	}

	public class Results {
		String error;
		String code;
		String updateAt;
		String sessionToken;
		String username;
		String email;
		String objectId;
		String createdAt;
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getUpdateAt() {
			return updateAt;
		}
		public void setUpdateAt(String updateAt) {
			this.updateAt = updateAt;
		}
		public String getSessionToken() {
			return sessionToken;
		}
		public void setSessionToken(String sessionToken) {
			this.sessionToken = sessionToken;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getObjectId() {
			return objectId;
		}
		public void setObjectId(String objectId) {
			this.objectId = objectId;
		}
		public String getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}
		@Override
		public String toString() {
			return "Results [error=" + error + ", code=" + code + ", updateAt="
					+ updateAt + ", sessionToken=" + sessionToken
					+ ", username=" + username + ", email=" + email
					+ ", objectId=" + objectId + ", createdAt=" + createdAt
					+ "]";
		}
		
		
	}
	
}
