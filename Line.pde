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
