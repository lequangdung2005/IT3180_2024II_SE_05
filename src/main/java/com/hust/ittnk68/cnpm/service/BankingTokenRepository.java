package com.hust.ittnk68.cnpm.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class BankingTokenRepository {

	private final Set<String> arrivedSet;
	private final Set<String> resolvedSet;

	public BankingTokenRepository()
	{
		arrivedSet = new HashSet<>();
		resolvedSet = new HashSet<>();
	}

	public boolean tokenArrive (String token)
	{
		return arrivedSet.add (token);
	}

	public boolean resolveToken (String token)
	{
		if (! arrivedSet.contains (token))
			return false;
		return resolvedSet.add (token);
	}
	
}
