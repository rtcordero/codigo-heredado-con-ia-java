package aparcamiento;

public class Tarifa {

    // calcula lo que paga un vehiculo en centimos
    public int calcula(int min, String t, boolean res) {
        if (min < 15) {
            return 0;
        }
        int h = (min + 59) / 60;
        int precioHora;
        int tope;
        switch (t) {
            case "COCHE" -> { precioHora = 250; tope = 2000; }
            case "FURGO" -> { precioHora = 350; tope = 2800; }
            case "MOTO" -> { precioHora = 100; tope = 800; }
            default -> { return 0; }
        }
        if (res) {
            precioHora = precioHora * 80 / 100;
        }
        return Math.min(h * precioHora, tope);
    }
}
