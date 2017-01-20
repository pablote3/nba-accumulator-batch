insert into team VALUES(1, 'CHI', 'Chicago', 'East', 'Central', 'Chicago', '2009-07-01', 'Chicago Zephyr''s', 'Zephyrs', 'Chicago Coliseum', 'IL', 'chicago-zephyr''s', '9999-12-31');
insert into team VALUES(2, 'HAR', 'Harlem', 'East', 'Atlantic', 'Harlem', '2009-07-01', 'Harlem Globetrotter''s', 'Globetrotter''s', 'Rucker Park', 'NY', 'harlem-globetrotter''s', '2010-06-30');
insert into team VALUES(3, 'STL', 'St. Louis', 'East', 'Southwest', 'St. Louis', '2009-07-01', 'St. Louis Bomber''s', 'Bomber''s', 'St. Louis Arena', 'MO', 'st-louis-bomber''s', '2010-06-30');
insert into team VALUES(4, 'SAL', 'Salinas', 'West', 'Pacific', 'Salinas', '2010-07-01', 'Salinas Cowboys', 'Cowboys', 'Salinas High School', 'CA', 'salinas-cowboys', '9999-12-31');
insert into team VALUES(5, 'STL', 'Baltimore', 'East', 'Southeast', 'Baltimore', '2005-07-01', 'Baltimore Bullets', 'Bullets', 'Baltimore Coliseum', 'MD', 'baltimore-bullets', '2006-06-30');
insert into team VALUES(6, 'CLE', 'Rebels', 'East', 'Central', 'Cleveland', '2010-07-01', 'Cleveland Rebels', 'Rebels', 'Cleveland Arena', 'OH', 'cleveland-rebels', '9999-12-31');
insert into team VALUES(7, 'ROC', 'Rochester', 'East', 'Atlantic', 'Rochester', '2008-07-01', 'Rochester Royals', 'Royals', 'Edgerton Park Arena', 'NY', 'rochester-royals', '2009-06-30');
insert into team VALUES(8, 'STL', 'St. Louis', 'East', 'Southwest', 'St. Louis', '2010-07-01', 'St. Louis Bomber''s', 'Bomber''s', 'St. Louis Multiplex', 'MO', 'st-louis-bomber''s', '2011-06-30');
insert into team VALUES(9, 'DET', 'Detroit', 'East', 'Central', 'Detroit', '2010-07-01', 'Detroit Pistons', 'Pistons', 'Palace of Auburn Hills', 'MI', 'detroit-pistons', '9999-12-31');
insert into team VALUES(10, 'ROC', 'Rochester', 'East', 'Atlantic', 'Rochester', '2007-07-01', 'Rochester Royals', 'Royals', 'Edgerton Park Arena', 'NY', 'rochester-royals', '2008-06-30');
insert into team VALUES(20, 'CHI', 'Chicago', 'East', 'Central', 'Chicago', '2012-07-01', 'Chicago Bulls', 'Bulls', 'United Center', 'IL', 'chicago-bulls', '9999-12-31');
insert into team VALUES(21, 'UTA', 'Salt Lake City', 'West', 'Northwest', 'Utah', '2012-07-01', 'Utah Jazz', 'Jazz', 'Energy Solutions Arena', 'UT', 'utah-jazz', '9999-12-31');

