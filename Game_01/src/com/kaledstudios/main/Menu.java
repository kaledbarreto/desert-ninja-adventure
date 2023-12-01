package com.kaledstudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {

	public String[] options = {"new game", "load game", "exit"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
	public boolean pause = false;
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "new game" || options [currentOption] == "Continue") {
				Game.gameState = "NORMAL";
				pause = false;
			}else if(options[currentOption] == "exit") {
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,200));
		g.fillRect(0,0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.red);
		g.setFont(new Font("arial", Font.BOLD, 35));
		g.drawString(">The Desert Ninja Adventure<", (Game.WIDTH*Game.SCALE) /2 -230, (Game.HEIGHT*Game.SCALE) / 2 - 200);
		
		//Opções do Menu
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 25));
		if(pause == false)
			g.drawString("New Game", (Game.WIDTH*Game.SCALE) /2 - 67, (Game.HEIGHT*Game.SCALE) / 2 - 80);
		else
			g.drawString("Continue", (Game.WIDTH*Game.SCALE) /2 - 57, (Game.HEIGHT*Game.SCALE) / 2 - 80);
		g.setFont(new Font("arial", Font.BOLD, 25));
		g.drawString("Load Game", (Game.WIDTH*Game.SCALE) /2 - 70, (Game.HEIGHT*Game.SCALE) / 2 - 40);
		g.setFont(new Font("arial", Font.BOLD, 25));
		g.drawString("Exit", (Game.WIDTH*Game.SCALE) /2 -30, (Game.HEIGHT*Game.SCALE) / 2);
		
		if(options[currentOption] == "new game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) /2 - 97, 220);
		}else if(options[currentOption] == "load game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) /2 - 100, 260);
		}else if(options[currentOption] == "exit") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) /2 - 60, 300);
		}
		
	}
}
