package com.yang.miniloan.login;

public class DummyLoginServiceImpl implements LoginService {

	@Override
	public boolean login(Credential credential) {
		if (credential instanceof SimpleCredential) {
			SimpleCredential sc = (SimpleCredential) credential;
			String account = sc.getAccount();
			String pwd = sc.getPassword();
			if (account == null || pwd == null) {
				// TODO
				throw new RuntimeException("Please provide crential");
			}
			return account.equalsIgnoreCase(pwd);

		} else {
			throw new RuntimeException("TO BE IMPLEMENTED");

		}
	}

}
