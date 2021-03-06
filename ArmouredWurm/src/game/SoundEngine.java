package game;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * This will be the sound Class, it will have a queue that the Engine sends sounds to, 
 * this classes job will be to look at the queue and if there is a sound in the queue. play this sound.
 * it will also have volume control and other such things. 
 * 
 *  Ideas:
 *  Map ( String -> sound)
 *  whens the engine starts it will load all the sounds to a map 
 *  
 *  then when something wants to make a sound they will send a String over through the queue. 
 *  the sound will look at the String find the appropriate sound in the Map and play it.
 *  
 *  text file with all the availible sounds?
 *  
 *  Level music on loop?
 */
public class SoundEngine 
{
	private Map<String,Clip> soundData;
	public Queue<String> playQ;
	private FloatControl volume;
	private float v;
	public boolean audible;
	//LVL MUSIC NEEEDS A BUFFER.
	public SoundEngine()
	{
		//constructor;
		
		this.audible = true;
		//John Setup a soundloader here!
		this.soundData = new HashMap<String,Clip>();
		this.playQ = new LinkedList<String>();
		this.v = -10.0f;
		
		//add("res/drip2.wav");
		
	}
	public void update()
	{
		for( String x : playQ)
		{
			Clip out = soundData.get(x);
			out.setMicrosecondPosition(0); 
			out.start();
		}
		playQ.clear();
	}
	public void add(String in)
	{
		AudioInputStream out = null;
		try
		{
			
		if(new File(in).isFile())
		{
			out = AudioSystem.getAudioInputStream(new File(in));
		}
		else
		{
			//out = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/"+in));
			InputStream is = getClass().getResourceAsStream("/"+in);
			out = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
		}
			//ADD and panic state!
		AudioFormat format = out.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		
		Clip soundclip = null;
		
		
		soundclip = (Clip) AudioSystem.getLine(info);
		soundclip.open(out);
		
		volume = (FloatControl) soundclip.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue(v);
		
		soundData.put(in,soundclip);
		} 
		catch (FileNotFoundException e) {System.out.println("BAD SOUND FILE:"+in);}//e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		catch (UnsupportedAudioFileException e) {e.printStackTrace();}
		catch (LineUnavailableException e) {e.printStackTrace();}
		
	}
	public void play(String in)
	{
		this.playQ.add(in);
	}
	public boolean getAudible()
	{
		return this.audible;
	}
		//MAIN<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>	
	public static void main(String[] args) 
	{
		//For Testing sound.
		SoundEngine jones = new SoundEngine();
		for(int i = 0; i < 400000; i++)
		{
			System.out.println(i);
			if(i == 25)
			{
				jones.playQ.add("res/drip2.wav");
			}
			if(i == 300000)
			{
				jones.playQ.add("res/drip2.wav");
			}
			jones.update();
		}
		
	}
}