# Informe de Lab 2 Paradigmas | Grupo 24

## Integrantes:
- Martín Maximiliano Roberto Monnittola
- Ariel Anelio Alvarez Godoy
- Agustin Chapuis

## Breve Resumen:


### Trait Model

Se definio los siguientes metodos del trait Model:

```matchWithFilters:```  toma como parametro de entrada un diccionario y retoran un Booleano, el 
                   diccionario de entrada consta de las asociasiones entre los atributos y 
                   los valores de los mismo con los que se determina si el objeto sobre el
                   que es llamado el metodo cumple o no.

```validateKeys:```  toma como parametro de entrada un conjunto de String y no retorna valor alguno .
               La tarea de este procedimiento es controlar que los elementos del conjunto tomado
               como entrada sean todos nombres de algun atributo miembro de la clase excepto el 
               atributo 'id'.

### Class Freelancer

Se definio la clase freelancer la cual contiene los atributos y metodos necesarios para poder contener 
los datos del usuario freelancer y los metodos necesarios para operar sobre estos datos.

Se definieron los siguientes atributos:

```username:``` Nombre del usuario
```country_code:``` Codigo del pais
```category_ids:``` Ids de las categorias en las que puede trabajar
```reputation:``` Reputacion
```hourly_price:``` Precio que cobra por hora
```total_earning:``` Total de dinero ganado

Los metodos definidos fueron:

```fromJson:``` Se sobreescribio el metodo fromJson heredado del trait model, debido a que
          la Clase Freelancer tenia atributos propios no definidos en Model, pero se lo llamo
          (al metodo de Model) para que procesara los atributos definidos en dicho Trait, esto
          se decidio asi porque consideramos que no se deberia reescribir codigo y tambien
          porque pensamos que el procedimiento que procese dichos datos debia realizar la tarea
          donde se definieron esos atributos.
		  También tomamos la desición de que si al momento de extraer del Jsonvalue con una llave
		  un valor que no corresponda al tipo de dato esperado se lance la excepción `IllegalArgumentException`
	      para luego capturarla con un catch y actuar en consecuencia (por ejemplo si esperamos un valor
		  JString y es un JInt u otro se procede a lanzar la excepción). En el caso que no se obtenga un valor
		  al extraer con la llave `reputation` el mismo atributo se setea con `"Junior"` como fue indicado en la
		  consigna.

```toMap:``` Se sobreescribio el metodo heredado de Model, debido a que se debia contemplar los
        atributos de la Clase, pero se lo llamo para que procesara los atributos que se heredaban
        de Model.

```validateKeys:``` toma un conjunto de String y controla que todos sus elementos sean nombres de
         atributos de la Clase que se puedan setear al hacer un POST. Se considera válida la presencia o no
		 de un valor para el atributo `reputation`

```validateCategoryId:``` Toma una lista de enteros con los ids de todas las categorias existentes 
         y controla que los ids de freelancer sean correctas(que existan las categorias).
         En caso que esto no se cumple se genera una excepción para luego ser capturada en un catch.

```matchWithFilters:``` Toma un mapa con los atributos a filtrar y sus respectivos valores y 
         corrobora que los mismos sean atributos validos en la clase Freelancer.
         Tambien controla que si filtra por una id de categoria, la misma sea válida entre las 
         categorias existentes. 

```IncrementTotal_earning:``` Toma un entero y lo suma a lo total ganado por el freelancer.

### Class Client

Se definio la clase cliente la cual contiene los atributos y metodos necesarios para poder contener 
los datos del usuario cliente y los metodos necesarios para operar sobre estos datos.

Se definieron los siguientes atributos:

```username:``` representa el nombre del usuario
```country_code:``` representa el codigo del pais
```total_spend:``` representa el dinero total gastado

Los metodos definidos fueron:

```getUsername:``` para obtener el nombre del cliente
```getCountry_code:``` para obtener el codigo del pais
```getTotal_spend:``` para obtener el total gastado hasta el momento

```fromJson:``` Se sobreescribio el metodo fromJson heredado del trait model, debido a que
          la Clase Client tenia atributos propios no definidos en Model, pero se lo llamo
          (al metodo de Model) para que procesara los atributos definidos en dicho Trait, esto
          se decidio asi porque consideramos que no se deberia reescribir codigo y tambien
          porque pensamos que el procedimiento que procese dichos datos debia realizar la tarea
          donde se definieron esos atributos.

```toMap:``` Se sobreescribio el metodo heredado de Model, debido a que se debia contemplar los
        atributos de la Clase, pero se lo llamo para que procesara los atributos que se heredaban
        de Model.

