package com.example.gon.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Entity class to represent "things" in the game: players, flags, jails, bases, etc. 
 */
public abstract class Entity{
  /** time scaling factor.  Will be more important when View is graphics based */
  public static double TIME_SCALE = 50;     
   
  /** character representation of this entity */
  protected char symbol;
  
  /** current x-coordinate of this entity */
  protected double x;
  /** current y-coordinate of this entity */
  protected double y;
  
  /** current speed in x-direction of this entity */
  protected double speedX;
  /** current speed in y-direction of this entity */
  protected double speedY;


  /** unique ID for entity */
  protected int id;

  protected static int ID = 0; // used to generate unique id's


 /** maximum speed for a player for use in tournament */
  public static final int MAX_SPEED = 10;
  


  /** creates an entity (for players) with specified symbol at specified position 
    * 
    * @param symbol is a character (char) representation of the entity
    * @param x is the x-coordinate of the entity
    * @param y is the y-coordinate of the entity
    */
  public Entity(char symbol, double x, double y){
    this.symbol = symbol;
    this.x = x;
    this.y = y;
    this.id = ID;  // set the ID for the entity 
    ID +=1;        // update the ID counter for the next entity created
  }
   
  
  /** constructor for non-player things
    * 
    * @param symbol is a character (char) representation of the entity
    * @param x is the x-coordinate of the entity
    * @param y is the y-coordinate of the entity
    */
  public Entity(Field f, int side, char symbol, double x, double y, int imageid){
  	this(symbol, x, y);
    f.registerThing(this, this.id, side);  // register with the field
    this.imageID = imageid;
  }
  


  /** gets this entity's symbol
    * 
    * @return the symbol of this entity (char)
    */
  public char getSymbol(){ return this.symbol; }
  
  /** gets the current speed in the x-direction of this entity
    * 
    * @return the current x-direction speed of this entity
    */
  public double getSpeedX(){ return this.speedX; }

  /** gets the current speed in the y-direction of this entity
    * 
    * @return the current y-direction speed of this entity
    */
  public double getSpeedY(){ return this.speedY; }
  

  /** gets the current x-coordinate of this entity 
    * 
    * @return the current x-coordinate of this entity
    */
  public int getX() { return (int) this.x; }

  /** gets the current y-coordinate of this entity 
    * 
    * @return the current y-coordinate of this entity
    */
  public int getY() { return (int) this.y; }
  
  /** update the position of this entity using basic physics
    * <p>
    * In each direction, we have
    * new_position = current_position + speed * time_elapsed
    * <p>
    * Note: TIME_SCALE will need to be manually adjusted to a value that allows good game play 
    * 
    * @param time is a length of time
    * @param field is the current field
    * @param id is supposed to the entity's id value
    */
  public final void updatePosition(long time, Field field, int id)
          throws SecurityException, EntityOutOfBoundsException
    { 
    if( id != this.id ){
      throw new SecurityException("Unauthorized change of position");
    }

    /* need to add logic of when to actually throw this  */
    if(false){
    	 throw new EntityOutOfBoundsException("out of bounds");
    }

    this.x += (time * this.speedX*2) / TIME_SCALE;
    this.y += (time * this.speedY*2) / TIME_SCALE;
    checkCoordinates(field);
  }
  
  /** checks that this player is within the field boundaries
    * 
    * @param field is the field that this entity is on
    */ 
  protected void checkCoordinates(Field field) throws EntityOutOfBoundsException{
    // check if this entity is out of the playing field
    // if they are throw the exception
    // if they are not, do nothing
  }
  
  
  /** logic for this entity (change direction/speed)
    * <p>
    * logic is based on current playing field (which holds information about 
    * all entities on the field) and possibly state of this entity. Do NOT
    * update the entity's position here, just update speed.
    *  
    * @param field is the current playing field
    */
  public abstract void play(Field field);
  
  /** update this entity for any changes to it 
    * <p>
    * For example, if this entity is moved by another entity, this entity's 
    * positions need to be updated.
    * 
    * @param field is the current playing field
    */
  public abstract void update(Field field);
  
 
  /** override the equals method from Object
    * <p>
    * This needs to be implemented in a child class
    * 
    * @param o is an object to be tested for equality with this
    * @return throws an exception
    */
  @Override
  public boolean equals(Object o){
  	if(this == o){return true;}
  	if(o==null){return false;}
  	if( o instanceof Entity){
  		return this.id == ((Entity)o).id;
  	}
  	return false;
  }


  /* Setter methods
   * 
   * We don't want a player of one team to be able to change the
   * position or movement of a player on another team.  
   * The caller needs to know the entity's id number.
   */
  
  /** sets the x-coordinate of this entity
    *
    * @param x is the new x-coordinate
    * @param id should be the entity's id
    */
  protected final void setX(double x, int id){
    /* check if the caller knows this entity's id */
    if( id != this.id ){
      throw new SecurityException("Unauthorized change of entity x coordinate");
    }
    this.x = x;
  }
  
  /** sets the y-coordinate of this entity
    *
    * @param y is the new y-coordinate
    * @param id should be the entity's id
    */
  protected final void setY(double y, int id){
    /* check if the caller knows this entity's id */
      if( id != this.id ){
      throw new SecurityException("Unauthorized change of entity y coordinate");
    }
    this.y = y;
  }
  
  /** sets the speed in the x-direction of this entity 
    * 
    * @param speedX is the new x-direction speed 
    * @param id should be the entity's id
    */
  protected final void setSpeedX(double speedX, int id){ 
    /* check if the caller knows this entity's id */
      if( id != this.id ){
      throw new SecurityException("Unauthorized change of entity x-direction speed");
    }
    this.speedX = speedX;
  }
  
  /** sets the current speed in the y-direction of this entity 
    * 
    * @param speedY is the new y-direction speed 
    * @param id should be the entity's id
    */
  protected final void setSpeedY(double speedY, int id){ 
    /* check if the caller knows this entity's id */
    if( id != this.id ){
      throw new SecurityException("Unauthorized change of entity y-direction speed");
    }
    this.speedY = speedY;
  }
  


  //
  // this is used for graphical representations
  // 

  /** the sprite that will represent this entity */
  protected int imageID;

  public int getSprite(){ return this.imageID; }
  
  public void setSprite(int imageID, int id){
    /* check if the caller knows this entity's id */
    if( id != this.id ){
      throw new SecurityException("Unauthorized change of entity's sprite");
    }
    this.imageID = imageID;
  }
  
  
  
}