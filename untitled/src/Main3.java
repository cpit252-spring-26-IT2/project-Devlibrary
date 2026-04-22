// 1. The Products
interface Ship {
    void sail();
    void fireCannons();
}

class PirateShip implements Ship {
    public void sail() { System.out.println("Hoisting the Jolly Roger! Sailing to find the One Piece."); }
    public void fireCannons() { System.out.println("Firing cannons at the Marines!"); }
}

class MarineShip implements Ship {
    public void sail() { System.out.println("Raising the World Government flag! Patrolling the seas."); }
    public void fireCannons() { System.out.println("Firing cannons at the Pirates!"); }
}

// 2. The Bad Creator (Tightly coupled)
abstract class Shipyard {

    // 1. THE FACTORY METHOD
    // It returns a Ship, and takes no parameters. The subclasses will override this!
    public abstract Ship createShip();

    // 2. THE CORE LOGIC
    // It calls the factory method to get a ship, then deploys it.
    public void deployShip() {
        Ship s = createShip(); // Get the ship from the factory
        System.out.println("--- A new ship has been built! ---");
        s.sail();
        s.fireCannons();
    }
}
class PirateShipyard extends Shipyard{

    @Override
    public Ship createShip() {
        return new PirateShip();
    }
}
class MarineShipyard extends Shipyard{

    @Override
    public Ship createShip() {
        return new MarineShip();
    }
}

public class Main3 {
    public static void main(String[] args) {
        Shipyard shipyard = new PirateShipyard();
        shipyard.deployShip();
        Shipyard shipyard2 = new MarineShipyard();
        shipyard2.deployShip();
    }
}