```validateKeys:``` toma un conjunto de String y controla que todos sus elementos sean nombres de
               atributos de la Clase Client.

```IncrementTotal_spend:``` Toma un entero como parametro de entrada y con este incrementa el valor
               del atributo Total_spend. Se decidio crear el metodo para mantener en 

### Class Job

Se definio la clase Job la cual contiene los atributos y metodos necesarios para poder
contener los datos de cada trabajo y los metodos necesarios para operar sobre estos datos.

Atributos:

```title:``` Titulo del trabajo
```category:``` Id de la categoria del trabajo
```client_id:```  Numero de id del cliente
```preferred_expertise:``` Experiencia que prefiere el cliente que publica el trabajo
```preferred_country:``` Pais que prefiere el cliente que publica el trabajo
```hourly_price:``` Precio que se va a pagar por este trabajo (por hora)

Metodos definidos:

```validateCategoryId:``` Toma una lista de enteros que son los ids de todas las 
         categorias existentes y controla que el id del trabajo posteado este 
         entre estos

```validateClientId:``` Toma una lista de enteros que son los ids de los clientes
         ya registrados y controla que el id del usuario que postea el trabajo este
         entre estos

### Preguntas de la catedra:

Mencionen y expliquen al menos dos casos en los que utilizaron herencia para evitar tener que duplicar código.
¿En qué clase se encargan de controlar que las categorías asignadas a un freelancer efectivamente existan? ¿Por qué es responsabilidad de esa clase y no de otra?

1- Los metodos de fromJson y toMap son dos casos donde se uso la herencia para evitar la dulicacion de codigo
ya que a pesar de que se tuvo que sobreescribir dichos metodos al implementar los requerimientos de la catedra,
se hizo una llamada a super en la clase Client y Freelance para evitar tener que escribir el codigo necesario
para manejar el atributo Id, ya sea para pasarlo a un diccionario o para crear un objeto a partir de un Json.

2- Habia dos formas de abordar este problema:
     La primera forma era crear un metodo en la Clase databaseTable para que controle que en su atributo
     ```_instances```, que es un diccionario , tenga una clave que machee con la clave ingresada para el Freelancer
     a crear.
     La segunda era listar las claves de las Categorias disponibles o los Idś en el endpoint correspondiente
     en este caso  ```post /api/freelancers```, crear el Objeto Freelancer con los valores que deeberian 
     ser categorias y llamar a un metodo definido en la Clase Freelancer que tome la lista de ids de 
     Categorias cargados en el sistema y los compare con la lista almacenada en si mismo (en una atributo 
     propio)
   
   Se eligio el punto dos para implementar nuestro codigo, pero al analizar al final lo implementado nos
   dimos cuenta que el punto uno era mejor, el tiempo no nos alcanzo para modificarlo.

¿Qué concepto de la programación orientada a objetos es el que les permite que un mismo endpoint como `api/freelancers` tome distintos parámetros?
FreelanceandoServlet
Consideramos que el concepto de POO que permite que que un mismo endpoint tome distintos parametros es

  * La ```busqueda dinamica``` debido a que si una ruta no coincide con la url entrante se busca la siguiente

    de acuerdo a un orden especificado por el framework.

  * La ```Abstraccion``` ya que se puede acceder a los distintos parametros tratando al cuerpo de la peticion

    como un objeto.


* ¿Dónde colocaron el código con la lógica de la acción pagar? ¿Lo dividieron entre varios objetos, o pusieron todo en un mismo lugar?
* ¿Dónde tendrían que agregar código si se decidiera llamar a una API externa como MercadoPago para realizar la transacción?

1- Se dividio la logica de la accion pagar en distintos objetos, primero el incremento del monto
   gastado del cliente se lo realizo creando un metodo en la clase Client,IncrementTotal_spend,el cual
   modifica el atributo total_spend, luego el incremento del monto del total ganado por el Freelancer se lo 
   realiza en la Clase Freelancer a traves del metodo IncrementTotal_earning, el cual modifica el atributo 
   total_earning. Ambos metodos son llamados desde el bloque de accion de la ruta ```post /api/posts/pay```
   para realizar el procesamiento completo.

2- Se agregaria codigo en la Clase FreelanceandoServlet para hacer el llamado, pero se deberia retocar la
   Clase Freelancer y Client para agregar miembros y metodos que soporten las cuentas y los mecanismo
   de autenticacion que exije el sitio de mercado libre.


   
     



               
