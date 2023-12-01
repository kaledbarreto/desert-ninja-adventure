package com.kaledstudios.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.kaledstudios.entities.Player;
import com.kaledstudios.main.Game;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(2, 1, 50, 8);
		g.setColor(Color.green);
		g.fillRect(2, 1,(int)((Game.player.life/Game.player.maxlife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxlife, 11, 9);
	}
}
