# Código heredado con IA: caracteriza antes de cambiar

Es el código del artículo [Código heredado con IA: caracteriza antes de cambiar](https://rubentejera.com/tests-de-caracterizacion-codigo-legado-ia/), que explica cada paso.

Cuando hay que cambiar código heredado sin tests, un agente de IA puede «mejorar» de paso algo que nadie le pidió y cambiar un comportamiento sin que se note. Este repositorio muestra la salida: **tests de caracterización**, que fijan lo que el código hace hoy, no lo que debería hacer. El ejemplo es la tarifa de un aparcamiento en Java: un método enredado, sus tests, un cambio de un agente que rompe tres comportamientos y el refactor final con la red puesta.

Lo que demuestra está en el **historial**: un commit por paso, y cada commit falla o pasa exactamente como dice su mensaje.

## Cómo ejecutarlo

Necesitas JDK 25 o 27 y Maven 3.10.0, o solo Docker.

1. Clona el repositorio y entra en la carpeta.
2. Pasa los tests con Maven, o con Docker y la imagen oficial de Maven, sin instalar nada:

   ```bash
   mvn test
   scripts/mvn.sh test
   ```

3. Recorre el historial, del primer paso al último:

   ```bash
   git log --oneline --reverse
   git checkout <commit>   # y mvn test en cada paso
   git checkout main       # para volver
   ```

## Qué mirar primero

1. El historial: `git log --reverse --format=%s`. Los pasos acaban en su resultado: `(verde)` si los tests pasan y `(rojo)` si fallan.
   1. Código enredado, sin tests.
   2. Doce tests de caracterización (verde).
   3. El cambio propuesto por el agente para añadir las motos (rojo): tres tests de caracterización fallan.
   4. Se descarta la reescritura y se fijan las reglas de la moto con tests, que fallan contra el código original (rojo).
   5. Se añade solo la rama de la moto (verde).
   6. El refactor con la red de tests (verde).
2. [`TarifaTest.java`](src/test/java/aparcamiento/TarifaTest.java): los tests, que se leen como las reglas que nadie había escrito.
3. El diff del agente: `git show` del primer commit que acaba en «(rojo)».
4. [`Tarifa.java`](src/main/java/aparcamiento/Tarifa.java) y [`Vehiculo.java`](src/main/java/aparcamiento/Vehiculo.java): el código final, y cómo cambió con `git log -p src/main`.
5. [`scripts/comprobar-historial.sh`](scripts/comprobar-historial.sh): recorre el historial y falla si un commit «(rojo)» pasa o uno «(verde)» falla. La CI lo ejecuta en cada push.

```text
src/main/java/aparcamiento/
  Tarifa.java       ← la tarifa, con la misma firma pública que el código heredado
  Vehiculo.java     ← precio, tope y descuento de cada vehículo
src/test/java/aparcamiento/
  TarifaTest.java   ← tests de caracterización y los de la moto
scripts/
  mvn.sh                   ← Maven en Docker, sin root
  comprobar-historial.sh   ← cada commit hace lo que dice su mensaje
```

## Versiones

- JDK 25.0.5 (Eclipse Temurin, la LTS más reciente). Cada commit se ha comprobado también con JDK 27.0.1, y la CI pasa los tests en los dos.
- Maven 3.10.0, en las imágenes oficiales `maven:3.10.0-eclipse-temurin-25` y `maven:3.10.0-eclipse-temurin-27`.
- JUnit 6.1.3, maven-surefire-plugin 3.6.1 y maven-compiler-plugin 3.16.0.

## Cómo se hizo

Desarrollado con el enfoque [IA Limpia](https://rubentejera.com/que-es-la-ia-limpia/): un agente de IA escribe el código siguiendo los pasos del artículo, los tests validan cada paso y el resultado se revisa y se aprueba a mano antes de publicarlo.

**El cambio del agente del paso 3 es un ejemplo construido**, no la respuesta de una sesión real a ese encargo. Se escribió a propósito para ilustrar un riesgo: que el agente haga lo que se le pide y, además, «simplifique» el código y cambie de paso un comportamiento.

El historial reconstruye los pasos del artículo en su mismo orden, para poder recorrerlos uno a uno. Cada commit se ha ejecutado con JDK 25.0.5 y 27.0.1, y la CI comprueba en cada push que cada rojo falla y cada verde pasa (`scripts/comprobar-historial.sh`).

## Licencia

[MIT](LICENSE).

## Continuación

El mismo refactor, en doce pasos pequeños y cada uno en verde, está en `refactorizar-con-ia-java`, el código del artículo [Refactorizar con IA en pasos pequeños, sin romper nada](https://rubentejera.com/refactorizar-con-ia/).
