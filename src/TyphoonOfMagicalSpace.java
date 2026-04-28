import java.util.Scanner;

public class TyphoonOfMagicalSpace extends Magica {

    public TyphoonOfMagicalSpace() {
        super("Typhoon Of Magical Space");
    }

    @Override
    public void activar(Jugador jugador, Jugador oponente) {
        if (oponente.getCampo().isEmpty()) {
            System.out.println("No hay cartas para destruir");
            return;
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("Elige un monstruo enemigo:");
        for (int i = 0; i < oponente.getCampo().size(); i++) {
            System.out.println(i + ". " + oponente.getCampo().get(i).getNombre());
        }

        int idx = sc.nextInt();

        Monstruo eliminado = oponente.getCampo().remove(idx);

        System.out.println("🌪️ Destruiste " + eliminado.getNombre());
    }
}
