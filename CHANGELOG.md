# Changelog

Todas las modificaciones importantes en el proyecto están en este documento.

## 1.1.0 - XX/XX/2025

### Arreglado
- Se resolvió un bug que te dejaba en bucle infinito al intentar mejorar las minas cuando no tenías recursos.
- Ya se debería poder vaciar la consola en cada iteración sin importar en qué consola o sistema operativo se intente.
- Se disminuyó el retraso al calcular la cantidad de lotes de joyería que se pueden producir con la cantidad actual de minerales.
- Ahora puede soportar al ingresar el tipo incorrecto de variable al preguntar por un entero.
- Algunos mensajes de errores incompletos o que no estaban en el lugar correcto.

### Añadido
- Ahora todos los datos se obtendrán de un archivo de texto plano llamado `datos.txt`.
- El progreso se guardará en el archivo mencionado anteriormente.

### Alterado
- Se cambiaron los controles para el juego para mantener una mejor consistencia del lenguaje.
- El coste de los lotes de joyería y su valor ahora son variables para facilitar su cambio y balance.
- Para optimizar código y evitar repetirlo se convirtió el pedir cantidad de lotes a fabricar en una función.
- Ahora el símbolo del dolar ($) está antes del número en vez de estar después para que tenga sentido con el formato universal.