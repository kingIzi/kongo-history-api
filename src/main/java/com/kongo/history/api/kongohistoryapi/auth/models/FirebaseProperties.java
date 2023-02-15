package com.kongo.history.api.kongohistoryapi.auth.models;

import lombok.Data;

@Data
public class FirebaseProperties {

	int sessionExpiryInDays;
	String databaseUrl;
	boolean enableStrictServerSession;
	boolean enableCheckSessionRevoked;
	boolean enableLogoutEverywhere;

}
