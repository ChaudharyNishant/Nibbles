//Created by Nishant Chaudhary
//https://github.com/ChaudharyNishant

package sample;

import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Main extends Application
{
	Timeline timeline = new Timeline();
	String pressed = "RIGHT";
	int l = 6, time = 500, c = 1;
	Snake bonus = new Snake(-1, -1);
	boolean dead;
	
	public void start(Stage primaryStage)
	{
		Random rand = new Random();
		Button btn[][] = new Button[14][14];
		GridPane grid = new GridPane();
		int i, j;
		Snake snake[] = new Snake[144];
		Snake point = new Snake();
		for(i = 0; i < 144; i++)
			snake[i] = new Snake();
		for(i = 0; i < 14; i++)
			for(j = 0; j < 14; j++)
			{
				btn[i][j] = new Button();
				grid.add(btn[i][j], j, i);
				btn[i][j].setMinHeight(20);
				btn[i][j].setMaxHeight(20);
				btn[i][j].setPrefWidth(20);
				if(i == 0 || j == 0 || i == 13 || j == 13)
					btn[i][j].setId("black");
				else
					btn[i][j].setId("white");
			}
		for(i = 1; i < 6; i++)
			btn[1][i].setId("hor");
		btn[1][6].setId("hl");
		snake[0].iIndex = 1;
		snake[0].jIndex = 6;
		for(i = 1; i < 6; i++)
		{
			snake[i].iIndex = 1;
			snake[i].jIndex = 6 - i;
		}
		do
		{
			point.iIndex = rand.nextInt(12) + 1;
			point.jIndex = rand.nextInt(12) + 1;
		}while(point.iIndex == 1 && point.jIndex < 7);
		btn[point.iIndex][point.jIndex].setId("yellow");
		
		for(i = 0; i < 14; i++)
			for(j = 0; j < 14; j++)
			{
				btn[i][j].setOnAction(e ->
				{
					if(timeline.getStatus().toString().equals("RUNNING"))
						timeline.stop();
					else if(!dead)
						timeline.play();
				});
				btn[i][j].setOnKeyPressed(e ->
				{
					if(snake[0].jIndex != snake[1].jIndex && (e.getCode().toString().equals("UP") || e.getCode().toString().equals("DOWN")) && timeline.getStatus().toString().equals("RUNNING"))
						pressed = e.getCode().toString();
					else if(snake[0].iIndex != snake[1].iIndex && (e.getCode().toString().equals("RIGHT") || e.getCode().toString().equals("LEFT")) && timeline.getStatus().toString().equals("RUNNING"))
						pressed = e.getCode().toString();
				});
			}
		
		Play(btn, snake, point);
		
		Scene scene = new Scene(grid, 280, 280);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	void Play(Button btn[][], Snake snake[], Snake point)
	{
		Random rand = new Random();
		timeline = new Timeline(new KeyFrame(Duration.millis(time), e->
		{
			int x;
			dead = false;
			if(!dead)
			{
				if(snake[l-1].iIndex != -1)
					btn[snake[l-1].iIndex][snake[l-1].jIndex].setId("white");
				SetColor(btn, snake);
				
				for(x = l-1; x > 0; x--)
				{
					snake[x].iIndex = snake[x-1].iIndex;
					snake[x].jIndex = snake[x-1].jIndex;
				}
				
				if(pressed.equals("RIGHT"))
				{
					btn[snake[0].iIndex][snake[0].jIndex + 1].setId("hl");
					snake[0].jIndex++;
				}
				else if(pressed.equals("DOWN"))
				{
					btn[snake[0].iIndex + 1][snake[0].jIndex].setId("hu");
					snake[0].iIndex++;
				}
				else if(pressed.equals("LEFT"))
				{
					btn[snake[0].iIndex][snake[0].jIndex - 1].setId("hr");
					snake[0].jIndex--;
				}
				else if(pressed.equals("UP"))
				{
					btn[snake[0].iIndex - 1][snake[0].jIndex].setId("hd");
					snake[0].iIndex--;
				}
			}
			if(snake[0].iIndex == point.iIndex && snake[0].jIndex == point.jIndex && l < 144)
			{
				boolean fine;
				if(c == 2 && l < 143)
				{
					time = 500;
					timeline.stop();
					Play(btn, snake, point);
					do
					{
						fine = true;
						bonus.iIndex = rand.nextInt(12) + 1;
						bonus.jIndex = rand.nextInt(12) + 1;
						for(x = 0; x < l; x++)
							if(snake[x].iIndex == bonus.iIndex && snake[x].jIndex == bonus.jIndex)
								fine = false;
					}while(!fine);
					btn[bonus.iIndex][bonus.jIndex].setId("blue");
				}
				else if(c > 2)
				{
					btn[bonus.iIndex][bonus.jIndex].setId("white");
					bonus.iIndex = -1;
					bonus.jIndex = -1;
					c = 1;
				}
				
				l++;
				snake[l-1].iIndex = -1;
				do
				{
					fine = true;
					point.iIndex = rand.nextInt(12) + 1;
					point.jIndex = rand.nextInt(12) + 1;
					for(x = 0; x < l; x++)
						if(snake[x].iIndex == point.iIndex && snake[x].jIndex == point.jIndex)
							fine = false;
					if(bonus.iIndex == point.iIndex && bonus.jIndex == point.jIndex)
						fine = false;
				}while(!fine);
				btn[point.iIndex][point.jIndex].setId("yellow");
				c++;
			}
			if(snake[0].iIndex == bonus.iIndex && snake[0].jIndex == bonus.jIndex)
			{
				bonus.iIndex = -1;
				bonus.jIndex = -1;
				c = 1;
				if(l < 139)
					x = rand.nextInt(6);
				else
					x = rand.nextInt(5);
				if(x == 0)													//Slower
				{
					c = 1;
					time = 750;
					timeline.stop();
					Play(btn, snake, point);
				}
				else if(x == 1)												//Faster
				{
					c = 1;
					time = 250;
					timeline.stop();
					Play(btn, snake, point);
				}
				else if(x == 2)												//Slowest
				{
					c = 1;
					time = 1000;
					timeline.stop();
					Play(btn, snake, point);
				}
				else if(x == 3)												//Fastest
				{
					c = 1;
					time = 150;
					timeline.stop();
					Play(btn, snake, point);
				}
				else if(x == 4)												//Shorter
				{
					for(x = l - 5; x < l; x++)
						btn[snake[x].iIndex][snake[x].jIndex].setId("white");
					l -= 5;
				}
				else														//Longer
				{
					l += 5;
					for(x = l-5; x < l; x++)
						snake[x].iIndex = -1;
				}
			}
			
			for(x = 3; x < l; x++)
				if(snake[0].iIndex == snake[x].iIndex && snake[0].jIndex == snake[x].jIndex)
					dead = true;
			if(l == 144)
				dead = true;
			if(snake[0].iIndex == 0 || snake[0].jIndex == 0 || snake[0].iIndex == 13 || snake[0].jIndex == 13 || dead)
			{
				System.out.print("Dead");
				dead = true;
				timeline.stop();
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	void SetColor(Button btn[][], Snake snake[])
	{
		int i, j, im, jm;
		i = snake[0].iIndex;
		j = snake[0].jIndex;
		im = snake[1].iIndex;
		jm = snake[1].jIndex;
		if(pressed.equals("RIGHT") || pressed.equals("LEFT"))
			btn[i][j].setId("hor");
		else if(pressed.equals("UP") || pressed.equals("DOWN"))
			btn[i][j].setId("ver");
		if((pressed.equals("LEFT") && i == im+1 && j == jm) || (pressed.equals("UP") && i == im && j == jm+1))
			btn[i][j].setId("lu");
		else if((pressed.equals("UP") && i == im && j == jm-1) || (pressed.equals("RIGHT") && i == im+1 && j == jm))
			btn[i][j].setId("ur");
		else if((pressed.equals("RIGHT") && i == im-1 && j == jm) || (pressed.equals("DOWN") && i == im && j == jm-1))
			btn[i][j].setId("rd");
		else if((pressed.equals("DOWN") && i == im && j == jm+1) || (pressed.equals("LEFT") && i == im-1 && j == jm))
			btn[i][j].setId("dl");
		
		i = snake[l-3].iIndex;
		j = snake[l-3].jIndex;
		im = snake[l-2].iIndex;
		jm = snake[l-2].jIndex;
		if(i == im && j == jm-1)
			btn[im][jm].setId("l");
		else if(i == im && j == jm+1)
			btn[im][jm].setId("r");
		else if(i == im-1 && j == jm)
			btn[im][jm].setId("u");
		else if(i == im+1 && j == jm)
			btn[im][jm].setId("d");
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}

//Created by Nishant Chaudhary
//https://github.com/ChaudharyNishant
