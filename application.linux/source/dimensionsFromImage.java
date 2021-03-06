/* autogenerated by Processing revision 1277 on 2021-11-14 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class dimensionsFromImage extends PApplet {

PImage img; // Declare a variable of type PImage
float xOffset = 0.0f; 
float yOffset = 0.0f;

ArrayList<Line> lines;

double scaleFactor = 0.0f;
double oneUnitIs = 1.0f;

boolean isSettingUnitSize = false;
boolean isPanning = false;

String sizeUnitBuffer = "";

float zoomFactor = 1.0f;
float translateX = 0.0f;
float translateY = 0.0f;

 public void setup() {
  /* size commented out by preprocessor */;
  printInstructions();
  // Make a new instance of a PImage by loading an image file
  img = loadImage("input.jpg");
  //img.resize(0, height);
  lines = new ArrayList<Line>();
  frameRate(30);
}

public static void printInstructions() {
  System.out.println("If no image is loaded put a input.jpg JPEG image into the folder with this program and open again.");
  System.out.println("Use space bar to pan, use mouse wheel to zoom.");
  System.out.println("Drag the mouse to draw a line the first line will be used as the ruler and is one 'unit'.");
  System.out.println("Hit S to get into scale input mode, type a number and hit s to save the new 'unit' size.");
  System.out.println("Hit R to reset the view.");
}

 public void draw() {
  background(0);
  // Draw the image to the screen at coordinate (0,0)
  
  
  pushMatrix();
  translate(translateX,translateY);
  scale(zoomFactor);
  image(img,0,0);
  
  if(mousePressed && xOffset!=0 && yOffset!=0) {
    stroke(color(0, 0, 0));
    line(
      (xOffset-translateX)/zoomFactor,
      (yOffset-translateY)/zoomFactor,
      (mouseX-translateX)/zoomFactor,
      (mouseY-translateY)/zoomFactor
    );
  }
  
  for(int i = 0; i < lines.size(); i++) {
    Line line = lines.get(i);
    if(i == 0){
      scaleFactor = line.getLineLength();
      stroke(color(255, 0, 0));
    }
    else
      stroke(color(0, 0, 0));
    line(line.start.x,line.start.y, line.end.x,line.end.y);
    
    textSize(16/zoomFactor);
    //blendMode(DIFFERENCE);
    fill(color(255,255,255));
    rect(line.end.x, line.end.y-20, 100, 20);
    fill(color(0,0,0));
    text(String.valueOf(((int)(line.getLineLength()/scaleFactor*oneUnitIs*100))/100.0f), line.end.x, line.end.y);
    //blendMode(BLEND);
  }
  popMatrix();
  if(isPanning) {    
    translateX += mouseX - pmouseX;
    translateY += mouseY - pmouseY;
  }
}

 public void mousePressed() {
  xOffset = mouseX; 
  yOffset = mouseY;
}

 public void mouseReleased() {
  PVector start = new PVector((xOffset-translateX)/zoomFactor, (yOffset-translateY)/zoomFactor);
  PVector end = new PVector((mouseX-translateX)/zoomFactor, (mouseY-translateY)/zoomFactor);
  lines.add(new Line(start, end));
}

 public void mouseWheel(MouseEvent e) {
  translateX -= mouseX;
  translateY -= mouseY;
  float delta = e.getCount() < 0 ? 1.05f : e.getCount() > 0 ? 1.0f/1.05f : 1.0f;
  zoomFactor *= delta;
  translateX *= delta;
  translateY *= delta;
  translateX += mouseX;
  translateY += mouseY;
}


public static boolean isNumeric(String strNum) {
    if (strNum == null) {
        return false;
    }
    try {
        double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
        return false;
    }
    return true;
}

 public void keyPressed() {
  if(isSettingUnitSize) {
    if(isNumeric(String.valueOf(key)))
      sizeUnitBuffer += key;
    System.out.println(sizeUnitBuffer);
  }
  if(key=='s') {
    if(!isSettingUnitSize) {
      System.out.println("Setting scale factor mode on");
    } else {
      System.out.println("Setting scale factor mode off");
      oneUnitIs = Double.valueOf(sizeUnitBuffer);
      sizeUnitBuffer = "";
    }
    isSettingUnitSize = !isSettingUnitSize;
  }
  
  if (key == 'r') {
      zoomFactor = 1;
      translateX = 0.0f;
      translateY = 0.0f;
  }
  if (keyCode == 32) {
      isPanning = true;
  }
}

 public void keyReleased() {
  isPanning = false;
  if(key=='d') {
    if(lines.size()>0)
    lines.remove(lines.size()-1);
  }
}
public class Line {
  public PVector start;
  public PVector end;
  public String label;
  public Line(PVector _start, PVector _end) {
    this.start = _start;
    this.end = _end;
    this.label = String.valueOf(this.getLineLength());
  }
  
  public double getLineLength() {
    return Math.sqrt(Math.pow(this.start.x-this.end.x,2) + Math.pow(this.start.y-this.end.y,2));
  }
  
}


  public void settings() { size(1024, 768); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "dimensionsFromImage" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
