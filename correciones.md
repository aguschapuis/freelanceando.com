# Grupo 24	por Facundo Bustos	
## Corrección		
	Tag o commit corregido:	xxxx
		
### Entrega y git		97,50%
	Informe	100,00%
	Commits de cada integrante	100,00%
	En tiempo y con tag correcto	75,00%
	Commits frecuentes y con nombres significativos	100,00%
### Funcionalidad		81,25%
	Parte1: pasa los test de respuesta con código 200 de freelancer	100,00%
	Parte2: pasa los tests de respuesta con código 200 de cliente	100,00%
	Parte2: pasa los tests de respuesta con código 200 de trabajo	100,00%
	Parte 3: pasa los test de filtrado de freelancer	100,00%
	Parte 3: pasa los test de filtrado de trabajo	50,00%
	Parte 4: pasa los test de pago	100,00%
	Parte 4: guardan el cliente y el freelancer después de modificar sus atributos	0,00%
	Manejan correctamente los casos donde el input tiene errores de campos o tipos	75,00%
### Modularización y diseño		77,50%
	Cada modelo está definido en un archivo separado	100,00%
	Usan conceptos de la programación funcional, como las funciones map y filter	75,00%
	Evitan repetir código aprovechando la herencia a partir de Model. 	100,00%
	Mantienen la separación de responsabilidades: el manejo de la API en el Servlet, el manejo de la tabla y el conjunto de instancias en DatabaseTable, y el manejo de atributos y métodos particulares en cada subclase de Model.	75,00%
	Usan DatabaseTable.get y Option para comprobar si los objetos adecuados existen antes de crear nuevos (ej. que las categorías existan antes de crear el freelancer)	100,00%
	El código de filtrado no está duplicado en cada una de las subclases de Model	0,00%
	La modificación de las instancias cliente y freelancer ocurre dentro de la clase correpondiente, y no en el endpoint	100,00%
### Calidad de código		75,00%
	Estilo de línea	75,00%
	Estructuras de código simples	50,00%
	Hacen uso de los métodos de las estructuras de datos estándares como Map o List, en lugar de intentar re-implementarlos	100,00%
	Reutilizan funciones de librería, por ejemplo para serializar y deserializar Json	100,00%
	Estilo de código	50,00%
### Opcionales		
	Punto estrella 1: User y serialización de Reputation	0,00%
	Punto estrella 2: Filtros ordenados	0,00%
	Punto estrella 3: Mantener referencias a objetos en lugar de ids	0,00%
	Puntos estrella 4: Contratos	0,00%
		
# Nota Final		7.46
		
		
# Comentarios		
	En lineas generales el lab está muy bien, solo señalo algunas cosas que se pueden mejorar.	
	Lograron todas las funcionalidades requeridas. 	
	Mucho código a nivel del servlet, es decir, toda la lógica en la ultima capa de la aplicacion.  Podrian haber creado una capa en el medio con la lógica de los endpoits. Como una capa de "controladores" para cada posible endpoint.	
	olvidaron poner el tag en el ultimo commit	
	No me gusta lo de "matchWithFilters" porque esta repetido tanto en jobs como en Freelancer, y parece que la funcionalidad de filtrar no depende de que subclase de Model estamos filtrando, debería estar definida para todas	
	Problemas con el endpoint de filtrado para Jobs	
	Documentar los casos de pruebas que realizaron ustedes para chequear su codigo	
		
		
