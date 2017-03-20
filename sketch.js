var meteors = [];
var meteorCount = 10;
var ship;
var lasers = [];

var keysDown = [];

function setup()
{
  //Disable right clcik dialog
  frameRate(60);
  canvas = createCanvas(docWidth(), docHeight());
  for(var i = 0; i < meteorCount; i++)
  {
    meteors[i] = new Meteor(-75 + random(width), -75, random(35, 100),
      new Vec2D(random(0.5, 4), 0).rot(random(0, TWO_PI)),
      random(-PI / 200, PI / 200));
  }
  ship = new Ship(width / 2, height / 2, new Vec2D(0, 0), 0);
}

var time = 0;
var lST = 0;

function draw()
{
  time += 1 / 60;
  background(16);
  noFill();
  stroke(255);
  strokeWeight(2);
  for(var i = 0; i < meteors.length; i++)
  {
    meteors[i].show();
    meteors[i].update();
  }
  for(var i = 0; i < lasers.length; i++)
  {
    lasers[i].update();
    lasers[i].show();
    if(lasers[i].hit)
    {
      lasers.splice(i, 1);
    }
  }
  ship.show();
  ship.update();
  var vel = new Vec2D(0, 0);
  if(keysDown[87]) //W
  {
    vel.y += -1;
  }
  if(keysDown[83]) //S
  {
    vel.y += 1;
  }
  if(keysDown[65]) //A
  {
    vel.x += -1;
  }
  if(keysDown[68]) //D
  {
    vel.x += 1;
  }
  var delta = new Vec2D(mouseX - ship.pos.x, mouseY - ship.pos.y);
  ship.rot = atan2(delta.y, delta.x) + PI / 2;
  vel.limit(1);
  vel.mult(5);
  ship.vel.add(vel);
  ship.vel.limit(10);
  if(floor(lST) < floor(time) && meteors.length < 40)
  {
    meteors.push(new Meteor(-50, -50, random(35, 100),
      new Vec2D(random(0.5, 4), 0).rot(random(0, TWO_PI)),
      random(-PI / 200, PI / 200)));
    lST = time;
  }
}

function mouseClicked()
{
  ship.fire();
}

function keyPressed()
{
  keysDown[keyCode] = true;
}

function keyReleased()
{
  keysDown[keyCode] = false;
}

function windowResized()
{
  resizeCanvas(docWidth(), docHeight());
}

function docWidth()
{
  return document.documentElement.clientWidth;
}

function docHeight()
{
  return document.documentElement.clientHeight;
}

function triangleArea(x1, y1, x2, y2, x3, y3)
{
  var a = dist(x1, y1, x2, y2);
  var b = dist(x1, y1, x3, y3);
  var c = dist(x2, y2, x3, y3);
  var s = (a + b + c) / 2;
  return sqrt(s * (s - a) * (s - b) * (s - c));
}

class Vec2D
{
  constructor(x, y)
  {
    this.x = x;
    this.y = y;
  }

  mag()
  {
    return sqrt(this.x * this.x + this.y * this.y);
  }

  normalize()
  {
    var mag = this.mag();
    this.x /= mag;
    this.y /= mag;
  }

  limit(mag)
  {
    if(this.mag() > mag)
    {
      this.normalize()
      this.x *= mag;
      this.y *= mag;
    }
  }

  add(vec)
  {
    this.x += vec.x;
    this.y += vec.y;
  }

  mult(val)
  {
    this.x *= val;
    this.y *= val;
  }

  zero()
  {
    this.x = 0;
    this.y = 0;
  }

  rot(angle)
  {
    var x = this.x;
    this.x = x * cos(angle) - this.y * sin(angle);
    this.y = x * sin(angle) + this.y * cos(angle);
    return this;
  }
}

class BBTriangle
{
  constructor(p1, p2, p3)
  {
    this.p1 = p1;
    this.p2 = p2;
    this.p3 = p3;
    this.area = triangleArea(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
  }

  check(x, y)
  {
    return floor(triangleArea(this.p1.x, this.p1.y, this.p2.x, this.p2.y, x, y)
      + triangleArea(this.p2.x, this.p2.y, this.p3.x, this.p3.y, x, y)
      + triangleArea(this.p1.x, this.p1.y, this.p3.x, this.p3.y, x, y))
      <= this.area;
  }

  rotate(center, angle)
  {
    var p1 = new Vec2D(center.x - this.p1.x, center.y - this.p1.y);
    var p2 = new Vec2D(center.x - this.p2.x, center.y - this.p2.y);
    var p3 = new Vec2D(center.x - this.p3.x, center.y - this.p3.y);
    p1.rot(angle);
    p2.rot(angle);
    p3.rot(angle);
    this.p1 = new Vec2D(center.x - p1.x, center.y - p1.y);
    this.p2 = new Vec2D(center.x - p2.x, center.y - p2.y);
    this.p3 = new Vec2D(center.x - p3.x, center.y - p3.y);
  }

  translate(vec)
  {
    this.p1.add(vec);
    this.p2.add(vec);
    this.p3.add(vec);
  }

  show()
  {
    push();
    stroke(0, 255, 0);
    triangle(this.p1.x, this.p1.y, this.p2.x, this.p2.y, this.p3.x, this.p3.y);
    pop();
  }
}

class BBMesh
{
  constructor(bbts)
  {
    this.bbts = bbts;
  }

