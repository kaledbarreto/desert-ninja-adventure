package com.kaledstudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kaledstudios.main.Game;
import com.kaledstudios.main.Sound;
import com.kaledstudios.world.Camera;
import com.kaledstudios.world.World;

public class Enemy extends Entity{

	private double speed = 0.5;
	
	private int maskx = 5, masky = 5, maskw = 6, maskh = 6;
	
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
	
	private BufferedImage[] sprites;
	
	private int life = 5;
	
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;
	
	public Enemy(int x, int y, int widht, int height, BufferedImage sprite) {
		super(x, y, widht, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(70, 10, 10,10);
		sprites[1] = Game.spritesheet.getSprite(80, 10, 10,10);
	}

	public void tick(){
		//maskx = 5; masky = 5; maskw = 6; maskh = 6;
		//if(Game.rand.nextInt(100) < 30) {
		if(this.isCollidingWithPlayer() == false){
		if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
				&& !isColidding((int)(x+speed), this.getY())){
			x+=speed;
		}
		else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
				&& !isColidding((int)(x-speed), this.getY())){
			x-=speed;
		}
		
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
				&& !isColidding(this.getX(), (int)(y+speed))){
			y+=speed;
		}
		else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
				&& !isColidding(this.getX(), (int)(y-speed))){
			y-=speed;
		}
		}else {
			//Estamos Colidindo
			if(Game.rand.nextInt(100) < 10){
				//Sound.hurtEffect.play();
				Game.player.life-= Game.rand.nextInt(3);
				Game.player.isDamaged = true;

				//System.out.println("Vida: " + Game.player.life);
			}

		}
		//}
		
			frames++;
			if(frames == maxFrames){
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
		
			collidingBullet();
			
			if(life <=0) {
				destroySelf();
				return;
			}
			
			if(isDamaged) {
				this.damageCurrent++;
				if(this.damageCurrent == this.damageFrames){
					this.damageCurrent = 0;
					this.isDamaged = false;
				}
			}
		
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i = 0; i < Game.bullets.size(); i++){
			Entity e = Game.bullets.get(i);
				if(Entity.isColidding(this, e)) {
					isDamaged = true;
					life--;
					Game.bullets.remove(i);
					return;
				}
				
			}
		}
	
	public boolean isCollidingWithPlayer(){
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 10, 10);
		
		return enemyCurrent.intersects(player);
	}

	public boolean isColidding(int xnext, int ynext){
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for(int i = 0; i < Game.enemies.size(); i++){
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if(enemyCurrent.intersects(targetEnemy)){
				return true;
			}
		}
		
		
		return false;
	}
	
	public void render(Graphics g){
		if(!isDamaged)
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		else
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}
}
