PImage img; // Declare a variable of type PImage
float xOffset = 0.0; 
float yOffset = 0.0;

ArrayList<Line> lines;

double scaleFactor = 0.0;
double oneUnitIs = 1.0;

boolean isSettingUnitSize = false;
boolean isPanning = false;

String sizeUnitBuffer = "";

float zoomFactor = 1.0;
float translateX = 0.0;
float translateY = 0.0;

void setup() {
  size(1024, 768);
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

void draw() {
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
    text(String.valueOf(((int)(line.getLineLength()/scaleFactor*oneUnitIs*100))/100.0), line.end.x, line.end.y);
    //blendMode(BLEND);
  }
  popMatrix();
  if(isPanning) {    
    translateX += mouseX - pmouseX;
    translateY += mouseY - pmouseY;
  }
}

void mousePressed() {
  xOffset = mouseX; 
  yOffset = mouseY;
}

void mouseReleased() {
  PVector start = new PVector((xOffset-translateX)/zoomFactor, (yOffset-translateY)/zoomFactor);
  PVector end = new PVector((mouseX-translateX)/zoomFactor, (mouseY-translateY)/zoomFactor);
  lines.add(new Line(start, end));
}

void mouseWheel(MouseEvent e) {
  translateX -= mouseX;
  translateY -= mouseY;
  float delta = e.getCount() < 0 ? 1.05 : e.getCount() > 0 ? 1.0/1.05 : 1.0;
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

void keyPressed() {
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
      translateX = 0.0;
      translateY = 0.0;
  }
  if (keyCode == 32) {
      isPanning = true;
  }
}

void keyReleased() {
  isPanning = false;
  if(key=='d') {
    if(lines.size()>0)
    lines.remove(lines.size()-1);
  }
}
