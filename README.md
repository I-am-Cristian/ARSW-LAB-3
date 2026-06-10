# Laboratorio 03

### Sistema de Gestión de Salones

### Descripcion
Diseñe e implemente un servidor TCP para gestionar la reserva de salones de la Escuela. Este ejercicio debe reutilizar el estilo cliente-servidor, pero no debe copiar literalmente el caso de películas.

### Requisitos funcionales 
- El servidor debe mantener en memoria una lista inicial de salones: E301, E302, E303 y E304. 
- Cada salón puede estar disponible o reservado.
- El cliente debe poder consultar el estado de un salón.
- El cliente debe poder reservar un salón disponible.
- El cliente debe poder liberar un salón reservado

### Pruebas

Cliente
![alt text](rooms-tcp/resources/image.png)

Servidor
![alt text](rooms-tcp/resources/image-1.png)

El servidor debe mantener en memoria una lista

![alt text](rooms-tcp/resources/image-2.png)

Cada salón puede estar disponible o reservado.

Dispoonible
![Disponible](rooms-tcp/resources/image-4.png)
Reservado
![Reservado](rooms-tcp/resources/image-3.png)

El cliente debe poder consultar el estado de un salón.

![alt text](rooms-tcp/resources/image-5.png)

El cliente debe poder reservar un salón disponible.

![alt text](rooms-tcp/resources/image-6.png)

El cliente debe poder liberar un salón reservado

![alt text](rooms-tcp/resources/image-7.png)

![alt text](rooms-tcp/resources/image-8.png)

Error salon no existe y Operacion invalida

![alt text](rooms-tcp/resources/image-9.png)

### Preguntas de reflexión

- ¿Qué tan fácil sería agregar una nueva operación al protocolo?

    Es fácil pero con varios puntos de cambio obligatorios que aumentan el riesgo de errores.
    Cambios necesarios para agregar, por ejemplo, una operación `UPDATE_CAPACITY:id:nuevaCapacidad:`
    | Archivo | Líneas a modificar |
    |----------|-------------------|
    | `RoomServer.java` | 1. Agregar `case "UPDATE_CAPACITY"` en el `switch` (línea ~45)<br>2. Crear método `handleUpdateCapacity()`<br>3. Agregar validación de formato |
    | `Room.java` | Agregar método `setCapacity(int capacity)` |
    | `RoomClient.java` | 1. Agregar opción en el menú (líneas ~15-20)<br>2. Agregar lógica de construcción del comando |

    Vulnerabilidad crítica: Si agregas una operación en el servidor pero NO actualizas el cliente, este último enviará mensajes que el servidor rechazará con `"ERROR: Comando desconocido"`. No hay manera de que el cliente "descubra" automáticamente las operaciones disponibles.

- ¿Qué ocurre si dos clientes intentan reservar el mismo salón al mismo tiempo?

    HAY UNA CONDICIÓN DE CARRERA (RACE CONDITION) GRAVE POR LO QUE PASARIA ESTO:

    TIEMPO | Cliente A (Juan)        | Cliente B (Maria)        | Estado Salón E301
    -------|-------------------------|--------------------------|------------------
    T1     | handleReserve("E301")   |                          | reserved = false
    T2     | room = findById("E301") |                          | reserved = false
    T3     | room.reserve("Juan")    |                          | 
    T4     |   Lee: reserved = false |                          | reserved = false
    T5     |   Evalúa: !reserved=True|                          | reserved = false
    T6     |                          | handleReserve("E301")    | reserved = false
    T7     |                          | room = findById("E301")  | reserved = false
    T8     |                          | room.reserve("Maria")    | reserved = false
    T9     |                          |   Lee: reserved = false  | reserved = false
    T10    |   reserved = true        |                          | reserved = true (Juan)
    T11    |   reservedBy = "Juan"    |                          | reservedBy = "Juan"
    T12    |                          |   reserved = true        | reserved = true
    T13    |                          |   reservedBy = "Maria"   | reservedBy = "Maria" SOBRESCRIBE!
    T14    | Devuelve true (OK)       | Devuelve true (OK)       | AMBOS creen que reservaron

- ¿Dónde está definido realmente el contrato de comunicación: en un archivo formal o en convenciones de texto?

    El contrato está fragmentado en 4 lugares diferentes, todos informales y ninguno enforceable automáticamente.

    | Ubicación | Lo que define | Ejemplo en el código |
    |-----------|---------------|----------------------|
    | **Servidor (líneas 12-14)** | Comandos y formato | `System.out.println("GET_ROOM:id");` |
    | **Servidor (switch líneas 45-52)** | Comandos válidos | `case "GET_ROOM":`<br>`case "RESERVE":` |
    | **Servidor (métodos handler)** | Formato específico | `if (parts.length < 2) return "ERROR: Formato inválido. Use GET_ROOM:id";` |
    | **Cliente (líneas 23-26)** | Mapeo de comandos | `if (input.equalsIgnoreCase("list")) input = "LIST_ROOMS";` |
    | **Cliente (mensajes de ayuda)** | Documentación para usuario | `System.out.println("1. Consultar salón (GET_ROOM:id)");` |