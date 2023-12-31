package com.kaledstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.kaledstudios.graficos.Spritesheet;
import com.kaledstudios.main.Game;
import com.kaledstudios.world.Camera;
import com.kaledstudios.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamage;
	
	private boolean hasGun = false;
	
	public int ammo = 0;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public boolean shoot = false, mouseShoot = false;
	
	public double life = 100, maxlife = 100;
	public int mx, my;
	
	public Player(int x, int y, int widht, int height, BufferedImage sprite) {
		super(x, y, widht, height, sprite);
		

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 10, 10, 10);
		
		for(int i = 0; i < 4; i++){
			rightPlayer[i] = Game.spritesheet.getSprite(20 + (i*10), 0, 10, 10);
		}
		
		for(int i = 0; i < 4; i++){
			leftPlayer[i] = Game.spritesheet.getSprite(20 + (i*10), 10, 10, 10);
		}
		
	}
	
	public void tick(){
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())){
			moved = true;
			dir = right_dir;
			x+=speed;
			
		}
		else if(left && World.isFree((int)(x-speed),this.getY())){
			moved = true;
			dir = left_dir;
			x-=speed;
			
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))){
			moved = true;
			y-=speed;
			
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed))){
			moved = true;
			y+=speed;
		}
		
		if(moved){
			frames++;
			if(frames == maxFrames){
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
		}
		
		checkCollisionGun();
		checkCollisionLifePack();
		checkCollisionAmmo();
		
		if(isDamaged){
			this.damageFrames++;
			if(this.damageFrames == 5) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			if(hasGun && ammo > 0) {
				ammo--;
				int dx = 0;
				int px = 0;
				int py = 3;
				if(dir == right_dir) {
					px = 8;
					dx = 1;
				}else {
					px = -8;
					dx = -1;
				}
				
				BulletShoot bullet = new BulletShoot(this.getX()+px, this.getY()+py, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			if(hasGun && ammo > 0) {
				ammo--;
				
				int px = 0, py = 1;
				double angle = 0;
				if(dir == right_dir){
					px = 200000;
					angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX()+px - Camera.x));
				}else {
					px = -1;
					angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX()+px - Camera.x));
				}
				
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
	
				BulletShoot bullet = new BulletShoot(this.getX()+px, this.getY()+py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}
		
		if(life <= 0){
			//Game Over
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*10 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*10 - Game.HEIGHT);
	}
	
	public void checkCollisionGun(){
		for(int i = 0; i< Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)){
					hasGun = true;
					//System.out.println("Peguei a Arma! >:D");
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void checkCollisionAmmo(){
		for(int i = 0; i< Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)){
					ammo+=100;
					//System.out.println("Munição Atual: "+ ammo);
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void checkCollisionLifePack(){
		for(int i = 0; i< Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack) {
				if(Entity.isColidding(this, atual)){
					life+=10;
					if(life >= 100)
						life = 100;
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void render(Graphics g){
		if(!isDamaged) {
			if(dir == right_dir){
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun){
					//Desenhar Arma p/ Direita
					g.drawImage(Entity.GUN_RIGHT, this.getX()+3 - Camera.x, this.getY() - Camera.y, null);
				}
			}else if(dir == left_dir){
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun){
					//Desenhar Arma p/ Esquerda
					g.drawImage(Entity.GUN_LEFT, this.getX()-3 - Camera.x, this.getY() - Camera.y, null);
				}
			}
		}else {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
			if(hasGun){
				if(dir == left_dir) {
					g.drawImage(Entity.GUN_DAMAGE_LEFT, this.getX()-3 - Camera.x, this.getY() - Camera.y, null);
				}else {
					g.drawImage(Entity.GUN_DAMAGE_RIGHT, this.getX()+3 - Camera.x, this.getY() - Camera.y, null);
				}
			}
		}
	}
}
