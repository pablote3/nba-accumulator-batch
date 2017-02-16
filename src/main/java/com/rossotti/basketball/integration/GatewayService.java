package com.rossotti.basketball.integration;

import com.rossotti.basketball.business.model.StandingsBusiness;

public interface GatewayService {
	StandingsBusiness processGames(ServiceProperties serviceProperties);
}