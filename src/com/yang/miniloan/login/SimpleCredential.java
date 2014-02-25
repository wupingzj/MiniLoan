package com.yang.miniloan.login;

public class SimpleCredential extends Credential {
	private String account;
	private String password;

	public SimpleCredential(String account, String password) {
		super();
		this.account = account;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccount() {
		return account;
	}

}