insert into player VALUES(1, '2002-02-20', 'Sacramento, CA, USA', 'Luke Puzdrakiew''icz', 'Luke', 78, 'Puzdrakiew''icz', 175);
insert into player VALUES(2, '1966-06-02', 'Sacramento, CA, USA', 'Thad Puzdrakiewicz', 'Thad', 81, 'Puzdrakiewicz', 210);
insert into player VALUES(3, '2000-03-13', 'Sacramento, CA, USA', 'Thad Puzdrakiewicz', 'Thad', 79, 'Puzdrakiewicz', 180);
insert into player VALUES(4, '1969-09-08', 'Sacramento, CA, USA', 'Michelle Puzdrakiewicz', 'Michelle', 75, 'Puzdrakiewicz', 170);
insert into player VALUES(5, '1966-06-10', 'Sacramento, CA, USA', 'Junior Puzdrakiewicz', 'Junior', 80, 'Puzdrakiewicz', 240);
insert into player VALUES(6, '1966-06-07', 'Sacramento, CA, USA', 'Tuey Puzdrakiewicz', 'Tuey', 80, 'Puzdrakiewicz', 240);
insert into player VALUES(7, '2010-06-07', 'Sacramento, CA, USA', 'Trey Puzdrakiewicz', 'Tuey', 80, 'Puzdrakiewicz', 240);
insert into player VALUES(10, '1990-03-04', 'Saginaw, Michigan, USA', 'Andre Drummond', 'Andre', 79, 'Drummond', 230);
insert into player VALUES(11, '1989-09-02', 'Philadelphia, Pennsylvania, USA', 'Marcus Morris', 'Marcus', 81, 'Morris', 235);
insert into player VALUES(12, '1993-02-18', 'Thomaston, Georgia, USA', 'Kentavious Caldwell-Pope', 'Kentavious', 78, 'Caldwell-Pope', 204);
insert into player VALUES(13, '1990-04-16', 'Pordenone, Italy', 'Reggie Jackson', 'Reggie', 75, 'Jackson', 208);
insert into player VALUES(14, '1992-05-06', 'Utėnai, Lithuania', 'Jonas Valančiūnas', 'Jonas', 79, 'Valančiūnas', 180);
insert into player VALUES(20, '1992-06-26', 'Saint-Quentin, France', 'Rudy Gobert', 'Rudy', 85, 'Gobert', 220);
insert into player VALUES(21, '1990-03-23', 'Brownsburg, Indiana, USA', 'Gordon Hayward', 'Gordon', 81, 'Hayward', 207);
insert into player VALUES(22, '1989-09-14', 'Houston, Texas, USA', 'Jimmy Butler', 'Jimmy', 79, 'Butler', 220);
insert into player VALUES(23, '1988-10-04', 'Chicago, Illinois, USA', 'Derrick Rose', 'Derrick', 75, 'Rose', 190);

insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(1, 1, 1, '2009-11-30', '9999-12-31', '21', 'SG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(2, 1, 4, '2009-10-30', '2009-11-03', '31', 'PG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(3, 2, 4, '2009-10-30', '2009-11-04', '12', 'C');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(4, 2, 2, '2010-01-01', '2010-01-10', '12', 'C');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(5, 3, 5, '2009-10-30', '9999-12-31', '9', 'PG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(10, 10, 9, '2015-11-15', '9999-12-31', '51', 'SG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(11, 11, 9, '2015-10-30', '9999-12-31', '28', 'PG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(12, 12, 9, '2015-10-30', '2015-12-01', '12', 'C');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(13, 13, 9, '2015-10-30', '9999-12-31', '9', 'PG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(14, 14, 9, '2015-10-30', '9999-12-31', '9', 'PG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(20, 20, 21, '2014-10-29', '2015-06-30', '27', 'C');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(21, 21, 21, '2014-10-29', '2015-06-30', '20', 'SF');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(22, 22, 20, '2014-10-29', '2015-06-30', '41', 'SF');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(23, 23, 20, '2014-10-29', '2015-06-30', '1', 'PG');

insert into official VALUES(1, 'Joe', '2009-07-01', 'LateCa''ll', '96', '2010-06-30');
insert into official VALUES(2, 'Mike', '2009-07-01', 'MissedCa''ll', '97', '2010-06-30');
insert into official VALUES(3, 'Mike', '2010-07-01', 'MissedCa''ll', '98', '2011-06-30');
insert into official VALUES(4, 'Hefe', '2005-07-01', 'QuestionableCall', '99', '2006-06-30');
insert into official VALUES(5, 'Limo', '2005-07-01', 'TerribleCall', '100', '2006-06-30');
insert into official VALUES(10, 'Zach', '2010-10-30', 'Zarba', '28', '9999-12-31');
insert into official VALUES(11, 'Brian', '2010-04-25', 'Forte', '100', '9999-12-31');
insert into official VALUES(12, 'Eli', '2010-11-05', 'Roe', '45', '9999-12-31');
insert into official VALUES(20, 'Tony', '1994-11-08', 'Brothers', '25', '9999-12-31');
insert into official VALUES(21, 'Sean', '1993-11-05', 'Corbin', '33', '9999-12-31');
insert into official VALUES(22, 'J.T.', '2009-12-08', 'Orr', '72', '9999-12-31');

insert into game VALUES(1, '2015-10-27 20:00:00.0', 'Regular', 'Completed');
insert into gameOfficial (id, gameId, officialId) VALUES(1, 1, 2);
insert into gameOfficial (id, gameId, officialId) VALUES(2, 1, 3);
insert into gameOfficial (id, gameId, officialId) VALUES(3, 1, 4);
insert into boxScore (id, gameId, teamId, location, result, points) VALUES(1, 1, 1, 'Home', 'Win', 114);
insert into boxScorePlayer(id, boxScoreId, rosterPlayerId, position, starter, points) VALUES(1, 1, 1, 'SG', true, 12);
insert into boxScore (id, gameId, teamId, location, result, points) VALUES(2, 1, 2, 'Away', 'Loss', 98);
insert into boxScorePlayer(id, boxScoreId, rosterPlayerId, position, starter, points) VALUES(2, 2, 2, 'PG', false, 0);
insert into boxScorePlayer(id, boxScoreId, rosterPlayerId, position, starter, points) VALUES(3, 2, 3, 'C', true, 5);

insert into game VALUES(2, '2015-10-27 21:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(3, 2, 3, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(4, 2, 4, 'Away');

insert into game VALUES(3, '2015-10-27 20:30:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(5, 3, 5, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(6, 3, 6, 'Away');

insert into game VALUES(4, '2015-10-28 20:00:00.0', 'Regular', 'Completed');
insert into boxScore (id, gameId, teamId, location) VALUES(7, 4, 1, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(8, 4, 3, 'Away');

insert into game VALUES(5, '2015-10-29 20:00:00.0', 'Regular', 'Postponed');
insert into boxScore (id, gameId, teamId, location) VALUES(9, 5, 5, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(10, 5, 3, 'Away');

insert into game VALUES(6, '2015-10-29 20:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(11, 6, 1, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(12, 6, 2, 'Away');

insert into game VALUES(7, '2015-10-30 10:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(13, 7, 5, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(14, 7, 3, 'Away');

insert into game VALUES(8, '2015-10-15 10:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(15, 8, 6, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(16, 8, 5, 'Away');

insert into game VALUES(9, '2015-11-24 10:00:00.0', 'Regular', 'Completed');
insert into boxScore (id, gameId, teamId, location) VALUES(17, 9, 9, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(18, 9, 8, 'Away');

insert into game VALUES(10, '2015-01-07 19:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(19, 10, 20, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(20, 10, 21, 'Away');

insert into game VALUES(11, '2015-01-08 19:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(21, 11, 20, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(22, 11, 21, 'Away');

insert into game VALUES(12, '2015-01-09 19:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(23, 12, 20, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(24, 12, 21, 'Away');

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses,
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame)
VALUES(1, 1, '2015-10-30', 1, '1st', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses,
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame)
VALUES(2, 3, '2015-10-30', 3, '3rd', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses,
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame)
VALUES(3, 3, '2015-10-31', 5, '5th', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses,
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame)
VALUES(4, 4, '2015-10-31', 17, '17th', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses,
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame)
VALUES(5, 5, '2015-10-31', 17, '17th', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses,
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame)
VALUES(6, 6, '2015-10-31', 17, '17th', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);
