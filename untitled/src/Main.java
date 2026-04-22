// 1. The Interface (The label on the "Sealed Box")
// It only tells us that whatever uses this interface can clone itself and draw itself.
interface Shape {
    Shape clone();
    void draw();
}

// 2. Concrete Class A (A hidden possibility inside the box)
class Circle implements Shape {
    public int radius;

    public Circle(int radius) { this.radius = radius; }

    // Copy Constructor
    public Circle(Circle source) {
        this.radius = source.radius;
    }

    @Override
    public Shape clone() {
        return new Circle(this);
    }

    @Override
    public void draw() { System.out.println("Drawing a Circle with radius " + radius); }
}

// 3. Concrete Class B (Another hidden possibility inside the box)
class Rectangle implements Shape {
    public int width;
    public int height;

    public Rectangle(int width, int height) { this.width = width; this.height = height; }

    // Copy Constructor
    public Rectangle(Rectangle source) { this.width = source.width; this.height = source.height; }

    @Override
    public Shape clone() { return new Rectangle(this); }

    @Override
    public void draw() { System.out.println("Drawing a Rectangle: " + width + "x" + height); }
}

public class Main {
    public static void main(String[] args) {
        Shape c = new Circle(10);
        Shape myRect = new Rectangle(5, 8);

        System.out.println("--- Passing a Circle into the unknown method ---");
        Shape cCopy = c.clone();
        cCopy.draw();

        System.out.println("\n--- Passing a Rectangle into the unknown method ---");
        Shape s=myRect.clone();
        s.draw();
    }
}