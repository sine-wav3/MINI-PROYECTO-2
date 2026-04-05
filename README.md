<h3>MINI-PROYECTO-1</h3>
<h3>DANIELA FRANCO IBARRA</h3>
<h3>DAYAN STEFANY MARULANDA</h3>
<h3>JUAN ALEJANDRO MARQUEZ</h3>

 Yu-Gi-Oh! 
Mini Proyecto 1 – Programación Orientada a Eventos (Java)

 Descripción del juego
Este proyecto implementa una simulación simplificada de un duelo de Yu-Gi-Oh! ejecutado completamente por consola en Java 21.
Dos jugadores se enfrentan utilizando mazos de cartas compuestos por monstruos y cartas mágicas. Cada jugador inicia con 8000 puntos de vida (LP) y el objetivo es reducir los LP del oponente a 0 o hacer que pierda por quedarse sin cartas en el mazo.
El sistema modela entidades del juego como clases, aplicando los principios fundamentales de la Programación Orientada a Objetos (OOP).

 Instrucciones de ejecución
 Requisitos
Java JDK 21 o superior
Consola (CMD, PowerShell, terminal Linux o Mac)

 Pasos para ejecutar
Clonar el repositorio:
git clone < https://github.com/sine-wav3/MINI-PROYECTO-1.git>

Acceder al proyecto:
cd <MINI-PROYECTO-1>

Compilar los archivos en un Entorno de Desarrollo Integrado (IDE) como Visual Studio.

Ejecutar el programa:
java Juego


Mecánica del juego
 Inicio
Se ingresan los nombres de los jugadores
Se genera un conjunto de cartas (monstruos y mágicas)
Se reparten 20 cartas a cada jugador
Cada jugador roba 5 cartas iniciales
Se selecciona aleatoriamente quién comienza

 Turno de juego
Cada turno sigue este flujo:
El jugador roba 1 carta
Se muestra el estado actual:
LP de ambos jugadores
Número de monstruos en campo
Se elige una acción:
1. Jugar carta
2. Atacar
3. Cambiar modo
4. Pasar turno


 Acciones disponibles
 Jugar carta
Monstruo: se invoca al campo
Mágica: se activa inmediatamente y ejecuta su efecto

 Atacar
Si el oponente no tiene monstruos → ataque directo
Si tiene:
Se selecciona un objetivo
Si ATK atacante > DEF defensor:
Se destruye el monstruo
Se inflige daño por la diferencia

 Cambiar modo
Permite alternar entre:
Modo ataque
Modo defensa
Solo una vez por turno por monstruo

 Pasar turno
No se realiza ninguna acción

Condiciones de victoria
Un jugador gana si:
El oponente llega a 0 LP
El oponente intenta robar carta y su mazo está vacío

Estructura del proyecto
 Clase abstracta Carta
Atributo: nombre
Base para todas las cartas

 Clase Monstruo (hereda de Carta)
Atributos:
ATK (ataque)
DEF (defensa)
nivel
modo (ataque/defensa)
Funcionalidades:
Cambio de modo (cambiarModo())
Restricción de cambio por turno
Modificación de estadísticas (buffs)

 Interfaz Activable
Define el comportamiento de cartas con efectos:
void activar(Jugador jugador, Jugador oponente);


 Clase Mágica
Hereda de Carta
Implementa Activable
Representa cartas con efectos inmediatos

 Cartas mágicas implementadas
Carta
Efecto
PotOfGreed
Roba 2 cartas
Hinotama
Inflige 500 de daño directo
DarkHole
Destruye todos los monstruos en el campo
Raigeki
Destruye todos los monstruos del oponente
BoostAtk
Aumenta ATK de un monstruo en 500
StandarOfCourage
Aumenta ATK de todos tus monstruos en 200
AceleronMiauravilloso
Aumenta DEF de un monstruo en 200
AcesCoup
Lanza moneda: roba 2 cartas tú o el oponente
ChangeOfHeart
Roba un monstruo del oponente
TyphoonOfMagicalSpace
Destruye un monstruo enemigo específico


 Clase Jugador
Responsabilidades:
Manejo de:
mano
campo
mazo
puntos de vida (LP)
Métodos clave:
robarCarta()
jugarCarta()
recibirDano()
mostrarMano()
mostrarCampo()

 Clase Juego (main)
Controla:
Flujo del juego
Turnos
Acciones del jugador
Sistema de combate

Conceptos de OOP aplicados
 Encapsulamiento
Atributos privados en clases
Acceso mediante getters y setters

 Herencia
Carta → Monstruo, Magica

Polimorfismo
Uso de instanceof para distinguir tipos de carta
Ejecución dinámica del método activar()

 Interfaces
Activable define comportamiento común para cartas mágicas




