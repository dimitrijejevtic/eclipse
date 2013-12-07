package gamecore;
import static org.lwjgl.opengl.GL11.*;
import java.io.IOException;
import org.lwjgl.opengl.*;
import org.lwjgl.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.lwjgl.Sys;

public class RunGame {
	private List<Texture> textList= new LinkedList<Texture>();
	private Texture oneT,twoT,threeT,fourT,fiveT,sixT,backT,middleBackT, buttonT, alphaT1, alphaT2;
	private Texture tempText=oneT;
	private int diceNum, currentFrame=0, delayFrame=0, y=0;
	private boolean isSelected=false;
	private long lastFrame;

	public RunGame(){
		try{
			Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(640, 480));
			Display.setInitialBackground(207, 207, 207);
			Display.setTitle("RollADice");
			Display.setVSyncEnabled(true);
			Display.create();}
		catch (LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		loadGL();
		loadTextures();

		while(!Display.isCloseRequested()){
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			//Background
			backT.bind();
			glBegin(GL_QUADS);	
				glTexCoord2f(1,1);
				glVertex2f(0,0);
				glTexCoord2f(1,1);
				glVertex2f(640,0);	
				glTexCoord2f(1,1);
				glVertex2f(640,480);
				glTexCoord2f(1,1);
				glVertex2f(0,480);
			glEnd();
			middleBackT.bind();
			glBegin(GL_QUADS);
				glTexCoord2f(0,0);
				glVertex2f(150,0);
				glVertex2f(490,0);
				glVertex2f(490,480);
				glVertex2f(150,480);
			glEnd();
			//Button
			buttonT.bind();
			glBegin(GL_QUADS);
				glTexCoord2f(0,0);
				glVertex2f(260,10);
				glTexCoord2f(1,0);
				glVertex2f(380,10);
				glTexCoord2f(1,1);
				glVertex2f(380,40);
				glTexCoord2f(0,1);
				glVertex2f(260,40);
			glEnd();

			currentFrame++;
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				Display.destroy();
				System.exit(0);
			}
			if(Display.isCloseRequested()){
				Display.destroy();
				System.exit(0);
			}			
			//checking if the button is clicked
			if(Mouse.getX()>260&& Mouse.getX()<380&& Mouse.getY()>10&&Mouse.getY()<50&& Mouse.isButtonDown(0)&&!isSelected){
				delayFrame=0;
				System.out.println("Button is clicked");
				isSelected=true;
				if(delayFrame<30){
					if(delayFrame%3==0){
						diceNum=randomize();
						tempText=this.textList.get(diceNum);
						try{
							renderDice(tempText);
						}catch (NullPointerException e){
							renderDice(oneT);
						}
					}
					delayFrame++;
				}
			}
			//rendering dice in the 1st frame
			try{
				renderDice(tempText);
			}catch (NullPointerException e){
				renderDice(oneT);
			}
			if(currentFrame==30){
				isSelected=false;
				currentFrame=0;
			}
			if(y>480){
				y=-480;
			}
			alphaT2.bind();
			glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(530, -y);
			glTexCoord2f(1,0);
			glVertex2f(680, -y);
			glTexCoord2f(1,1);
			glVertex2f(680,480-y);
			glTexCoord2f(0,1);
			glVertex2f(530,480-y);
			glEnd();
			alphaT1.bind();
			glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(0,y);
			glTexCoord2f(1,0);
			glVertex2f(150,y);
			glTexCoord2f(1,1);
			glVertex2f(150,y+480);
			glTexCoord2f(0,1);
			glVertex2f(0,y+480);
			glEnd();
			y++;
			Display.update();
			Display.sync(30);
		}
		Display.destroy();
	}

	public  void loadTextures(){
		try{
			this.oneT = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/one.png"));
			this.twoT = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/two.png"));
			this.threeT = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/three.png"));
			this.fourT = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/four.png"));
			this.fiveT = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/five.png"));
			this.sixT = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/six.png"));	
			this.backT=TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/back.png"));
			this.middleBackT= TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/middleBackT.png"));
			this.buttonT=TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/button.png"));
			this.alphaT1=TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/alpha.png"));
			this.alphaT2=TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/alpha.png"));
			textList.add(oneT);textList.add(twoT);textList.add(threeT);textList.add(fourT);textList.add(fiveT);textList.add(sixT);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public int randomize(){
		return new Random().nextInt(6);
	}
	public long getTime(){
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	public void renderDice(Texture texture) throws NullPointerException{
		texture.bind();			
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(200,360);
		glTexCoord2f(1,0);
		glVertex2f(440,360);
		glTexCoord2f(1,1);
		glVertex2f(440,120);
		glTexCoord2f(0,1);
		glVertex2f(200,120);
		glEnd();	
	}
	public void spin(){

	}
	public long getTite(){
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}
	public int getDelta(){
		long currentTime=getTime();
		int delta= (int) (currentTime-lastFrame);
		lastFrame=getTime();
		return delta;
	}
	public void loadGL(){
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0, 0, 640, 480);
		glMatrixMode(GL_MODELVIEW);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	public static void main(String[] args) {
		RunGame run=new RunGame();
	}

}

