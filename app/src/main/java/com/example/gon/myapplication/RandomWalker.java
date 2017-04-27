package com.example.gon.myapplication;//  COMP1006/1406 ~ Assignment 07
// Name : Tri Cao ~~ 
// Date: 11/20/2016
// Topic: Capture the flag game

/** RandomWalker class extends Player class
  * A RandomWalker player will continually walk in the playing field
  * When it reaches a border of the playing field, it should �bounce� 
  * off the wall and continue walking and bouncing off the borders
  */

public class RandomWalker extends Player{
 
  @Override
  public void play(Field field){
    // play is the logic for the player
    // you should make changes to the player's speed here
    // you should not update position
    
    /* Check if this player reaches the wall and "bounce" off the wall*/
    if(getX()+speedX<=field.minX || getX()+speedX>= field.maxX-16){     
      //If this player reaches the left or right wall
      this.speedX = -this.speedX;
      return;
    }
    if(getY()+speedY<=field.minY || getY()+speedY>=field.maxY-16){
      //If this player reaches the top or bottom wall
      this.speedY = -this.speedY;
      return;
    }
    
    // I'm giving RandomWalker a random addition to speedX and speedY everytime
    // in order not to imediately and unaturally stop this player
    this.speedX += (Math.random()*2-1)*0.1;
    this.speedY += (Math.random()*2-1)*0.1;
    //if the player going to fast (speed>2) then slow it down (to new speed = 1.5)
    if (this.speedX> 2){
      this.speedX = 1.5;
    }
    if (this.speedX<-2){
      this.speedX = -1.5;
    }
    if (this.speedY> 2){
      this.speedY = 1.5;
    }
    if (this.speedY<-2){
      this.speedX = -1.5;
    }
    //
    // should not update position here
    //      updatePosition(1,field);
    // this is now removed.  
    //
  }
    
  
  @Override
  public void update(Field field){}
  


  
  
  /** create a player that has some random motion 
    * <p>
    * the player starts in a random direction
    *
    * @param f is the field the player will be playing on
    * @param side is the side of the field the player will play on
    * @param name is this player's name 
    * @param number is this player's number
    * @param team is this player's team's name
    * @param symbol is a character representation of this player
    * @param x is the initial x position of this player
    * @param y is the initial y position of this player
    */
  public RandomWalker(Field f, int side, String name, int number, String team,char symbol, double x, double y){
    super(f, side, name, number, team, symbol, x, y);
    this.speedX = Math.random()*4-2;
    this.speedY = Math.random()*4-2;
  }
  
}