  check(x, y)
  {
    for(var i = 0; i < this.bbts.length; i++)
    {
      if(this.bbts[i].check(x, y))
      {
        return true;
      }
    }
    return false;
  }

  rotate(center, angle)
  {
    for(var i = 0; i < this.bbts.length; i++)
    {
      this.bbts[i].rotate(center, angle);
    }
  }

  translate(vec)
  {
    for(var i = 0; i < this.bbts.length; i++)
    {
      this.bbts[i].translate(vec);
    }
  }

  show()
  {
    for(var i = 0; i < this.bbts.length; i++)
    {
      this.bbts[i].show();
    }
  }
}

class Meteor
{
  constructor(x, y, size, heading, rotation)
  {
    this.pos = new Vec2D(x, y);
    this.vel = heading;
    this.rot = rotation;
    this.size = size;
    this.verts = [];
    var seed = random(100000);
    for(var theta = 0; theta < TWO_PI + 0.1; theta += PI / 10)
    {
      var n = size / 4 + noise(seed, (PI - abs(theta - PI))) * size * 3 / 4;
      this.verts.push(new Vec2D(x + cos(theta) * n, y + sin(theta) * n));
    }
    var bbts = [];
    for(var i = 0; i < this.verts.length - 1; i++)
    {
      bbts.push(new BBTriangle(new Vec2D(x, y),
        new Vec2D(this.verts[i].x, this.verts[i].y),
        new Vec2D(this.verts[i + 1].x, this.verts[i + 1].y)));
    }
    this.mesh = new BBMesh(bbts);
  }

  updateVelocity()
  {
    this.pos.add(this.vel);
    for(var i = 0; i < this.verts.length; i++)
    {
      this.verts[i].add(this.vel);
    }
    this.mesh.translate(this.vel);
    var fix = new Vec2D(0, 0);
    if(this.pos.x - this.size > width)
    {
      //Move to the left
      fix.x -= width + this.size * 2
    }
    else if(this.pos.x + this.size < 0)
    {
      //Move to the right
      fix.x += width + this.size * 2;
    }
    if(this.pos.y - this.size > height)
    {
      //Move to the top
      fix.y -= height + this.size * 2;
    }
    else if(this.pos.y + this.size < 0)
    {
      // Move to the bottom
      fix.y += height +  this.size * 2;
    }
    this.pos.add(fix);
    for(var i = 0; i < this.verts.length; i++)
    {
      this.verts[i].add(fix);
    }
    this.mesh.translate(fix);
  }

  updateRotation()
  {
    this.mesh.rotate(this.pos, this.rot);
    for(var i = 0; i < this.verts.length; i++)
    {
      var vert = this.verts[i];
      var p1 = new Vec2D(this.pos.x - vert.x, this.pos.y - vert.y);
      p1.rot(this.rot);
      this.verts[i] = new Vec2D(this.pos.x - p1.x, this.pos.y - p1.y);
    }
  }

  destroy()
  {
    meteors.splice(meteors.indexOf(this), 1);
  }

  update()
  {
    this.updateVelocity();
    this.updateRotation();
  }

  show()
  {
    stroke(255);
    noFill();
    beginShape();
    for(var i = 0; i < this.verts.length; i++)
    {
      vertex(this.verts[i].x, this.verts[i].y);
    }
    endShape();
  }
}

class Ship
{
  constructor(x, y, heading, rotation)
  {
    this.pos = new Vec2D(x, y);
    this.vel = heading;
    this.rot = rotation;
  }

  fire()
  {
    lasers.push(new Laser(this.pos.x, this.pos.y,
      new Vec2D(1, 0).rot(this.rot), 15));
  }

  updateVelocity()
  {
    this.pos.add(this.vel);
    this.vel.mult(0.96);
    if(this.pos.x + 30 < 0)
    {
      this.pos.x = width + 30;
    }
    else if(this.pos.x - 30 > width)
    {
      this.pos.x = -30;
    }
    if(this.pos.y + 30 < 0)
    {
      this.pos.y = height + 30;
    }
    else if(this.pos.y - 30 > height)
    {
      this.pos.y = -30;
    }
  }

  update()
  {
    this.updateVelocity();
  }

  show()
  {
    fill(255);
    var a = new Vec2D(-30, 30);
    var b = new Vec2D(30, 30);
    var c = new Vec2D(0, -30);
    a.rot(this.rot);
    b.rot(this.rot);
    c.rot(this.rot);
    triangle(this.pos.x + a.x, this.pos.y + a.y, this.pos.x + b.x,
      this.pos.y + b.y, this.pos.x + c.x, this.pos.y + c.y);
  }
}

class Laser
{
  constructor(x, y, heading, speed)
  {
    this.pos = new Vec2D(x, y);
    this.heading = heading;
    this.heading.rot(-PI / 2);
    this.heading.normalize();
    this.heading.mult(speed);
    this.hit = false;
  }

  update()
  {
    for(var i = 0; i < meteors.length; i++)
    {
      if(meteors[i].mesh.check(this.pos.x, this.pos.y))
      {
        meteors[i].destroy();
        this.hit = true;
        break;
      }
    }
    this.pos.add(this.heading);
    if(this.pos.x > width + 10 || this.pos.x < -10 || this.pos.y > height + 10
      || this.pos.y < -10)
    {
      this.hit = true;
    }
  }

  show()
  {
    push();
    strokeWeight(4);
    stroke(255, 0, 0);
    point(this.pos.x, this.pos.y);
    pop();
  }
}
