package com.example.gon.myapplication;

public class Flag extends Entity{
  
  /* flag does not have any logic yet */
  @Override
  public void play(Field field){}
  
  /* flag does not move yet */
  @Override
  public void update(Field field){}
  



  @Override
  public boolean equals(Object o){
    if(this == o){return true;}

    if(o == null){ 
      System.err.println("null equals");
      return false;  
    }

    /* flags are the same if they have the same symbol */
    if(o instanceof Flag && this.getSymbol() == ((Flag)o).getSymbol()){
      return true;
    }
    
    return false;
  }


  public Flag(char symbol, int x, int y){
    super(symbol, x, y);
    speedX = speedY = 0;
  }

  public Flag(Field f, int side, char symbol, int x, int y, int image){
    super(f, side, symbol, x, y, image);
    speedX = speedY = 0;
  }
  
}