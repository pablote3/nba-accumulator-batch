package com.rossotti.basketball;

import com.rossotti.basketball.app.service.GameAppService;
import com.rossotti.basketball.app.service.TeamAppService;
import com.rossotti.basketball.jpa.model.BoxScore;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.jpa.model.Game.GameStatus;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.util.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

//@SpringBootApplication
public class LoadSchedule {
	private final Logger logger = LoggerFactory.getLogger(LoadSchedule.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(LoadSchedule.class, args);

		TeamAppService teamService = (TeamAppService) ctx.getBean(TeamAppService.class);
		GameAppService gameService = (GameAppService) ctx.getBean(GameAppService.class);
		PropertyService propertyService = (PropertyService) ctx.getBean(PropertyService.class);

		Path path =  Paths.get(propertyService.getProperty_Path("loader.fileSchedule")).resolve(System.getProperty("fileName"));
		File file = path.toFile();

		BufferedReader bufRdr = null;
		try {
			bufRdr = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		Game game;
		BoxScore boxScoreHome;
		BoxScore boxScoreAway;
		Team teamHome;
		Team teamAway;
		int i = 0;

		//read each line of text file
		try {
			assert bufRdr != null;
			bufRdr.readLine();								            	//jump over header line
			while((line = bufRdr.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				String gameDate = st.nextToken();                        	//start date
				String gameDateTime = gameDate + " " + st.nextToken();    	//start time (ET)
				String awayTeam = st.nextToken().trim();
				String homeTeam = st.nextToken().trim();

				teamHome = teamService.findTeamByLastName(homeTeam, LocalDate.parse(gameDate, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
				teamAway = teamService.findTeamByLastName(awayTeam, LocalDate.parse(gameDate, DateTimeFormatter.ofPattern("MM/dd/yyyy")));

				game = new Game();
				game.setGameDateTime(LocalDateTime.parse(gameDateTime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")));
				game.setStatus(GameStatus.Scheduled);
				game.setSeasonType(Game.SeasonType.Regular);

				boxScoreAway = new BoxScore();
				boxScoreAway.setLocation(BoxScore.Location.Away);
				boxScoreAway.setTeam(teamAway);
				boxScoreAway.setGame(game);
				game.addBoxScore(boxScoreAway);

				boxScoreHome = new BoxScore();
				boxScoreHome.setLocation(BoxScore.Location.Home);
				boxScoreHome.setTeam(teamHome);
				boxScoreHome.setGame(game);
				game.addBoxScore(boxScoreHome);

				gameService.createGame(game);
				System.out.println("i = " + i++ + " " + teamAway.getFullName() + " at " + teamHome.getFullName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//close the file
		try {
			bufRdr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        ctx.close();
	}
}