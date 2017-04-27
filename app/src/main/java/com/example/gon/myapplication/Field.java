package com.example.gon.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by Gon on 2016-12-29.
 */

public class Field extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    private Context context;

    private Canvas canvas;

    private Paint paint;

    /** determines if players start at base or not */
    public static boolean START_FROM_BASE = true;

    /** constant scaling for moving */
    protected static long SCALE = 30;

    /** determines if the game has been won (completed) */
    protected static boolean GAME_IS_WON = false;

    /** distance between entities to be considered touching (from centre to centre) */
    public static Double ARMS_LENGTH = 12.0;

    /** max x-coordinate for entity on field */
    final int maxX;

    /** max y-coordinate for entity on field */
    final int maxY;

    /** min x-coordinate for entity on field */
    final int minX;

    /** min y-coordinate for entity on field */
    final int minY;

     /*                   */
  /* teams in the game */
  /*                   */

    /** the team for territory 1 */
    protected ArrayList<Entity> team1 = new ArrayList<Entity>();

    /** the team for territory 2 */
    protected ArrayList<Entity> team2 = new ArrayList<Entity>();

    /** getter for the players in team 1
     *
     * @return the players in team 1
     */
    public ArrayList<Entity> getTeam1(){ return this.team1; }

    /** getter for the players in team 2
     *
     * @return the players in team 2
     */
    public ArrayList<Entity> getTeam2(){ return this.team2; }





  /*                                    */
  /* basic entities that all games need */
  /*                                    */



    /** A look-up table for players and their ids */
    protected Hashtable<Entity, Integer> getID = new Hashtable<Entity, Integer>();



    /** things for territory 1 */
    protected final Entity flag1;
    protected final Entity jail1;
    protected final Entity base1;

    /** things for territory 2 */
    protected final Entity flag2;
    protected final Entity jail2;
    protected final Entity base2;


    /** the flag for territory 2 */

    /** the jail for territory 1 */

    /** the jail for territory 2 */

    /** the base for territory 1 */
    /** the base for territory 2 */

    /** holds the "things" in a game: flags, bases and jails */
    ArrayList<Entity> things = new ArrayList<Entity>();

    /** A look-up table that maps entity's to the team (territory) they belong to */
    protected Hashtable<Entity, Integer> getTeam = new Hashtable<Entity, Integer>();

    /** gets the x and y coordinates of flag 1
     *
     * @return [x,y], the x and y coordinates of the flag 1 on this field
     */
    public int[] getFlag1Position(){ return new int[]{ flag1.getX(), flag1.getY() }; }

    /** gets the x and y coordinates of flag 2
     *
     * @return [x,y], the x and y coordinates of the flag 2 on this field
     */
    public int[] getFlag2Position(){ return new int[]{ flag2.getX(), flag2.getY() }; }

    /** gets the x and y coordinates of jail 1
     *
     * @return [x,y], the x and y coordinates of the jail 1 on this field
     */
    public int[] getJail1Position(){ return new int[]{ jail1.getX(), jail1.getY() }; }

    /** gets the x and y coordinates of jail 2
     *
     * @return [x,y], the x and y coordinates of the jail 2 on this field
     */
    public int[] getJail2Position(){ return new int[]{ jail2.getX(), jail2.getY() }; }

    /** gets the x and y coordinates of base 1
     *
     * @return [x,y], the x and y coordinates of the base 1 on this field
     */
    public int[] getBase1Position(){ return new int[]{ base1.getX(), base1.getY() }; }

    /** gets the x and y coordinates of base 2
     *
     * @return [x,y], the x and y coordinates of the base 2 on this field
     */
    public int[] getBase2Position(){ return new int[]{ base2.getX(), base2.getY() }; }


    /** getter for all the "things" in the game: flags, bases and jails
     *
     * @return an arraylist containing both flags, bases and jails in this field
     */
    public ArrayList<Entity> getThings(){
    /* hard-coded list of all things in the game. Change this if you want to add more things */
        Entity[] allThings = new Entity[]{ flag1, flag2, base1, base2, jail1, jail2 };
        return new ArrayList<Entity>( Arrays.asList(allThings) );

    }




  /*                                               */
  /* methods that the game will use                */
  /*                                               */


    /** update all entities on the field
     * <p>
     * update the position of all entity's on this field
     */
    public void update(){
        for(Entity en: team1){
            try{
                en.updatePosition(SCALE*1, this, getID.get(en));
            }catch(EntityOutOfBoundsException e){
                // add code here
            }
        }
        for(Entity en: team2){
            try{
                en.updatePosition(SCALE*1, this, getID.get(en));
            }catch(EntityOutOfBoundsException e){
                // add code here
            }
        }
        for(Entity en: things){
            try{
                en.updatePosition(SCALE*1, this, getID.get(en));
            }catch(EntityOutOfBoundsException e){
                // add code here
            }
        }
    }

    /** have all entities on this field "play" (perform their own logic) */
    public void play(){
        for(Entity e: team1){
            e.play(this);
        }
        for(Entity e: team2){
            e.play(this);
        }
        for(Entity e: things){
            e.play(this);
        }
    }

    /** Assigns a player to given territory on the field
     * <p>
     * Registers a player to a side (territory) on the field.  If <code>START_FROM_BASE</code> is
     * true, a registered player will have its position set be the same as the base for
     * territory they are being registered to.
     * <p>
     * Side effects: registers a player with a territory.  Assigns a sprite (image) to the player.
     * Players position is set to their base if START_FROM_BASE is true.
     *
     * @param a is a player
     * @param id is the player's id number
     * @param territory is either 1 or 2 (representing a territory)
     * @return true if <code>a</code> is not already registered, <code>territory</code> is 1 or 2,
     * and the player's coordinates are not on the other territory. False, otherwise.
     */
    public boolean registerPlayer(Player a, int id, int territory){
        if( getTeam.containsKey(a) || territory < 1 || territory > 2){
            return false;
        }
        if( territory == 1 ){
            if (a.getX() > maxX/2){
        /* player must be on the correct side of the field */
                return false;
            }
            a.setSprite(R.drawable.blue, id);
            if(START_FROM_BASE){
                a.setX(base1.getX(), id);
                a.setY(base1.getY(), id);
            }
            team1.add(a);
        }else{
            if (a.getX() < maxX/2){
        /* player must be on the correct side of the field */
                return false;
            }
            a.setSprite(R.drawable.red, id);
            if(START_FROM_BASE){
                a.setX(base2.getX(), id);
                a.setY(base2.getY(), id);
            }
            team2.add(a);
        }

        getTeam.put(a, territory);
        getID.put(a, id);
        return true;
    }


    /** Assigns a non-player entity to given territory on the field
     * <p>
     * Side effects: registers a non-player entity with a territory.
     * Assigns a sprite (image) to the entity.
     * Remembers the entity's id.
     *
     * @param a is an entity (non-player)
     * @param id is the entity's id number
     * @param territory is either 1 or 2 (representing a territory)
     */
    public boolean registerThing(Entity a, int id, int territory){
        if( territory < 1 || territory > 2){
            return false;
        }

        // currently only remembers the thing's id

        getID.put(a, id);
        return true;
    }



  /*                                               */
  /* methods that players use                      */
  /*                                               */

    /** Attempt to catch a player
     *
     * @param a is a player trying to catch player <code>b<\code>
     * @param b is a player
     * @return true if <code>a</code> and <code>b</code> on are on different teams
     * and are within ARMS_LENGTH of each other. False otherwise.
     */
    public boolean catchOpponent(Player a, Player b){

        if( a.getTeam().equals(b.getTeam()) ){
            return false;
        }
        if( Math.hypot( a.getX() - b.getX(), a.getY() - b.getY() ) <= ARMS_LENGTH ){
            return true;
        }
        return false;
    }

    /** attempt to free a player from jail
     *
     * @param a is a player trying to free a teammate from jail
     * @param b is a player
     * @return true if <code>a</code> and <code>b</code> are on the same team,
     * <code<b</code> is in jail, and <code>a</code> is within ARMS_REACH of
     * the jail where <code>b</code> is located.  False otherwise.
     */
    public boolean freeTeammate(Player a, Player b){
        //
        // need to add some code here
        //
        return false;
    }

    /** attempt to pick up a flag
     *
     * @param a is a player trying to pick up a flag
     * @return true if <code>a</code> is within ARMS_REACH of
     * the opposing team's flag and it is not being carried by anyone else.
     * Returns false otherwise.
     */
    public boolean pickUpFlag(Player a){
        Entity b = flag1;
        if( getTeam.get(a).equals(new Integer(1)) ){
            b = flag2;
        }
        if( Math.hypot( a.getX() - b.getX(), a.getY() - b.getY() ) <= ARMS_LENGTH ){
            return true;
        }
        return false;
    }


    /** attempt to win the game
     *
     * @param a is a player
     * @return true if <code>a</code> is carrying its opponent's flag
     * and is with ARMS_REACH of its own base.  False otherwise.
     */
    public boolean winGame(Player a){
        Entity b = base1;
        if( getTeam.get(this).equals(new Integer(1)) ){
            b = base2;
        }

        //
        // needs some code to determine if a is carrying the flag
        //

        if( Math.hypot( a.getX() - b.getX(), a.getY() - b.getY() ) <= ARMS_LENGTH ){
            GAME_IS_WON = true;
            return true;
        }
        return false;
    }

    /** Asks if the game is still running (no winner yet)
     *
     * @return true if the game is still running (the game has not been won) and false otherwise.
     */
    public boolean gameStillRunning(){
        return !GAME_IS_WON;
    }


    public Field(Context context){
        super(context);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);

        // initialize field dimensions (hardcoded for now)
        maxX = this.canvas.getWidth();
        minX = 0;
        maxY = this.canvas.getHeight();
        minY = 0;

        // initialize all the "things" in the game

        flag1 = new Flag(this, 1, 'f', 34+minX, 191+minY, R.drawable.blueflag);
        jail1 = new Jail(this, 1, 'j', 29+minX, maxY- 191, R.drawable.jail);
        base1 = new Base(this, 1, 'b', 29+minX, 199+minY, R.drawable.bluebase);
        getTeam.put(base1, 1); getTeam.put(jail1,1); getTeam.put(flag1, 1);
        things.add(base1); things.add(jail1); things.add(flag1);

        flag2 = new Flag(this, 2, 'F', maxX-44, maxY- 207, R.drawable.redflag);
        jail2 = new Jail(this, 2, 'J', maxX-49, minY+191, R.drawable.jail);
        base2 = new Base(this, 2, 'B', maxX-49, maxY-199, R.drawable.redbase);
        getTeam.put(base2, 2); getTeam.put(jail2,2); getTeam.put(flag2, 2);
        things.add(base2); things.add(jail2); things.add(flag2);

        START_FROM_BASE = true;


        /* --------------------------------------------- */
        /* create players in the game                    */

        Player p,q;

        int NUM_PLAYERS = 1;

        for(int i=0; i<NUM_PLAYERS; i+=1){
            // create a player and register them with territory 1
            p = new RandomWalker(this, 1, "Cat van Kittenish", 12, "blues", 'b', Math.random()*400+10, Math.random()*500+10);

            // create a player and register them with territory 2
            //q = new DummyPlayer(f, 2, "Bunny El-Amin", 7, "reds", 'r', Math.random()*400+410, Math.random()*500+10);
        }
    }

    @Override

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
       /*DO NOTHING*/
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);
        this.canvas = getHolder().lockCanvas();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(true){
            try{
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {e.printStackTrace();}
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }


    @Override
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvas.drawBitmap(bitmap, 0,0, paint);
        canvas.drawColor(Color.GREEN);
        paint.setColor(Color.BLACK);

        // Draw a line
        canvas.drawLine((maxX-minX)/2,minY,(maxX-minX)/2,maxY,paint);
    }
}